/**
 * Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.ds.envirocar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.envirocar.server.core.TemporalFilterOperator;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.n52.sos.convert.ConverterException;
import org.n52.sos.ds.AbstractGetObservationDAO;
import org.n52.sos.ds.EnviroCarConstants;
import org.n52.sos.ds.envirocar.util.OmObservationMeasurementsCreator;
import org.n52.sos.exception.ows.NoApplicableCodeException;
import org.n52.sos.exception.ows.concrete.MissingObservedPropertyParameterException;
import org.n52.sos.exception.ows.concrete.NotYetSupportedException;
import org.n52.sos.ogc.filter.FilterConstants;
import org.n52.sos.ogc.filter.FilterConstants.TimeOperator;
import org.n52.sos.ogc.filter.SpatialFilter;
import org.n52.sos.ogc.filter.TemporalFilter;
import org.n52.sos.ogc.gml.time.TimeInstant;
import org.n52.sos.ogc.gml.time.TimePeriod;
import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.ConformanceClasses;
import org.n52.sos.ogc.sos.Sos1Constants;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.request.GetObservationRequest;
import org.n52.sos.request.SpatialFeatureQueryRequest;
import org.n52.sos.response.GetObservationResponse;
import org.n52.sos.service.Configurator;
import org.n52.sos.util.CollectionHelper;
import org.n52.sos.util.GeometryHandler;
import org.n52.sos.util.http.HTTPStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public class EnviroCarGetObservationDAO extends AbstractGetObservationDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnviroCarGetObservationDAO.class);

    private final EnviroCarDaoFactoryHolder daoFacHoler = new EnviroCarDaoFactoryHolder();

    private static final Set<String> validTemporalFilterValueReferences = Sets.newHashSet("phenomenonTime",
            "om:phenomenonTime");

    public EnviroCarGetObservationDAO() {
        super(SosConstants.SOS);
    }

    @Override
    public String getDatasourceDaoIdentifier() {
        return EnviroCarConstants.ENVIROCAR_DATASOURCE_DAO_IDENTIFIER;
    }

    @Override
    public Set<String> getConformanceClasses() {
        return Sets.newHashSet(ConformanceClasses.SOS_V2_SPATIAL_FILTERING_PROFILE);
    }

    @Override
    public GetObservationResponse getObservation(GetObservationRequest request) throws OwsExceptionReport {
        if (request.getVersion().equals(Sos1Constants.SERVICEVERSION) && request.getObservedProperties().isEmpty()) {
            throw new MissingObservedPropertyParameterException();
        }
        if (request.isSetResultFilter()) {
            throw new NotYetSupportedException("result filtering");
        }
        final GetObservationResponse response = new GetObservationResponse();
        response.setService(request.getService());
        response.setVersion(request.getVersion());
        response.setResponseFormat(request.getResponseFormat());
        if (request.isSetResultModel()) {
            response.setResultModel(request.getResultModel());
        }
        try {
            response.setObservationCollection(queryObservations(request, daoFacHoler.getEnviroCarDaoFactory()));
        } catch (ConverterException ce) {
            throw new NoApplicableCodeException().causedBy(ce).withMessage("Error while processing observation data!")
                    .setStatus(HTTPStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    private List<OmObservation> queryObservations(GetObservationRequest request,
            EnviroCarDaoFactory enviroCarDaoFactory) throws OwsExceptionReport, ConverterException {
        Set<String> features =
                getFeatures(request, request.hasSpatialFilteringProfileSpatialFilter(), enviroCarDaoFactory);
        MeasurementFilter filter = new MeasurementFilter();
        if (request.isSetOffering() || request.isSetProcedure()) {
            Set<String> sensors;
            if (request.isSetOffering() && request.isSetProcedure()) {
                sensors =
                        Sets.newHashSet(CollectionHelper.conjunctCollections(request.getOfferings(),
                                request.getProcedures()));
                if (sensors.size() == 0) {
                    return new ArrayList<OmObservation>();
                }
            } else {
                sensors = Sets.newHashSet();
                if (request.isSetOffering()) {
                    sensors.addAll(request.getOfferings());
                }
                if (request.isSetProcedure()) {
                    sensors.addAll(request.getProcedures());
                }
            }
            filter.setSensorIds(sensors);
        }
        if (request.isSetObservableProperty()) {
            filter.setPhenomenonIds(request.getObservedProperties());
        }
        if (CollectionHelper.isNotEmpty(features)) {
            filter.setTrackIds(features);
        }
        if (request.isSetTemporalFilter()) {
            filter.setTemporalFilter(toEcTemporalFilter(request.getTemporalFilters()));
        }
        if (request.hasSpatialFilteringProfileSpatialFilter()) {
            if (!FilterConstants.SpatialOperator.BBOX.equals(request.getSpatialFilter().getOperator())) {
                throw new NotYetSupportedException(Sos2Constants.GetObservationParams.spatialFilter.name(), request
                        .getSpatialFilter().getOperator().name());
            }
            filter.setGeometry(GeometryHandler.getInstance().switchCoordinateAxisOrderIfNeeded(
                    request.getSpatialFilter().getGeometry()));
        }
        return toObservations(enviroCarDaoFactory.getMeasurementDAO().get(filter), request, enviroCarDaoFactory);
    }

    private Set<String> getFeatures(final SpatialFeatureQueryRequest request,
            boolean hasSpatialFilteringProfileSpatialFilter, final EnviroCarDaoFactory enviroCarDaoFactory)
            throws OwsExceptionReport {
        if (hasSpatialFilteringProfileSpatialFilter) {
            return getFeatureIdentifier(null, request.getFeatureIdentifiers(), enviroCarDaoFactory);
        } else {
            return getFeatureIdentifier(request.getSpatialFilter(), request.getFeatureIdentifiers(),
                    enviroCarDaoFactory);
        }
    }

    private Set<String> getFeatureIdentifier(SpatialFilter spatialFilter, List<String> featureIdentifier,
            EnviroCarDaoFactory enviroCarDaoFactory) throws OwsExceptionReport {
        Set<String> foiIDs = null;
        // spatial filter
        if (spatialFilter != null) {
            if (spatialFilter.getValueReference().contains("om:featureOfInterest")
                    && spatialFilter.getValueReference().contains("sams:shape")) {
                foiIDs =
                        new HashSet<String>(Configurator.getInstance().getFeatureQueryHandler()
                                .getFeatureIDs(spatialFilter, enviroCarDaoFactory));
            } else {
                throw new NoApplicableCodeException()
                        .withMessage("The requested valueReference for spatial filters is not supported by this server!");
            }
        }
        // feature of interest
        if (CollectionHelper.isNotEmpty(featureIdentifier)) {
            if (foiIDs == null) {
                foiIDs = new HashSet<String>(featureIdentifier);
            } else {
                Set<String> tempFoiIDs = new HashSet<String>();
                for (String foiID : featureIdentifier) {
                    if (foiIDs.contains(foiID)) {
                        tempFoiIDs.add(foiID);
                    }
                }
                foiIDs = tempFoiIDs;
            }
        }
        return foiIDs;
    }

    private org.envirocar.server.core.TemporalFilter toEcTemporalFilter(List<TemporalFilter> temporalFilters)
            throws OwsExceptionReport {
        if (temporalFilters.size() != 1) {
            throw new NotYetSupportedException(SosConstants.Operations.GetObservation.name(), String.format(
                    "with more than one '%s'", Sos2Constants.GetObservationParams.temporalFilter.name()));
        }
        TemporalFilter temporalFilter = temporalFilters.iterator().next();
        checkValueReference(temporalFilter.getValueReference());
        if (temporalFilter.getTime() instanceof TimeInstant) {
            TimeInstant time = (TimeInstant) temporalFilter.getTime();
            if (time.isSetValue()) {
                return new org.envirocar.server.core.TemporalFilter(toEcOperator(temporalFilter.getOperator()),
                        time.getValue());
            } else {
                throw new NotYetSupportedException(Sos2Constants.GetObservationParams.temporalFilter.name(),
                        "without time");
            }
        } else if (temporalFilter.getTime() instanceof TimePeriod) {
            TimePeriod time = (TimePeriod) temporalFilter.getTime();
            if (time.isSetStart() && time.isSetEnd()) {
                return new org.envirocar.server.core.TemporalFilter(toEcOperator(temporalFilter.getOperator()),
                        time.getStart(), time.getEnd());
            } else {
                throw new NotYetSupportedException(Sos2Constants.GetObservationParams.temporalFilter.name(),
                        "without time");
            }
        }
        throw new NotYetSupportedException(Sos2Constants.GetObservationParams.temporalFilter.name(), String.format(
                "with time type '%s'", temporalFilter.getTime().getClass().getCanonicalName()));
    }

    private void checkValueReference(String valueReference) throws OwsExceptionReport {
        if (!validTemporalFilterValueReferences.contains(valueReference)) {
            throw new NotYetSupportedException(Sos2Constants.GetObservationParams.temporalFilter.name(),
                    String.format(" with valueReference '%s'", valueReference));
        }
    }

    private TemporalFilterOperator toEcOperator(TimeOperator operator) throws OwsExceptionReport {
        switch (operator) {
        case TM_After:
            return TemporalFilterOperator.after;
        case TM_Before:
            return TemporalFilterOperator.before;
        case TM_Begins:
            return TemporalFilterOperator.begins;
        case TM_BegunBy:
            return TemporalFilterOperator.begunBy;
        case TM_Contains:
            return TemporalFilterOperator.contains;
        case TM_During:
            return TemporalFilterOperator.during;
        case TM_EndedBy:
            return TemporalFilterOperator.endedBy;
        case TM_Ends:
            return TemporalFilterOperator.ends;
        case TM_Equals:
            return TemporalFilterOperator.equals;
        case TM_Meets:
            return TemporalFilterOperator.meets;
        case TM_MetBy:
            return TemporalFilterOperator.metBy;
        case TM_OverlappedBy:
            return TemporalFilterOperator.overlapped;
        case TM_Overlaps:
            return TemporalFilterOperator.overlaps;
        default:
            throw new NotYetSupportedException(Sos2Constants.GetObservationParams.temporalFilter.name(),
                    operator.name());
        }
    }

    private List<OmObservation> toObservations(Measurements measurements, GetObservationRequest request,
            EnviroCarDaoFactory enviroCarDaoFactory) throws OwsExceptionReport, ConverterException {
        return new OmObservationMeasurementsCreator(measurements, request.getVersion(), request.getResultModel(),
                request.getObservedProperties(), enviroCarDaoFactory).create();
    }
}
