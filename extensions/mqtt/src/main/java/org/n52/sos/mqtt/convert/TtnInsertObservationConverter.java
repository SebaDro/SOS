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

import com.google.common.collect.Lists;
import org.n52.janmayen.net.IPAddress;
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsServiceRequestContext;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.InsertObservationRequest;
import org.n52.sos.mqtt.api.TtnMessage;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.InsertObservationRequestDecoder;

/**
 * 
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class TtnInsertObservationConverter implements MqttInsertObservationConverter<TtnMessage> {

    private InsertObservationRequestDecoder insertObservationDecoder;

    public TtnInsertObservationConverter() {
        insertObservationDecoder = new InsertObservationRequestDecoder();
    }

    @Override
    public InsertObservationRequest convert(TtnMessage message) throws OwsExceptionReport {

        InsertObservationRequest request;
        try {
            request = insertObservationDecoder.decode(message.getOmPayload());
            request.setService(SosConstants.SOS);
            request.setVersion(Sos2Constants.SERVICEVERSION);
            OwsServiceRequestContext requestContext = new OwsServiceRequestContext();
            requestContext.setIPAddress(new IPAddress("127.0.0.1"));
            request.setRequestContext(requestContext);
            request.setOfferings(Lists.newArrayList(message.getProcedure()));
            return request;
        } catch (DecodingException ex) {
            throw new NoApplicableCodeException().causedBy(ex);
        }
    }

}
