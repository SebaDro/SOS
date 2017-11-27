/*
 * Copyright (C) 2017 52north.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.n52.sos.mqtt.convert.marine;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.n52.sos.mqtt.api.CtdMessage;
import org.n52.sos.mqtt.convert.AbstractMqttInsertSensorConverter;
import org.n52.sos.ogc.OGCConstants;
import org.n52.sos.ogc.om.OmConstants;
import org.n52.sos.ogc.om.features.SfConstants;
import org.n52.sos.ogc.sensorML.elements.SmlCapabilities;
import org.n52.sos.ogc.sensorML.elements.SmlClassifier;
import org.n52.sos.ogc.sensorML.elements.SmlIdentifier;
import org.n52.sos.ogc.sensorML.elements.SmlIo;
import org.n52.sos.ogc.sos.SosInsertionMetadata;

/**
 *
 * @author Sebastian Drost
 */
public class CtdInsertSensorConverter extends AbstractMqttInsertSensorConverter<CtdMessage> {

    @Override
    protected String getProcedure(CtdMessage message) {
        return message.getProcedure();
    }

    @Override
    protected SosInsertionMetadata createInsertSensorMetadata() {
        SosInsertionMetadata metadata = new SosInsertionMetadata();
        metadata.setFeatureOfInterestTypes(Collections.singleton(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_POINT));
//        metadata.setObservationTypes(Lists.newArrayList(OmConstants.OBS_TYPE_MEASUREMENT,
//                OmConstants.OBS_TYPE_CATEGORY_OBSERVATION,
//                OmConstants.OBS_TYPE_COUNT_OBSERVATION,
//                OmConstants.OBS_TYPE_TEXT_OBSERVATION,
//                OmConstants.OBS_TYPE_TRUTH_OBSERVATION,
//                OmConstants.OBS_TYPE_GEOMETRY_OBSERVATION));
        metadata.setObservationTypes(Collections.singleton(OmConstants.OBS_TYPE_MEASUREMENT));
        return metadata;
    }

    @Override
    protected List<String> createObservableProperties() {
        return Lists.newArrayList(CtdMessage.PRESSURE, CtdMessage.TEMPERATURE, CtdMessage.CONDUCTIVITY, CtdMessage.SALINITY, CtdMessage.SOUND_VELOCITY);
    }

    @Override
    protected List<SmlCapabilities> createMobileInsitu() {
        return createMobileInsitu(true, false);
    }

    @Override
    protected List<SmlClassifier> createClassificationList() {
        List<SmlClassifier> classifier = Lists.newArrayList();
        classifier.add(createClassification(CtdMessage.PRESSURE));
        classifier.add(createClassification(CtdMessage.TEMPERATURE));
        classifier.add(createClassification(CtdMessage.CONDUCTIVITY));
        classifier.add(createClassification(CtdMessage.SALINITY));
        classifier.add(createClassification(CtdMessage.SOUND_VELOCITY));
        return classifier;
    }

    @Override
    protected List<SmlIdentifier> createIdentificationList(String procedureId, CtdMessage message) {
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
    protected List<SmlIo<?>> createOutputs() {
        List<SmlIo<?>> outputs = Lists.newArrayList();
        outputs.add(createQuantityOutput(CtdMessage.PRESSURE, CtdMessage.PRESSURE_UNIT));
        outputs.add(createQuantityOutput(CtdMessage.TEMPERATURE, CtdMessage.TEMPERATURE_UNIT));
        outputs.add(createQuantityOutput(CtdMessage.CONDUCTIVITY, CtdMessage.CONDUCTIVITY_UNIT));
        outputs.add(createQuantityOutput(CtdMessage.SALINITY, CtdMessage.SALINITY_UNIT));
        outputs.add(createQuantityOutput(CtdMessage.SOUND_VELOCITY, CtdMessage.SOUND_VELOCITY_UNIT));
        return outputs;
    }

    @Override
    protected List<SmlIo<?>> createInputs() {
        List<SmlIo<?>> inputs = Lists.newArrayList();
        inputs.add(createInput(CtdMessage.PRESSURE));
        inputs.add(createInput(CtdMessage.TEMPERATURE));
        inputs.add(createInput(CtdMessage.CONDUCTIVITY));
        inputs.add(createInput(CtdMessage.SALINITY));
        inputs.add(createInput(CtdMessage.SOUND_VELOCITY));
        return inputs;
    }

}
