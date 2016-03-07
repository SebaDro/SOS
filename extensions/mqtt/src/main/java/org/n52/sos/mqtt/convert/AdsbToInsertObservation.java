package org.n52.sos.mqtt.convert;

import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.n52.sos.mqtt.api.AdsbMessage;
import org.n52.sos.ogc.gml.AbstractFeature;
import org.n52.sos.ogc.gml.CodeWithAuthority;
import org.n52.sos.ogc.gml.ReferenceType;
import org.n52.sos.ogc.gml.time.TimeInstant;
import org.n52.sos.ogc.om.AbstractPhenomenon;
import org.n52.sos.ogc.om.NamedValue;
import org.n52.sos.ogc.om.OmConstants;
import org.n52.sos.ogc.om.OmObservableProperty;
import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.om.OmObservationConstellation;
import org.n52.sos.ogc.om.SingleObservationValue;
import org.n52.sos.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.sos.ogc.om.values.GeometryValue;
import org.n52.sos.ogc.om.values.QuantityValue;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sensorML.v20.PhysicalSystem;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.ogc.sos.SosProcedureDescription;
import org.n52.sos.request.InsertObservationRequest;
import org.n52.sos.util.GeometryHandler;
import org.n52.sos.util.JTSHelper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;

public class AdsbToInsertObservation {
    
    public InsertObservationRequest convert(AdsbMessage message) throws OwsExceptionReport {
        List<OmObservation> observations = Lists.newArrayList();
        observations.add(createTrackObservation(message));
        observations.add(createSpeedObservation(message));
        observations.add(createAltitudeObservation(message));
        
        InsertObservationRequest request = new InsertObservationRequest();
        request.setOfferings(Lists.newArrayList(message.getHex()));
        request.setObservation(observations);
        return request;
    }
    
    private OmObservationConstellation createObservationConstellation(AdsbMessage message, String phenomenon) {
        OmObservationConstellation constellation = new OmObservationConstellation();
        constellation.setObservableProperty(createPhenomenon(phenomenon));
        constellation.setFeatureOfInterest(createFeatureOfInterest(message));
        constellation.setOfferings(createOffering(message));
        constellation.setObservationType(OmConstants.OBS_TYPE_MEASUREMENT);
        constellation.setProcedure(createProcedure(message));
        return constellation;
    }

    private OmObservation createAltitudeObservation(AdsbMessage message) throws OwsExceptionReport {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(message, AdsbMessage.ALTITUDE));
        observation.addParameter(createSpatialFilteringProfileParameter(message));
        observation.setValue(createQuantityObservationValue(message.getTime(), message.getAltitude(), AdsbMessage.ALTITUDE_UNIT));
        return observation;
    }

    private OmObservation createSpeedObservation(AdsbMessage message) throws OwsExceptionReport {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(message, AdsbMessage.SPEED));
        observation.addParameter(createSpatialFilteringProfileParameter(message));
        observation.setValue(createQuantityObservationValue(message.getTime(), message.getSpeed(), AdsbMessage.SPEED_UNIT));
        return observation;
    }

    private OmObservation createTrackObservation(AdsbMessage message) throws OwsExceptionReport {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(message, AdsbMessage.TRACK));
        observation.addParameter(createSpatialFilteringProfileParameter(message));
        observation.setValue(createQuantityObservationValue(message.getTime(), message.getTrack(), AdsbMessage.TRACK_UNIT));
        return observation;
    }

    private SosProcedureDescription createProcedure(AdsbMessage message) {
        return new PhysicalSystem().setIdentifier(message.getHex());
    }

    private Set<String> createOffering(AdsbMessage message) {
        return Sets.newHashSet(message.getHex());
    }

    private AbstractPhenomenon createPhenomenon(String identifier) {
        return new OmObservableProperty(identifier);
    }

    private AbstractFeature createFeatureOfInterest(AdsbMessage message) {
        String idetifier = message.getFlight();
        if (Strings.isNullOrEmpty(idetifier)) {
            idetifier = message.getHex();
        }
        return new SamplingFeature(new CodeWithAuthority(idetifier));
    }

    private NamedValue<?> createSpatialFilteringProfileParameter(AdsbMessage message) throws OwsExceptionReport {
        final NamedValue<Geometry> namedValue = new NamedValue<Geometry>();
        namedValue.setName(new ReferenceType(Sos2Constants.HREF_PARAMETER_SPATIAL_FILTERING_PROFILE));
        int epsg = 4326;
        JTSHelper.getGeometryFactoryForSRID(epsg);
        final String wktString = GeometryHandler.getInstance().getWktString(message.getLon(), message.getLat());
        Geometry geometry = JTSHelper.createGeometryFromWKT(wktString, epsg);
        namedValue.setValue(new GeometryValue(geometry));
        return namedValue;
    }
    
    protected SingleObservationValue<Double> createQuantityObservationValue(DateTime time, int value, String unit) {
        SingleObservationValue<Double> obsValue = new SingleObservationValue<>();
        QuantityValue quantityValue = new QuantityValue(Double.parseDouble(Integer.toString(value)));
        quantityValue.setUnit(unit);
        obsValue.setValue(quantityValue);
        obsValue.setPhenomenonTime(new TimeInstant(time));
        return obsValue;
    }
}
