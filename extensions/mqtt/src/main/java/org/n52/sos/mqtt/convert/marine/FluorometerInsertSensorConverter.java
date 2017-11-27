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
import org.n52.sos.mqtt.api.FluorometerMessage;
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
    protected List<SmlIo<?>> createOutputs() {
        List<SmlIo<?>> outputs = Lists.newArrayList();
        outputs.add(createQuantityOutput(FluorometerMessage.FLUORESCENCE, FluorometerMessage.FLUORESCENCE_UNIT));
        outputs.add(createQuantityOutput(FluorometerMessage.TURBIDITY, FluorometerMessage.TURBIDITY_UNIT));
        outputs.add(createCountOutput(FluorometerMessage.CHL));
        outputs.add(createCountOutput(FluorometerMessage.NTU));
        outputs.add(createCountOutput(FluorometerMessage.THERMISTOR));

        return outputs;
    }

    @Override
    protected List<SmlIo<?>> createInputs() {
        List<SmlIo<?>> inputs = Lists.newArrayList();
        inputs.add(createInput(FluorometerMessage.FLUORESCENCE));
        inputs.add(createInput(FluorometerMessage.TURBIDITY));
        inputs.add(createInput(FluorometerMessage.CHL));
        inputs.add(createInput(FluorometerMessage.NTU));
        inputs.add(createInput(FluorometerMessage.THERMISTOR));
        return inputs;
    }

}
