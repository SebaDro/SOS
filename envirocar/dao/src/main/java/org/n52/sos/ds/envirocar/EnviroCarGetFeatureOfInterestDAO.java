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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.envirocar.server.core.filter.MeasurementFeatureFilter;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.mongo.dao.MongoMeasurementDao;
import org.n52.sos.ds.AbstractGetFeatureOfInterestDAO;
import org.n52.sos.ds.EnviroCarConstants;
import org.n52.sos.exception.ows.MissingParameterValueException;
import org.n52.sos.exception.ows.NoApplicableCodeException;
import org.n52.sos.ogc.filter.FilterConstants;
import org.n52.sos.ogc.filter.SpatialFilter;
import org.n52.sos.ogc.om.features.FeatureCollection;
import org.n52.sos.ogc.ows.CompositeOwsException;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.Sos1Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.request.GetFeatureOfInterestRequest;
import org.n52.sos.response.GetFeatureOfInterestResponse;
import org.n52.sos.util.GeometryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnviroCarGetFeatureOfInterestDAO extends AbstractGetFeatureOfInterestDAO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EnviroCarGetFeatureOfInterestDAO.class);

    private final EnviroCarDaoFactoryHolder factoryHolder = new EnviroCarDaoFactoryHolder();

    public EnviroCarGetFeatureOfInterestDAO() {
        super(SosConstants.SOS);
    }

    @Override
    public String getDatasourceDaoIdentifier() {
        return EnviroCarConstants.ENVIROCAR_DATASOURCE_DAO_IDENTIFIER;
    }

    @Override
    public GetFeatureOfInterestResponse getFeatureOfInterest(GetFeatureOfInterestRequest request)
            throws OwsExceptionReport {
        FeatureCollection featureCollection;

        if (isSos100(request)) {
            // sos 1.0.0 either or
            if (isMixedFeatureIdentifierAndSpatialFilters(request)) {
                throw new NoApplicableCodeException()
                        .withMessage("Only one out of featureofinterestid or location possible.");
            } else if (isFeatureIdentifierRequest(request) || isSpatialFilterRequest(request)) {
                featureCollection = getFeatures(request, factoryHolder.getEnviroCarDaoFactory());
            } else {
                throw new CompositeOwsException(new MissingParameterValueException(
                        Sos1Constants.GetFeatureOfInterestParams.featureOfInterestID),
                        new MissingParameterValueException(Sos1Constants.GetFeatureOfInterestParams.location));
            }
        } else // SOS 2.0
        {
            featureCollection = getFeatures(request, factoryHolder.getEnviroCarDaoFactory());
            /*
             * Now, we return the list of returned features and not a
             * complex encoded relatedFeature See
             * AbstractGetFeatureOfInterestDAO:100-195 Don't forget to
             * activate in MiscSettings the relatedFeature setting
             * featureCollection = processRelatedFeatures(
             * request.getFeatureIdentifiers(), featureCollection,
             * ServiceConfiguration
             * .getInstance().getRelatedSamplingFeatureRoleForChildFeatures
             * ());
             */
        }
        final GetFeatureOfInterestResponse response = new GetFeatureOfInterestResponse();
        response.setService(request.getService());
        response.setVersion(request.getVersion());
        response.setAbstractFeature(featureCollection);
        return response;
    }

    private FeatureCollection getFeatures(GetFeatureOfInterestRequest request, EnviroCarDaoFactory enviroCarDaoFactory) throws OwsExceptionReport {
        final Set<String> foiIDs = new HashSet<String>(queryFeatureIdentifiersForParameter(request, enviroCarDaoFactory));
        return new FeatureCollection(getConfigurator().getFeatureQueryHandler().getFeatures(
                new ArrayList<String>(foiIDs), null, enviroCarDaoFactory, request.getVersion(), -1));
    }
    
    /**
     * Get featureOfInterest identifiers for requested parameters
     * 
     * @param req
     *            GetFeatureOfInterest request
     * @param enviroCarDaoFactory 
     * @return Resulting FeatureOfInterest identifiers list
     * @throws OwsExceptionReport
     *             If an error occurs during processing
     */
    private Collection<String> queryFeatureIdentifiersForParameter(final GetFeatureOfInterestRequest req, EnviroCarDaoFactory enviroCarDaoFactory) throws OwsExceptionReport {
        if (req.hasNoParameter() && !req.isSetSpatialFilters()) {
            return enviroCarDaoFactory.getTrackDAO().getIdentifier();
        }
        if (req.containsOnlyFeatureParameter() && req.isSetFeatureOfInterestIdentifiers()) {
            return req.getFeatureIdentifiers();
        }
        return queryFeatureIdentifierOfParameter(req, enviroCarDaoFactory);
    }

    private Collection<String> queryFeatureIdentifierOfParameter(GetFeatureOfInterestRequest req,
            EnviroCarDaoFactory enviroCarDaoFactory) throws OwsExceptionReport {
        MeasurementFeatureFilter measurementFilter = new MeasurementFeatureFilter();
        if (req.isSetObservableProperties()) {
           measurementFilter.setPhenomenonIds(req.getObservedProperties()); 
        }
        if (req.isSetProcedures()) {
            measurementFilter.setSensorIds(req.getProcedures());
        }
        if (req.isSetSpatialFilters()) {
            for (SpatialFilter spatialFilter : req.getSpatialFilters()) {
                if (spatialFilter.isSetOperator() && FilterConstants.SpatialOperator.BBOX.equals(spatialFilter.getOperator())) {
                    measurementFilter.addGeometry(GeometryHandler.getInstance().switchCoordinateAxisOrderIfNeeded(spatialFilter.getGeometry()));
                }
            }
        } 
        return ((MongoMeasurementDao)enviroCarDaoFactory.getMeasurementDAO()).getTrackIds(measurementFilter);
    }
    
}
