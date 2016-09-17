/**
 * Copyright (C) 2012-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.mqtt.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.n52.sos.encode.SensorMLEncoderv20;
import org.n52.sos.mqtt.api.AdsbMessage;
import org.n52.sos.ogc.OGCConstants;
import org.n52.sos.ogc.om.OmConstants;
import org.n52.sos.ogc.om.features.SfConstants;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sensorML.elements.SmlCapabilities;
import org.n52.sos.ogc.sensorML.elements.SmlCapability;
import org.n52.sos.ogc.sensorML.elements.SmlClassifier;
import org.n52.sos.ogc.sensorML.elements.SmlIdentifier;
import org.n52.sos.ogc.sensorML.elements.SmlIo;
import org.n52.sos.ogc.sensorML.v20.PhysicalSystem;
import org.n52.sos.ogc.sos.SosInsertionMetadata;
import org.n52.sos.ogc.sos.SosOffering;
import org.n52.sos.ogc.swe.SweField;
import org.n52.sos.ogc.swe.simpleType.SweBoolean;
import org.n52.sos.ogc.swe.simpleType.SweObservableProperty;
import org.n52.sos.ogc.swe.simpleType.SweQuantity;
import org.n52.sos.ogc.swe.simpleType.SweText;
import org.n52.sos.request.InsertSensorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class AdsbToInsertSensor {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AdsbToInsertSensor.class);
    
    public InsertSensorRequest convert(AdsbMessage message) throws OwsExceptionReport { 
        return prepareSmlInsertSensorRequest(message);
    }
    
    private InsertSensorRequest prepareSmlInsertSensorRequest(AdsbMessage message) {
        final InsertSensorRequest insertSensorRequest = new InsertSensorRequest();
        final PhysicalSystem system = new PhysicalSystem();
        
        final String procedureId = message.getHex();
        final SosOffering sosOffering = new SosOffering(procedureId);
        system
                .setInputs(createInputs())
                .setOutputs(createOutputs())
                .setIdentifications(createIdentificationList(procedureId))
                .setClassifications(createClassificationList())
                .addCapabilities(createCapabilities(sosOffering))
                .addCapabilities(createMobileInsitu())
//                .addContact(createContact(schemaDescription.getDataset())) // TODO
                // ... // TODO
                .setIdentifier(procedureId)
                ;
        
        system.setSensorDescriptionXmlString(encodeToXml(system));
        
        insertSensorRequest.setAssignedOfferings(Collections.singletonList(sosOffering));
        insertSensorRequest.setAssignedProcedureIdentifier(procedureId);
        insertSensorRequest.setProcedureDescription(system);
        insertSensorRequest.setMetadata(createInsertSensorMetadata());
        return insertSensorRequest;
    }

    private static String encodeToXml(final PhysicalSystem system) {
        try {
            return new SensorMLEncoderv20().encode(system).xmlText();
        } catch (OwsExceptionReport ex) {
            LOGGER.error("Could not encode SML to valid XML.", ex);
            return "";  // TODO empty but valid sml
        }
    }
    
    private List<SmlIo<?>> createInputs() {
        List<SmlIo<?>> inputs = Lists.newArrayList();
        inputs.add(createInput(AdsbMessage.ALTITUDE));
        inputs.add(createInput(AdsbMessage.SPEED));
        inputs.add(createInput(AdsbMessage.TRACK));
        return inputs;
    }
    
    private List<SmlIo<?>> createOutputs() {
        List<SmlIo<?>> outputs = Lists.newArrayList();
        outputs.add(createOutput(AdsbMessage.ALTITUDE, AdsbMessage.ALTITUDE_UNIT));
        outputs.add(createOutput(AdsbMessage.SPEED, AdsbMessage.SPEED_UNIT));
        outputs.add(createOutput(AdsbMessage.TRACK, AdsbMessage.TRACK_UNIT));
        return outputs;
    }
    
    private SmlIo<?> createInput(String phenomeon) {
        return new SmlIo<>(new SweObservableProperty()
                .setDefinition(phenomeon))
                .setIoName(phenomeon);
    }
    
    private SmlIo<?> createOutput(String phenomeon, String unit) {
        return new SmlIo<>(new SweQuantity()
                .setUom(unit)
                .setDefinition(phenomeon))
                .setIoName(phenomeon);
    }

    private List<SmlIdentifier> createIdentificationList(String procedure) {
        List<SmlIdentifier> idents = new ArrayList<>();
        idents.add(new SmlIdentifier(
                OGCConstants.UNIQUE_ID, 
                OGCConstants.URN_UNIQUE_IDENTIFIER,
                // TODO check feautre id vs name
                procedure));
        idents.add(new SmlIdentifier(
                "longName",
                "urn:ogc:def:identifier:OGC:1.0:longName",
                procedure));
        return idents;
    }
    
    private List<SmlClassifier> createClassificationList() {
        List<SmlClassifier> classifier = Lists.newArrayList();
        classifier.add(createClassification(AdsbMessage.ALTITUDE));
        classifier.add(createClassification(AdsbMessage.SPEED));
        classifier.add(createClassification(AdsbMessage.TRACK));
        return classifier;
    }

    private SmlClassifier createClassification(String phenomenon) {
        return new SmlClassifier(
                "phenomenon", 
                "urn:ogc:def:classifier:OGC:1.0:phenomenon",
                null, 
                phenomenon);
    }
    
    

    private List<SmlCapabilities> createCapabilities(SosOffering offering) {
        List<SmlCapabilities> capabilities = new ArrayList<>();
        capabilities.add(createOfferingCapabilities(offering));
        return capabilities;
    }
    
    private SmlCapabilities createMobileInsitu() {
        SmlCapabilities capabilities = new SmlCapabilities("metadata");
        
        SmlCapability insitu = new SmlCapability("insitu");
        insitu.setAbstractDataComponent(new SweBoolean().setValue(false).addName("insitu"));
        capabilities.addCapability(insitu);
        
        SmlCapability mobile = new SmlCapability("mobile");
        mobile.setAbstractDataComponent(new SweBoolean().setValue(true).addName("mobile"));
        capabilities.addCapability(mobile);
        
        return capabilities;
    }

    private SmlCapabilities createOfferingCapabilities(SosOffering offering) {
        SmlCapabilities capabilities = new SmlCapabilities("offerings");
        
        SmlCapability insitu = new SmlCapability("offeringID");
        insitu.setAbstractDataComponent(createTextField(offering.getIdentifier(), "offeringID", "urn:ogc:def:identifier:OGC:offeringID"));
        capabilities.addCapability(insitu);
        return capabilities;
    }

    private SweField createTextField(String name, String definition, String value) {
        return new SweField(name, new SweText().setValue(value).setDefinition(definition));
    }


    private SosInsertionMetadata createInsertSensorMetadata() {
        SosInsertionMetadata metadata = new SosInsertionMetadata();
        metadata.setFeatureOfInterestTypes(Collections.singleton(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE));
        metadata.setObservationTypes(Collections.singleton(OmConstants.OBS_TYPE_MEASUREMENT));
        return metadata;
    }

}
