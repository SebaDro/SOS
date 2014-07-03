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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.mongo.entity.MongoSensor;
import org.n52.sos.cache.ContentCache;
import org.n52.sos.ds.envirocar.EnviroCarDaoFactory;
import org.n52.sos.ogc.OGCConstants;
import org.n52.sos.ogc.gml.CodeType;
import org.n52.sos.ogc.gml.time.TimePeriod;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.ows.SosServiceProvider;
import org.n52.sos.ogc.sensorML.AbstractProcess;
import org.n52.sos.ogc.sensorML.AbstractSensorML;
import org.n52.sos.ogc.sensorML.ProcessMethod;
import org.n52.sos.ogc.sensorML.ProcessModel;
import org.n52.sos.ogc.sensorML.RulesDefinition;
import org.n52.sos.ogc.sensorML.SensorML;
import org.n52.sos.ogc.sensorML.SensorMLConstants;
import org.n52.sos.ogc.sensorML.SmlResponsibleParty;
import org.n52.sos.ogc.sensorML.elements.SmlCapabilities;
import org.n52.sos.ogc.sensorML.elements.SmlClassifier;
import org.n52.sos.ogc.sensorML.elements.SmlIdentifier;
import org.n52.sos.ogc.sensorML.elements.SmlIo;
import org.n52.sos.ogc.sos.SosEnvelope;
import org.n52.sos.ogc.sos.SosOffering;
import org.n52.sos.ogc.sos.SosProcedureDescription;
import org.n52.sos.ogc.swe.SweDataRecord;
import org.n52.sos.ogc.swe.SweEnvelope;
import org.n52.sos.ogc.swe.SweField;
import org.n52.sos.ogc.swe.simpleType.SweObservableProperty;
import org.n52.sos.ogc.swe.simpleType.SweQuantity;
import org.n52.sos.service.Configurator;
import org.n52.sos.service.ProcedureDescriptionSettings;
import org.n52.sos.service.ServiceConfiguration;
import org.n52.sos.util.Constants;
import org.n52.sos.util.JavaHelper;
import org.n52.sos.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class EnviroCarProcedureDescriptionGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnviroCarProcedureDescriptionGenerator.class);

    private static final Joiner COMMA_JOINER = Joiner.on(",");

    private Sensor sensor;

    private EnviroCarDaoFactory enviroCarDaoFactory;

    public EnviroCarProcedureDescriptionGenerator(Sensor sensor, EnviroCarDaoFactory enviroCarDaoFactory) {
        this.sensor = sensor;
        this.enviroCarDaoFactory = enviroCarDaoFactory;
    }

    public SosProcedureDescription create() throws OwsExceptionReport {
        final SensorML sml = new SensorML();
        // 2.2 if no position is available -> processModel -> own class
        sml.addMember(createSmlProcessModel(sensor, enviroCarDaoFactory));
        return sml;
    }

    /**
     * Create a SensorML ProcessModel from Hibernate procedure entity
     * 
     * @param procedure
     *            Hibernate procedure entity
     * 
     * @return SensorML ProcessModel
     * 
     * @throws OwsExceptionReport
     *             If an error occurs
     */
    private ProcessModel createSmlProcessModel(Sensor sensor, EnviroCarDaoFactory enviroCarDaoFactory)
            throws OwsExceptionReport {
        final ProcessModel processModel = new ProcessModel();
        setCommonValues(sensor, processModel, enviroCarDaoFactory);
        Set<String> observableProperties = getObservablePropertiesForProcedure(sensor.getIdentifier());
        processModel.setMethod(createMethod(sensor, observableProperties));
        processModel.setNames(createNames(sensor));
        return processModel;
    }

    /**
     * Create a SensorML ProcessMethod for ProcessModel
     * 
     * @param procedure
     *            Hibernate procedure entity
     * @param observableProperties
     *            Properties observed by the procedure
     * 
     * @return SenbsorML ProcessModel
     */
    private ProcessMethod createMethod(Sensor sensor, Set<String> observableProperties) {
        return new ProcessMethod(createRulesDefinition(sensor, observableProperties));
    }

    /**
     * Create a names collection for procedure description
     * 
     * @param procedure
     *            Hibernate procedure entity
     * 
     * @return Collection with names
     */
    private List<CodeType> createNames(Sensor sensor) {
        return Lists.newArrayList(new CodeType(sensor.getIdentifier()));
    }

    /**
     * Create the rules definition for ProcessMethod
     * 
     * @param procedure
     *            Hibernate procedure entity
     * @param observableProperties
     *            Properties observed by the procedure
     * 
     * @return SensorML RulesDefinition
     */
    private RulesDefinition createRulesDefinition(Sensor sensor, Set<String> observableProperties) {
        RulesDefinition rD = new RulesDefinition();
        String template = procedureSettings().getProcessMethodRulesDefinitionDescriptionTemplate();
        String description = String.format(template, sensor.getIdentifier(), COMMA_JOINER.join(observableProperties));
        rD.setDescription(description);
        return rD;
    }

    /**
     * Set common values to procedure description
     * 
     * @param procedure
     *            Hibernate procedure entity
     * @param abstractSensorML
     *            SensorML process
     * 
     * @throws OwsExceptionReport
     *             If an error occurs
     */
    private void setCommonValues(Sensor sensor, AbstractProcess abstractSensorML,
            EnviroCarDaoFactory enviroCarDaoFactory) throws OwsExceptionReport {
        String identifier = sensor.getIdentifier();
        Set<String> observableProperties = getObservablePropertiesForProcedure(identifier);

        // 1 set description
        abstractSensorML.setDescriptions(createDescriptions(sensor, observableProperties));

        // 2 identifier
        abstractSensorML.setIdentifier(identifier);

        // 3 set identification
        abstractSensorML.setIdentifications(createIdentifications(identifier));

        // 7 set inputs/outputs --> observableProperties
        if (getServiceConfig().isAddOutputsToSensorML()
                && !"hydrology".equalsIgnoreCase(Configurator.getInstance().getProfileHandler().getActiveProfile()
                        .getIdentifier())) {
            abstractSensorML.setInputs(createInputs(observableProperties));
            abstractSensorML.setOutputs(createOutputs(sensor, observableProperties, enviroCarDaoFactory));
        }
        // bbox
        addCapabilities(abstractSensorML, getCache().getEnvelopeForOffering(identifier));
        // features
        abstractSensorML.addFeaturesOfInterest(getCache().getFeaturesOfInterestForOffering(identifier));
        // offering
        abstractSensorML.addOffering(new SosOffering(identifier, getCache().getNameForOffering(identifier)));
        // intended application classifier
        addIntendedApplicationClassifier(abstractSensorML);
        // procedure type classifier
        addProcedureTypeClassification(abstractSensorML);
        // valid time
        abstractSensorML.setValidTime(new TimePeriod(getCache().getMinPhenomenonTimeForProcedure(identifier),
                getCache().getMaxPhenomenonTimeForProcedure(identifier)));
        // keyword
        addKeywords(abstractSensorML);
        // contact
        abstractSensorML.addContact(createContactFromServiceContact());
    }

    private List<SmlIo<?>> createInputs(Set<String> observableProperties) throws OwsExceptionReport {
        final List<SmlIo<?>> inputs = Lists.newArrayListWithExpectedSize(observableProperties.size());
        int i = 1;
        for (String observableProperty : observableProperties) {
            inputs.add(new SmlIo<String>().setIoName("input#" + i++).setIoValue(
                    new SweObservableProperty().setDefinition(observableProperty)));
        }
        return inputs;
    }

    /**
     * Create SensorML output list from observableProperties
     * 
     * @param procedure
     *            Hibernate procedure entity
     * @param observableProperties
     *            Properties observed by the procedure
     * 
     * @return Output list
     * 
     * @throws OwsExceptionReport
     *             If an error occurs
     */
    private List<SmlIo<?>> createOutputs(Sensor sensor, Set<String> observableProperties,
            EnviroCarDaoFactory enviroCarDaoFactory) throws OwsExceptionReport {
        final List<SmlIo<?>> outputs = Lists.newArrayListWithExpectedSize(observableProperties.size());
        int i = 1;
        for (String observableProperty : observableProperties) {
            final SmlIo<?> output;
            output = createOutputFromPhenomenon(observableProperty, enviroCarDaoFactory);
            if (output != null) {
                output.setIoName("output#" + i++);
                outputs.add(output);
            }
        }
        return outputs;
    }

    private SmlIo<?> createOutputFromPhenomenon(String observableProperty, EnviroCarDaoFactory enviroCarDaoFactory) {
        Phenomenon phenomenon = enviroCarDaoFactory.getPhenomenonDAO().getByName(observableProperty);
        final SweQuantity quantity = new SweQuantity();
        quantity.setDefinition(observableProperty);
        if (StringHelper.isNotEmpty(phenomenon.getUnit())) {
            quantity.setUom(phenomenon.getUnit());
        }
        return new SmlIo<Double>(quantity);
    }

    private List<String> createDescriptions(Sensor sensor, Set<String> observableProperties) {
        String template = procedureSettings().getDescriptionTemplate();
        String identifier = sensor.getIdentifier();
        String obsProps = COMMA_JOINER.join(observableProperties);
        String type = "procedure";
        return Lists.newArrayList(String.format(template, type, identifier, obsProps));
    }

    private List<SmlIdentifier> createIdentifications(final String identifier) {
        ArrayList<SmlIdentifier> list =
                Lists.newArrayList(createIdentifier(identifier), createLongName(), createShortName());
        list.addAll(createFromProperties());
        return list;
    }

    private SmlIdentifier createIdentifier(final String identifier) {
        return new SmlIdentifier(OGCConstants.URN_UNIQUE_IDENTIFIER_END, OGCConstants.URN_UNIQUE_IDENTIFIER,
                identifier);
    }

    private SmlIdentifier createLongName() {
        StringBuilder builder = new StringBuilder();
        if (sensor.hasProperties()) {
            Map<String, Object> properties = sensor.getProperties();
            if (properties.containsKey(MongoSensor.PROPERTY_MODEL)
                    && properties.containsKey(MongoSensor.PROPERTY_MANUFACTURER)) {
                if (properties.containsKey(MongoSensor.PROPERTY_CONSTRUCTION_YEAR)) {
                    builder.append(properties.get(MongoSensor.PROPERTY_CONSTRUCTION_YEAR))
                            .append(Constants.BLANK_CHAR);
                }
                builder.append(properties.get(MongoSensor.PROPERTY_MANUFACTURER)).append(Constants.BLANK_CHAR);
                builder.append(properties.get(MongoSensor.PROPERTY_MODEL));
            } else {
                builder.append(sensor.getIdentifier());
            }
        } else {
            builder.append(sensor.getIdentifier());
        }
        return new SmlIdentifier(SensorMLConstants.ELEMENT_NAME_LONG_NAME, procedureSettings()
                .getIdentifierLongNameDefinition(), builder.toString());
    }

    private SmlIdentifier createShortName() {
        StringBuilder builder = new StringBuilder();
        if (sensor.hasProperties()) {
            Map<String, Object> properties = sensor.getProperties();
            if (properties.containsKey(MongoSensor.PROPERTY_MODEL)
                    && properties.containsKey(MongoSensor.PROPERTY_MANUFACTURER)) {
                builder.append(properties.get(MongoSensor.PROPERTY_MODEL));
            } else {
                builder.append(sensor.getIdentifier());
            }
        } else {
            builder.append(sensor.getIdentifier());
        }
        return new SmlIdentifier(SensorMLConstants.ELEMENT_NAME_SHORT_NAME, procedureSettings()
                .getIdentifierShortNameDefinition(), builder.toString());
    }

    private List<SmlIdentifier> createFromProperties() {
        List<SmlIdentifier> identifier = Lists.newArrayList();
        if (sensor.hasProperties()) {
            Map<String, Object> properties = sensor.getProperties();
            for (String key : properties.keySet()) {
                identifier.add(new SmlIdentifier(key, key, JavaHelper.asString(properties.get(key))));
            }
        }
        return identifier;
    }

    private void addCapabilities(AbstractSensorML sensorML, final SosEnvelope bbox) {
        if (bbox != null && bbox.isSetEnvelope()) {
            // add merged bbox to capabilities as swe:envelope
            final SweEnvelope envelope = new SweEnvelope(bbox, procedureSettings().getLatLongUom());
            envelope.setDefinition(SensorMLConstants.OBSERVED_BBOX_DEFINITION_URN);
            final SweField field = new SweField(SensorMLConstants.ELEMENT_NAME_OBSERVED_BBOX, envelope);
            sensorML.addCapabilities(new SmlCapabilities().setName(SensorMLConstants.ELEMENT_NAME_OBSERVED_BBOX)
                    .setDataRecord(new SweDataRecord().addField(field)));
        }
    }

    private void addIntendedApplicationClassifier(AbstractSensorML description) {
        addClassifier(description, SmlClassifier.INTENDED_APPLICATION, procedureSettings()
                .getClassifierIntendedApplicationDefinition(), "enviroCar");
    }

    private void addProcedureTypeClassification(AbstractSensorML description) {
        addClassifier(description, SmlClassifier.PROCEDURE_TYPE, procedureSettings()
                .getClassifierProcedureTypeDefinition(), sensor.getType());
    }

    private void addClassifier(AbstractSensorML description, String name, String definition, String value) {
        if (!Strings.isNullOrEmpty(value)) {
            SmlClassifier classifier = new SmlClassifier(name, definition, null, value);
            description.addClassification(classifier);
        }
    }

    private void addKeywords(AbstractProcess abstractSensorML) {
        Set<String> keywords = Sets.newHashSet();
        keywords.add(sensor.getIdentifier());
        keywords.add(sensor.getType());
        keywords.addAll(getCache().getObservablePropertiesForProcedure(sensor.getIdentifier()));
        keywords.add("enviroCar");
        if (sensor.hasProperties()) {
            for (Object object : sensor.getProperties().values()) {
                keywords.add(JavaHelper.asString(object));
            }
        }
    }
    
    private SmlResponsibleParty createContactFromServiceContact() throws OwsExceptionReport {
        SosServiceProvider sp = Configurator.getInstance().getServiceProvider();

        SmlResponsibleParty rp = new SmlResponsibleParty();
        if (sp.hasIndividualName()) {
            rp.setIndividualName(sp.getIndividualName());
        }
        if (sp.hasName()) {
            rp.setOrganizationName(sp.getName());
        }
        if (sp.hasSite()) {
            rp.addOnlineResource(sp.getSite());
        }
        if (sp.hasPositionName()) {
            rp.setPositionName(sp.getPositionName());
        }
        if (sp.hasDeliveryPoint()) {
            rp.addDeliveryPoint(sp.getDeliveryPoint());
        }
        if (sp.hasPhone()) {
            rp.addPhoneVoice(sp.getPhone());
        }
        if (sp.hasCity()) {
            rp.setCity(sp.getCity());
        }
        if (sp.hasCountry()) {
            rp.setCountry(sp.getCountry());
        }
        if (sp.hasPostalCode()) {
            rp.setPostalCode(sp.getPostalCode());
        }
        if (sp.hasMailAddress()) {
            rp.setEmail(sp.getMailAddress());
        }
        return rp;
    }

    ServiceConfiguration getServiceConfig() {
        return ServiceConfiguration.getInstance();
    }

    Set<String> getObservablePropertiesForProcedure(String identifier) {
        Set<String> props = getCache().getObservablePropertiesForProcedure(identifier);
        return props;
    }

    ProcedureDescriptionSettings procedureSettings() {
        return ProcedureDescriptionSettings.getInstance();
    }

    protected ContentCache getCache() {
        return Configurator.getInstance().getCache();
    }

}
