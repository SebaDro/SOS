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

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.sensorML.elements.SmlCapabilities;
import org.n52.shetland.ogc.sensorML.elements.SmlClassifier;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifier;
import org.n52.shetland.ogc.sensorML.elements.SmlIo;
import org.n52.shetland.ogc.sos.SosInsertionMetadata;
import org.n52.sos.mqtt.api.TtnMessage;
import org.n52.svalbard.encode.EncoderRepository;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class TtnInsertSensorConverter extends AbstractMqttInsertSensorConverter<TtnMessage> {

    private Set<String> observableProperties;

    public TtnInsertSensorConverter(EncoderRepository encoderRepository) {
        super(encoderRepository);
    }

    public TtnInsertSensorConverter setObservableProperties(Set<String> observableProperties) {
        this.observableProperties = observableProperties;
        return this;
    }

    @Override
    protected String getProcedure(TtnMessage message) {
        return message.getProcedure();
    }

    @Override
    protected SosInsertionMetadata createInsertSensorMetadata() {
        SosInsertionMetadata metadata = new SosInsertionMetadata();
        metadata.setFeatureOfInterestTypes(Collections.singleton(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_POINT));
        metadata.setObservationTypes(Lists.newArrayList(OmConstants.OBS_TYPE_MEASUREMENT,
                OmConstants.OBS_TYPE_CATEGORY_OBSERVATION,
                OmConstants.OBS_TYPE_COUNT_OBSERVATION,
                OmConstants.OBS_TYPE_TEXT_OBSERVATION,
                OmConstants.OBS_TYPE_TRUTH_OBSERVATION,
                OmConstants.OBS_TYPE_GEOMETRY_OBSERVATION));
        return metadata;
    }

    @Override
    protected List<String> createObservableProperties() {
        return Lists.newArrayList(observableProperties);
    }

    @Override
    protected List<SmlCapabilities> createMobileInsitu() {
        return createMobileInsitu(true, false);
    }

    @Override
    protected List<SmlClassifier> createClassificationList() {
        List<SmlClassifier> classifier = Lists.newArrayList();
        observableProperties.forEach(oP -> classifier.add(createClassification(oP)));
//        for (int i = 0; i < observableProperty.length; i++) {
//            classifier.add(createClassification(observableProperty[i]));
//        }
        return classifier;
    }

    @Override
    protected List<SmlIdentifier> createIdentificationList(String procedureId, TtnMessage message) {
        List<SmlIdentifier> idents = new ArrayList<>();
        idents.add(new SmlIdentifier(
                OGCConstants.UNIQUE_ID,
                OGCConstants.URN_UNIQUE_IDENTIFIER,
                // TODO check feautre id vs name
                procedureId));
        idents.add(new SmlIdentifier(
                "longName",
                "urn:ogc:def:identifier:OGC:1.0:longName",
                procedureId));
        if (!Strings.isNullOrEmpty(message.getProcedure())) {
            idents.add(new SmlIdentifier(
                    "dev_id",
                    "dev_id",
                    message.getDevId()));
        }
        return idents;
    }

    @Override
    protected List<SmlIo> createOutputs() {
        List<SmlIo> outputs = Lists.newArrayList();
        observableProperties.forEach(oP -> outputs.add(createQuantityOutput(oP, "")));
//        for (int i = 0; i < observableProperty.length; i++) {
//            outputs.add(createQuantityOutput(observableProperty[i], ""));
//        }
        return outputs;
    }

    @Override
    protected List<SmlIo> createInputs() {
        List<SmlIo> inputs = Lists.newArrayList();
        observableProperties.forEach(oP -> inputs.add(createInput(oP)));
//        for (int i = 0; i < observableProperty.length; i++) {
//            inputs.add(createInput(observableProperty[i]));
//        }
        return inputs;
    }

}
