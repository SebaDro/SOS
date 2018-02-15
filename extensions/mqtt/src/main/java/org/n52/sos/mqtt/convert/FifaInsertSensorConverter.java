/*
 * Copyright (C) 2012-2018 52°North Initiative for Geospatial Open Source
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

import java.util.Collections;
import java.util.List;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.sensorML.elements.SmlCapabilities;
import org.n52.shetland.ogc.sensorML.elements.SmlClassifier;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifier;
import org.n52.shetland.ogc.sensorML.elements.SmlIo;
import org.n52.shetland.ogc.sos.SosInsertionMetadata;
import org.n52.shetland.ogc.sos.SosOffering;

import org.n52.sos.mqtt.api.FifaMessage;
import org.n52.svalbard.encode.EncoderRepository;

public class FifaInsertSensorConverter extends AbstractMqttInsertSensorConverter<FifaMessage> {

    public FifaInsertSensorConverter(EncoderRepository encoderRepository) {
        super(encoderRepository);
    }

    @Override
    protected String getProcedure(FifaMessage message) {
        return message.getProcedure();
    }

    @Override
    protected SosInsertionMetadata createInsertSensorMetadata() {
        SosInsertionMetadata metadata = new SosInsertionMetadata();
        metadata.setFeatureOfInterestTypes(Collections.singleton(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_POINT));
        metadata.setObservationTypes(Collections.singleton(OmConstants.OBS_TYPE_MEASUREMENT));
        metadata.setObservationTypes(Collections.singleton(OmConstants.OBS_TYPE_COUNT_OBSERVATION));
        metadata.setObservationTypes(Collections.singleton(OmConstants.OBS_TYPE_TEXT_OBSERVATION));
        return metadata;
    }

    @Override
    protected List<String> createObservableProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<SmlCapabilities> createMobileInsitu() {
        return createMobileInsitu(true, false);
    }

    @Override
    protected List<SmlCapabilities> createCapabilities(SosOffering sosOffering) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<SmlClassifier> createClassificationList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<SmlIdentifier> createIdentificationList(String procedureId, FifaMessage message) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<SmlIo> createOutputs() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<SmlIo> createInputs() {
        // TODO Auto-generated method stub
        return null;
    }

}
