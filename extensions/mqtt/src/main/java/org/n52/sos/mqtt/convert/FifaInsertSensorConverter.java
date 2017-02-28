package org.n52.sos.mqtt.convert;

import java.util.Collections;
import java.util.List;

import org.n52.sos.mqtt.api.FifaMessage;
import org.n52.sos.ogc.om.OmConstants;
import org.n52.sos.ogc.om.features.SfConstants;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sensorML.SensorML20Constants;
import org.n52.sos.ogc.sensorML.elements.SmlCapabilities;
import org.n52.sos.ogc.sensorML.elements.SmlClassifier;
import org.n52.sos.ogc.sensorML.elements.SmlIdentifier;
import org.n52.sos.ogc.sensorML.elements.SmlIo;
import org.n52.sos.ogc.sensorML.v20.PhysicalSystem;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.ogc.sos.SosInsertionMetadata;
import org.n52.sos.ogc.sos.SosOffering;
import org.n52.sos.request.InsertSensorRequest;
import org.n52.sos.request.RequestContext;
import org.n52.sos.util.net.IPAddress;
import org.n52.svalbard.inspire.omso.InspireOMSOConstants;

public class FifaInsertSensorConverter extends AbstractMqttInsertSensorConverter<FifaMessage> {

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
    protected List<SmlIo<?>> createOutputs() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<SmlIo<?>> createInputs() {
        // TODO Auto-generated method stub
        return null;
    }

}
