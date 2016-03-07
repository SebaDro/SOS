package org.n52.sos.mqtt.convert;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.joda.time.DateTime;
import org.n52.sos.mqtt.api.AdsbMessage;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.request.InsertObservationRequest;
import org.junit.Test;

public class AdsbToInsertObservationTest {

    private AdsbMessage message = new AdsbMessage().setAltitude(123).setFlight("LH123").setHex("49454").setLat(52.0)
            .setLon(7.0).setSpeed(123).setTrack(123).setTime(new DateTime());
    
    private AdsbToInsertObservation converter = new AdsbToInsertObservation();
    
    @Test
    public void testConvert() throws OwsExceptionReport {
       InsertObservationRequest insertObservation = converter.convert(message);
       MatcherAssert.assertThat(insertObservation.isSetObservation(), CoreMatchers.is(true));
       MatcherAssert.assertThat(insertObservation.getObservations().size(), CoreMatchers.is(3));
       MatcherAssert.assertThat(insertObservation.isSetOfferings(), CoreMatchers.is(true));
       MatcherAssert.assertThat(insertObservation.getOfferings().size(), CoreMatchers.is(1));
    }

}
