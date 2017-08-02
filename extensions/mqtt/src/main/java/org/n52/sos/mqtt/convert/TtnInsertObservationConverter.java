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
package org.n52.sos.mqtt.convert;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import static org.n52.sos.coding.json.JSONConstants.OBSERVATION;
import org.n52.sos.decode.json.impl.InsertObservationRequestDecoder;
import org.n52.sos.decode.json.impl.ObservationDecoder;
import org.n52.sos.mqtt.api.OmMessage;
import org.n52.sos.mqtt.api.TtnMessage;
import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.request.InsertObservationRequest;
import org.n52.sos.request.RequestContext;
import org.n52.sos.util.net.IPAddress;

/**
 *
 * @author Sebastian Drost
 */
public class TtnInsertObservationConverter implements MqttInsertObservationConverter<TtnMessage> {

    private ObservationDecoder observationDecoder;

    public TtnInsertObservationConverter() {
        observationDecoder = new ObservationDecoder();
    }

    @Override
    public InsertObservationRequest convert(TtnMessage message) throws OwsExceptionReport {

        List<OmObservation> observations = observationDecoder.decodeJsonToObjectList(message.getOmPayload(), OmObservation.class);

//        List<OmObservation> observations = Lists.newArrayList(observation);
        InsertObservationRequest request = new InsertObservationRequest();
        request.setService(SosConstants.SOS);
        request.setVersion(Sos2Constants.SERVICEVERSION);
        RequestContext requestContext = new RequestContext();
        requestContext.setIPAddress(new IPAddress("127.0.0.1"));
        request.setRequestContext(requestContext);
        request.setOfferings(Lists.newArrayList(message.getDevId()));
        request.setObservation(observations);
        return request;
    }

}
