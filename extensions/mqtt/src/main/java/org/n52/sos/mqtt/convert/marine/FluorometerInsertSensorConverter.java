/*
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
package org.n52.sos.mqtt.convert.marine;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.sensorML.elements.SmlCapabilities;
import org.n52.shetland.ogc.sensorML.elements.SmlClassifier;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifier;
import org.n52.shetland.ogc.sensorML.elements.SmlIo;
import org.n52.shetland.ogc.sos.SosInsertionMetadata;
import org.n52.sos.mqtt.api.FluorometerMessage;
import org.n52.sos.mqtt.convert.AbstractMqttInsertSensorConverter;
/**
 *
 * @author Sebastian
 */
public class FluorometerInsertSensorConverter extends AbstractMqttInsertSensorConverter<FluorometerMessage> {

    @Override
    protected String getProcedure(FluorometerMessage message) {
        return message.getProcedure();
    }

    @Override
    protected SosInsertionMetadata createInsertSensorMetadata() {
        SosInsertionMetadata metadata = new SosInsertionMetadata();
        metadata.setFeatureOfInterestTypes(Collections.singleton(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_POINT));
        metadata.setObservationTypes(Lists.newArrayList(OmConstants.OBS_TYPE_MEASUREMENT,
                OmConstants.OBS_TYPE_COUNT_OBSERVATION));
        return metadata;
    }

    @Override
    protected List<String> createObservableProperties() {
        return Lists.newArrayList(FluorometerMessage.CHL, FluorometerMessage.FLUORESCENCE, FluorometerMessage.NTU, FluorometerMessage.THERMISTOR, FluorometerMessage.TURBIDITY);
    }

    @Override
    protected List<SmlCapabilities> createMobileInsitu() {
        return createMobileInsitu(true, false);
    }

    @Override
    protected List<SmlClassifier> createClassificationList() {
        List<SmlClassifier> classifier = Lists.newArrayList();
        classifier.add(createClassification(FluorometerMessage.CHL));
        classifier.add(createClassification(FluorometerMessage.FLUORESCENCE));
        classifier.add(createClassification(FluorometerMessage.NTU));
        classifier.add(createClassification(FluorometerMessage.THERMISTOR));
        classifier.add(createClassification(FluorometerMessage.TURBIDITY));
        return classifier;
    }

    @Override
    protected List<SmlIdentifier> createIdentificationList(String procedureId, FluorometerMessage message) {
        List<SmlIdentifier> idents = new ArrayList<>();
        idents.add(new SmlIdentifier(
                OGCConstants.UNIQUE_ID,
                OGCConstants.URN_UNIQUE_IDENTIFIER,
                procedureId));
        idents.add(new SmlIdentifier(
                "longName",
                "urn:ogc:def:identifier:OGC:1.0:longName",
                procedureId));
        if (!Strings.isNullOrEmpty(message.getProcedure())) {
            idents.add(new SmlIdentifier(
                    "sensor_id",
                    "sensor_id",
                    message.getSensorId()));
        }
        return idents;
    }

    @Override
    protected List<SmlIo> createOutputs() {
        List<SmlIo> outputs = Lists.newArrayList();
        outputs.add(createQuantityOutput(FluorometerMessage.FLUORESCENCE, FluorometerMessage.FLUORESCENCE_UNIT));
        outputs.add(createQuantityOutput(FluorometerMessage.TURBIDITY, FluorometerMessage.TURBIDITY_UNIT));
        outputs.add(createCountOutput(FluorometerMessage.CHL));
        outputs.add(createCountOutput(FluorometerMessage.NTU));
        outputs.add(createCountOutput(FluorometerMessage.THERMISTOR));

        return outputs;
    }

    @Override
    protected List<SmlIo> createInputs() {
        List<SmlIo> inputs = Lists.newArrayList();
        inputs.add(createInput(FluorometerMessage.FLUORESCENCE));
        inputs.add(createInput(FluorometerMessage.TURBIDITY));
        inputs.add(createInput(FluorometerMessage.CHL));
        inputs.add(createInput(FluorometerMessage.NTU));
        inputs.add(createInput(FluorometerMessage.THERMISTOR));
        return inputs;
    }

}
