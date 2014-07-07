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
package org.n52.sos.ds.envirocar.util;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Sensor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.n52.sos.cache.ContentCache;
import org.n52.sos.ds.FeatureQueryHandler;
import org.n52.sos.ds.envirocar.EnviroCarDaoFactory;
import org.n52.sos.exception.CodedException;
import org.n52.sos.ogc.gml.AbstractFeature;
import org.n52.sos.ogc.gml.CodeWithAuthority;
import org.n52.sos.ogc.gml.ReferenceType;
import org.n52.sos.ogc.gml.time.Time;
import org.n52.sos.ogc.gml.time.TimeInstant;
import org.n52.sos.ogc.om.AbstractPhenomenon;
import org.n52.sos.ogc.om.NamedValue;
import org.n52.sos.ogc.om.OmConstants;
import org.n52.sos.ogc.om.OmObservableProperty;
import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.om.OmObservationConstellation;
import org.n52.sos.ogc.om.SingleObservationValue;
import org.n52.sos.ogc.om.values.GeometryValue;
import org.n52.sos.ogc.om.values.QuantityValue;
import org.n52.sos.ogc.om.values.Value;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sensorML.SensorMLConstants;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.ogc.sos.SosProcedureDescription;
import org.n52.sos.ogc.sos.SosProcedureDescriptionUnknowType;
import org.n52.sos.service.Configurator;
import org.n52.sos.service.ServiceConfiguration;
import org.n52.sos.service.profile.Profile;
import org.n52.sos.util.CollectionHelper;
import org.n52.sos.util.GeometryHandler;
import org.n52.sos.util.SosHelper;
import org.n52.sos.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;

