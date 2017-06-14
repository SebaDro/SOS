/**
 * Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
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

import org.n52.sos.mqtt.api.AdsbMessage;
import org.n52.sos.ogc.OGCConstants;
import org.n52.sos.ogc.om.OmConstants;
import org.n52.sos.ogc.om.features.SfConstants;
import org.n52.sos.ogc.sensorML.elements.SmlCapabilities;
import org.n52.sos.ogc.sensorML.elements.SmlClassifier;
import org.n52.sos.ogc.sensorML.elements.SmlIdentifier;
import org.n52.sos.ogc.sensorML.elements.SmlIo;
import org.n52.sos.ogc.sos.SosInsertionMetadata;
import org.n52.sos.ogc.sos.SosOffering;
import org.n52.svalbard.inspire.omso.InspireOMSOConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class AdsbToInsertSensor extends AbstractMqttInsertSensorConverter<AdsbMessage> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AdsbToInsertSensor.class);
    
    @Override
    protected String getProcedure(AdsbMessage message) {
        return message.getHex();
    }

    @Override
    protected List<SmlIo<?>> createInputs() {
        List<SmlIo<?>> inputs = Lists.newArrayList();
        inputs.add(createInput(AdsbMessage.ALTITUDE));
        inputs.add(createInput(AdsbMessage.SPEED));
        inputs.add(createInput(AdsbMessage.TRACK));
        return inputs;
    }
    
    @Override
    protected List<SmlIo<?>> createOutputs() {
        List<SmlIo<?>> outputs = Lists.newArrayList();
        outputs.add(createOutput(AdsbMessage.ALTITUDE, AdsbMessage.ALTITUDE_UNIT));
        outputs.add(createOutput(AdsbMessage.SPEED, AdsbMessage.SPEED_UNIT));
        outputs.add(createOutput(AdsbMessage.TRACK, AdsbMessage.TRACK_UNIT));
        return outputs;
    }
    
    protected List<SmlIdentifier> createIdentificationList(String procedure, AdsbMessage message) {
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
        if (!Strings.isNullOrEmpty(message.getSquawk())) {
            idents.add(new SmlIdentifier(
                    "squawk",
                    "squawk",
                    message.getSquawk()));
        }
        return idents;
    }
    
    protected List<SmlClassifier> createClassificationList() {
        List<SmlClassifier> classifier = Lists.newArrayList();
        classifier.add(createClassification(AdsbMessage.ALTITUDE));
        classifier.add(createClassification(AdsbMessage.SPEED));
        classifier.add(createClassification(AdsbMessage.TRACK));
        return classifier;
    }

    protected List<SmlCapabilities> createMobileInsitu() {
        return createMobileInsitu(true, true);
    }

    protected List<String> createObservableProperties() {
        return Lists.newArrayList(AdsbMessage.ALTITUDE, AdsbMessage.SPEED, AdsbMessage.TRACK);
    }

    protected SosInsertionMetadata createInsertSensorMetadata() {
        SosInsertionMetadata metadata = new SosInsertionMetadata();
        metadata.setFeatureOfInterestTypes(Collections.singleton(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE));
        metadata.setObservationTypes(Collections.singleton(OmConstants.OBS_TYPE_MEASUREMENT));
        metadata.setObservationTypes(Collections.singleton(InspireOMSOConstants.OBS_TYPE_TRAJECTORY_OBSERVATION));
        return metadata;
    }

}
