package org.n52.sos.mqtt.convert;

import java.util.ArrayList;
import java.util.List;

import org.n52.sos.encode.SensorMLEncoderv20;
import org.n52.sos.mqtt.api.FifaMessage;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sensorML.SensorML20Constants;
import org.n52.sos.ogc.sensorML.elements.SmlCapabilities;
import org.n52.sos.ogc.sensorML.elements.SmlCapability;
import org.n52.sos.ogc.sensorML.elements.SmlClassifier;
import org.n52.sos.ogc.sensorML.elements.SmlIdentifier;
import org.n52.sos.ogc.sensorML.elements.SmlIo;
import org.n52.sos.ogc.sensorML.v20.PhysicalSystem;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.ogc.sos.SosInsertionMetadata;
import org.n52.sos.ogc.sos.SosOffering;
import org.n52.sos.ogc.swe.SweField;
import org.n52.sos.ogc.swe.simpleType.SweBoolean;
import org.n52.sos.ogc.swe.simpleType.SweObservableProperty;
import org.n52.sos.ogc.swe.simpleType.SweQuantity;
import org.n52.sos.ogc.swe.simpleType.SweText;
import org.n52.sos.request.InsertSensorRequest;
import org.n52.sos.request.RequestContext;
import org.n52.sos.util.net.IPAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public abstract class AbstractMqttInsertSensorConverter<T> implements MqttInsertSensorConverter<T> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMqttInsertSensorConverter.class);
    
    @Override
    public InsertSensorRequest convert(T message) throws OwsExceptionReport {
        final InsertSensorRequest insertSensorRequest = new InsertSensorRequest();
        insertSensorRequest.setService(SosConstants.SOS);
        insertSensorRequest.setVersion(Sos2Constants.SERVICEVERSION);
        RequestContext requestContext = new RequestContext();
        requestContext.setIPAddress(new IPAddress("127.0.0.1"));
        insertSensorRequest.setRequestContext(requestContext);
        insertSensorRequest.setProcedureDescriptionFormat(SensorML20Constants.NS_SML_20);
        final PhysicalSystem system = new PhysicalSystem();
        
        final String procedureId = getProcedure(message);
        final SosOffering sosOffering = new SosOffering(getProcedure(message), true);
        system.addOffering(sosOffering);
        system
                .setInputs(createInputs())
                .setOutputs(createOutputs())
                .setIdentifications(createIdentificationList(procedureId, message))
                .setClassifications(createClassificationList())
                .addCapabilities(createCapabilities(sosOffering))
                .addCapabilities(createMobileInsitu())
//                .addContact(createContact(schemaDescription.getDataset())) // TODO
                // ... // TODO
                .setIdentifier(procedureId);
        
        system.setSensorDescriptionXmlString(encodeToXml(system));
        
        insertSensorRequest.setObservableProperty(createObservableProperties());
        insertSensorRequest.setProcedureDescription(system);
        insertSensorRequest.setMetadata(createInsertSensorMetadata());
        return insertSensorRequest;
    }
    
    protected static String encodeToXml(final PhysicalSystem system) {
        try {
            return new SensorMLEncoderv20().encode(system).xmlText();
        } catch (OwsExceptionReport ex) {
            LOGGER.error("Could not encode SML to valid XML.", ex);
            return "";  // TODO empty but valid sml
        }
    }
    
    protected SmlIo<?> createInput(String phenomeon) {
        return new SmlIo<>(new SweObservableProperty()
                .setDefinition(phenomeon))
                .setIoName(phenomeon);
    }
    
    protected SmlIo<?> createOutput(String phenomeon, String unit) {
        return new SmlIo<>(new SweQuantity()
                .setUom(unit)
                .setDefinition(phenomeon))
                .setIoName(phenomeon);
    }
    
    protected SmlClassifier createClassification(String phenomenon) {
        return new SmlClassifier(
                "phenomenon", 
                "urn:ogc:def:classifier:OGC:1.0:phenomenon",
                null, 
                phenomenon);
    }
    
    protected SmlCapabilities createOfferingCapabilities(SosOffering offering) {
        SmlCapabilities capabilities = new SmlCapabilities("offerings");
        
        SmlCapability ofering = new SmlCapability("offeringID", createText("urn:ogc:def:identifier:OGC:offeringID", offering.getIdentifier()));
        capabilities.addCapability(ofering);
        return capabilities;
    }

    protected SweField createTextField(String name, String definition, String value) {
        return new SweField(name, new SweText().setValue(value).setDefinition(definition));
    }
    
    protected SweText createText(String definition, String value) {
        return (SweText) new SweText().setValue(value).setDefinition(definition);
    } 

    protected List<SmlCapabilities> createMobileInsitu(boolean insitu, boolean mobile) {
        SmlCapabilities capabilities = new SmlCapabilities("metadata");
        
        SmlCapability smlcInsitu  = new SmlCapability("insitu");
        smlcInsitu.setAbstractDataComponent(new SweBoolean().setValue(insitu).addName("insitu"));
        capabilities.addCapability(smlcInsitu);
        
        SmlCapability smlcMmobile = new SmlCapability("mobile");
        smlcMmobile.setAbstractDataComponent(new SweBoolean().setValue(mobile).addName("mobile"));
        capabilities.addCapability(smlcMmobile);
        
        return Lists.newArrayList(capabilities);
    }
    
    protected List<SmlCapabilities> createCapabilities(SosOffering offering) {
        List<SmlCapabilities> capabilities = new ArrayList<>();
        capabilities.add(createOfferingCapabilities(offering));
        return capabilities;
    }
    
    protected abstract String getProcedure(T message);

    protected abstract SosInsertionMetadata createInsertSensorMetadata();

    protected abstract List<String> createObservableProperties();

    protected abstract List<SmlCapabilities> createMobileInsitu();

    protected abstract List<SmlClassifier> createClassificationList();

    protected abstract List<SmlIdentifier> createIdentificationList(String procedureId, T message);

    protected abstract List<SmlIo<?>> createOutputs();

    protected abstract List<SmlIo<?>> createInputs();

}