public class OmObservationMeasurementsCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(OmObservationMeasurementsCreator.class);

    private final String version;

    private final EnviroCarDaoFactory daoFac;

    private final Measurements measurements;

    private final String resultModel;

    private final Map<String, AbstractFeature> features = Maps.newHashMap();

    private final Map<String, AbstractPhenomenon> observedProperties = Maps.newHashMap();

    private final Map<String, SosProcedureDescription> procedures = Maps.newHashMap();

    private final Set<OmObservationConstellation> observationConstellations = Sets.newHashSet();

    private final EnviroCarProcedureDescriptionGenerator procedureConverter;

    private final FeatureQueryHandler featureQueryHandler;

    private final boolean encodeProcedureInObservation;

    private List<OmObservation> observationCollection;

    private List<String> requestedObservableProperties;

    public OmObservationMeasurementsCreator(Measurements measurements, String version, String resultModel,
            List<String> requestedObservableProperties, EnviroCarDaoFactory daoFac) {
        this.version = version;
        this.daoFac = daoFac;
        this.resultModel = resultModel;
        this.measurements = measurements;
        this.procedureConverter = new EnviroCarProcedureDescriptionGenerator();
        this.requestedObservableProperties = requestedObservableProperties;
        this.featureQueryHandler = getFeatureQueryHandler();
        this.encodeProcedureInObservation = getActiveProfile().isEncodeProcedureInObservation();
    }

    public List<OmObservation> create() throws OwsExceptionReport {
        if (getMeasurements() == null) {
            return Collections.emptyList();
        } else if (this.observationCollection == null) {
            this.observationCollection = Lists.newLinkedList();
            // now iterate over resultset and create Measurement for each row
            for (Measurement m : getMeasurements()) {
                // check remaining heap size and throw exception if minimum is
                // reached
                SosHelper.checkFreeMemory();

                String procedureId = createProcedure(m);
                String featureId = createFeatureOfInterest(m);

                // TODO: add offering ids to response if needed later.
                // String offeringID =
                // hoc.getOffering().getIdentifier();
                // String mimeType = SosConstants.PARAMETER_NOT_SET;
                for (MeasurementValue mv : m.getValues()) {
                    if (checkPhenomenon(requestedObservableProperties, mv.getPhenomenon().getName())) {
                        String phenomenonId = createPhenomenon(mv);
                        createValue(m, mv, phenomenonId, procedureId, featureId);
                    }
                }
            }
        }
        return this.observationCollection;
    }

    private boolean checkPhenomenon(List<String> reqObsProp, String phenId) {
        if (CollectionHelper.isEmpty(reqObsProp)) {
            return true;
        }
        return reqObsProp.contains(phenId);
    }

    private Measurements getMeasurements() {
        return measurements;
    }

    private String getResultModel() {
        return resultModel;
    }

    private SosProcedureDescription getProcedure(String procedureId) {
        return procedures.get(procedureId);
    }

    private AbstractPhenomenon getObservedProperty(String phenomenonId) {
        return observedProperties.get(phenomenonId);
    }

    private AbstractFeature getFeature(String featureId) {
        return features.get(featureId);
    }

    private void checkOrSetObservablePropertyUnit(AbstractPhenomenon phen, String unit) {
        if (phen instanceof OmObservableProperty) {
            final OmObservableProperty obsProp = (OmObservableProperty) phen;
            if (obsProp.getUnit() == null && unit != null) {
                obsProp.setUnit(unit);
            }
        }
    }

    /**
     * Get observation value from all value tables for an Observation object
     * 
     * @param hObservation
     *            Observation object
     * 
     * @return Observation value
     * 
     * @throws OwsExceptionReport
     * @throws CodedException
     */
    private Value<?> getValueFromObservation(final MeasurementValue mv) throws CodedException, OwsExceptionReport {

        if (mv.getValue() instanceof BigDecimal) {
            return new QuantityValue((BigDecimal) mv.getValue());
        } else if (mv.getValue() instanceof Double) {
            return new QuantityValue(new BigDecimal((Double) mv.getValue()));
        } else if (mv.getValue() instanceof Boolean) {
            return new org.n52.sos.ogc.om.values.BooleanValue(Boolean.valueOf((Boolean) mv.getValue()));
        } else if (mv.getValue() instanceof Integer) {
            return new org.n52.sos.ogc.om.values.CountValue(Integer.valueOf((Integer) mv.getValue()));
        } else if (mv.getValue() instanceof String) {
            return new org.n52.sos.ogc.om.values.TextValue((String) (mv.getValue()));
        } else if (mv.getValue() instanceof Geometry) {
            return new org.n52.sos.ogc.om.values.GeometryValue((Geometry) mv.getValue());
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private OmObservation createNewObservation(OmObservationConstellation oc, Measurement m, Value<?> value) {
        final OmObservation o = new OmObservation();
        o.setObservationID(m.getIdentifier());
        o.setIdentifier(new CodeWithAuthority(m.getIdentifier()));
        o.setNoDataValue(getActiveProfile().getResponseNoDataPlaceholder());
        o.setTokenSeparator(getTokenSeparator());
        o.setTupleSeparator(getTupleSeparator());
        o.setObservationConstellation(oc);
        o.setResultTime(new TimeInstant(new DateTime(m.getModificationTime(), DateTimeZone.UTC)));
        o.setValue(new SingleObservationValue(getPhenomenonTime(m), value));
        return o;
    }

    private Time getPhenomenonTime(final Measurement m) {
        return new TimeInstant(new DateTime(m.getTime(), DateTimeZone.UTC));
    }

    private String createPhenomenon(final MeasurementValue mv) {
        LOGGER.trace("Creating Phenomenon...");
        final String phenID = mv.getPhenomenon().getName();
        if (!observedProperties.containsKey(phenID)) {
            observedProperties.put(phenID, new OmObservableProperty(phenID, null, mv.getPhenomenon().getUnit(), null));
        }
        LOGGER.trace("Creating Phenomenon done.");
        return phenID;
    }

    private String createProcedure(final Measurement m) throws OwsExceptionReport {
        // TODO sfp full description
        LOGGER.trace("Creating Procedure...");
        final String procedureId = m.getSensor().getIdentifier();
        if (!procedures.containsKey(procedureId)) {
            final Sensor sensor = m.getSensor();
            final String pdf = SensorMLConstants.NS_SML;
            final SosProcedureDescription procedure;
            if (encodeProcedureInObservation) {
                procedure = procedureConverter.create(sensor, getEnviroCarDaoFactory());
            } else {
                procedure = new SosProcedureDescriptionUnknowType(procedureId, pdf, null);
            }
            procedures.put(procedureId, procedure);
        }
        LOGGER.trace("Creating Procedure done.");
        return procedureId;
    }

    private String createFeatureOfInterest(final Measurement m) throws OwsExceptionReport {
        LOGGER.trace("Creating Feature...");
        final String foiID = m.getTrack().getIdentifier();
        if (!features.containsKey(foiID)) {
            final AbstractFeature featureByID =
                    featureQueryHandler.getFeatureByID(foiID, getEnviroCarDaoFactory(), getVersion(), -1);
            features.put(foiID, featureByID);
        }
        LOGGER.trace("Creating Feature done.");
        return foiID;
    }

    private void createValue(Measurement m, MeasurementValue mv, String phenomenonId, String procedureId,
            String featureId) throws OwsExceptionReport {
        LOGGER.trace("Creating Value...");
        final Value<?> value = getValueFromObservation(mv);
        if (value != null) {
            if (mv.getPhenomenon().getUnit() != null) {
                value.setUnit(mv.getPhenomenon().getUnit());
            }
            checkOrSetObservablePropertyUnit(getObservedProperty(phenomenonId), value.getUnit());
            OmObservationConstellation obsConst =
                    createObservationConstellation(m, procedureId, phenomenonId, featureId);
            final OmObservation sosObservation = createNewObservation(obsConst, m, value);
            // add SpatialFilteringProfile
            sosObservation.addParameter(createSpatialFilteringProfileParameter(m));
            observationCollection.add(sosObservation);
            // TODO check for ScrollableResult vs
            // setFetchSize/setMaxResult
            // + setFirstResult
        }
        LOGGER.trace("Creating Value done.");
    }

    private NamedValue<?> createSpatialFilteringProfileParameter(Measurement m) throws OwsExceptionReport {
        final NamedValue<Geometry> namedValue = new NamedValue<Geometry>();
        final ReferenceType referenceType = new ReferenceType(Sos2Constants.HREF_PARAMETER_SPATIAL_FILTERING_PROFILE);
        namedValue.setName(referenceType);
        namedValue.setValue(new GeometryValue(GeometryHandler.getInstance().switchCoordinateAxisOrderIfNeeded(
                m.getGeometry())));
        return namedValue;
    }

    private OmObservationConstellation createObservationConstellation(Measurement m, String procedureId,
            String phenomenonId, String featureId) {
        OmObservationConstellation obsConst =
                new OmObservationConstellation(getProcedure(procedureId), getObservedProperty(phenomenonId),
                        getFeature(featureId));

        /* sfp the offerings to find the templates */
        if (obsConst.getOfferings() == null) {
            final Set<String> offerings =
                    Sets.newHashSet(getCache().getOfferingsForObservableProperty(
                            obsConst.getObservableProperty().getIdentifier()));
            offerings.retainAll(getCache().getOfferingsForProcedure(obsConst.getProcedure().getIdentifier()));
            obsConst.setOfferings(offerings);
        }
        if (!observationConstellations.contains(obsConst)) {
            if (StringHelper.isNotEmpty(getResultModel())) {
                obsConst.setObservationType(getResultModel());
            } else {
                obsConst.setObservationType(OmConstants.OBS_TYPE_MEASUREMENT);
            }
            observationConstellations.add(obsConst);
        }
        return obsConst;
    }

    protected FeatureQueryHandler getFeatureQueryHandler() {
        return Configurator.getInstance().getFeatureQueryHandler();
    }

    protected Profile getActiveProfile() {
        return Configurator.getInstance().getProfileHandler().getActiveProfile();
    }

    protected String getTokenSeparator() {
        return ServiceConfiguration.getInstance().getTokenSeparator();
    }

    protected String getTupleSeparator() {
        return ServiceConfiguration.getInstance().getTupleSeparator();
    }

    protected String getNoDataValue() {
        return getActiveProfile().getResponseNoDataPlaceholder();
    }

    protected ContentCache getCache() {
        return Configurator.getInstance().getCache();
    }

    protected String getVersion() {
        return version;
    }

    protected EnviroCarDaoFactory getEnviroCarDaoFactory() {
        return daoFac;
    }

}
