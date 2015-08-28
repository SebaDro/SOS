/**
 * Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.binding.rest;

import static org.n52.iceland.util.http.HTTPStatus.INTERNAL_SERVER_ERROR;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.binding.Binding;
import org.n52.iceland.binding.BindingKey;
import org.n52.iceland.binding.PathBindingKey;
import org.n52.iceland.coding.decode.Decoder;
import org.n52.iceland.coding.decode.DecoderRepository;
import org.n52.iceland.coding.encode.Encoder;
import org.n52.iceland.coding.encode.EncoderKey;
import org.n52.iceland.coding.encode.EncoderRepository;
import org.n52.iceland.coding.encode.ExceptionEncoderKey;
import org.n52.iceland.coding.encode.XmlEncoderKey;
import org.n52.iceland.event.ServiceEventBus;
import org.n52.iceland.event.events.ExceptionEvent;
import org.n52.iceland.exception.CodedException;
import org.n52.iceland.exception.HTTPException;
import org.n52.iceland.exception.ows.MissingParameterValueException;
import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.exception.ows.OwsExceptionCode;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.exception.ows.concrete.NoEncoderForKeyException;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.ogc.sos.Sos2Constants;
import org.n52.iceland.ogc.sos.SosConstants;
import org.n52.iceland.response.ServiceResponse;
import org.n52.iceland.util.Producer;
import org.n52.iceland.util.http.HTTPStatus;
import org.n52.iceland.util.http.HttpUtils;
import org.n52.iceland.util.http.MediaTypes;
import org.n52.sos.binding.rest.decode.RestDecoder;
import org.n52.sos.binding.rest.encode.RestEncoder;
import org.n52.sos.binding.rest.requests.BadRequestException;
import org.n52.sos.binding.rest.requests.RequestHandler;
import org.n52.sos.binding.rest.requests.RestRequest;
import org.n52.sos.binding.rest.requests.RestResponse;
import org.n52.sos.binding.rest.resources.OptionsRequestHandler;
import org.n52.sos.binding.rest.resources.OptionsRestRequest;
import org.n52.sos.binding.rest.resources.ServiceEndpointRequest;
import org.n52.sos.binding.rest.resources.ServiceEndpointRequestHandler;
import org.n52.sos.binding.rest.resources.capabilities.CapabilitiesRequest;
import org.n52.sos.binding.rest.resources.capabilities.CapabilitiesRequestHandler;
import org.n52.sos.binding.rest.resources.features.FeaturesRequest;
import org.n52.sos.binding.rest.resources.features.FeaturesRequestHandler;
import org.n52.sos.binding.rest.resources.observations.IObservationsRequest;
import org.n52.sos.binding.rest.resources.observations.ObservationsRequestHandler;
import org.n52.sos.binding.rest.resources.offerings.OfferingsRequest;
import org.n52.sos.binding.rest.resources.offerings.OfferingsRequestHandler;
import org.n52.sos.binding.rest.resources.sensors.ISensorsRequest;
import org.n52.sos.binding.rest.resources.sensors.SensorsRequestHandler;

import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 */
public class RestBinding extends Binding implements Constructable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestBinding.class);
    public static final String URI_PATTERN = "/rest";
    private static final Set<BindingKey> KEYS = Collections.<BindingKey>singleton(new PathBindingKey(URI_PATTERN));
    private Set<String> conformanceClasses;
    private Constants bindingConstants;
    private ServiceEventBus eventBus;
    private EncoderRepository encoderRepository;
    private DecoderRepository decoderRepository;

    private Producer<XmlOptions> xmlOptions;
    
    private HttpUtils httpUtils;

    public HttpUtils getHttpUtils() {
        return httpUtils;
    }

    @Inject
    public void setHttpUtils(HttpUtils httpUtils) {
        this.httpUtils = httpUtils;
    }

    public void setXmlOptions(Producer<XmlOptions> xmlOptions) {
        this.xmlOptions = xmlOptions;
    }

    public XmlOptions getXmlOptions() {
        return xmlOptions.get();
    }

    @Inject
    public void setDecoderRepository(DecoderRepository decoderRepository) {
        this.decoderRepository = decoderRepository;
    }

    public DecoderRepository getDecoderRepository() {
        return decoderRepository;
    }

    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    public EncoderRepository getEncoderRepository() {
        return encoderRepository;
    }

    @Inject
    public void setEventBus(ServiceEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public ServiceEventBus getEventBus() {
        return eventBus;
    }

    @Inject
    public void setBindingConstants(Constants bindingConstants) {
        this.bindingConstants = bindingConstants;
    }

    @Override
    public void init() {
        this.conformanceClasses = Sets.newHashSet(this.bindingConstants.getConformanceClass());
    }

    @Override
    public Set<BindingKey> getKeys() {
        return KEYS;
    }

    @Override
    public String getUrlPattern() {
        return URI_PATTERN;
    }

    @Override
    public Set<String> getConformanceClasses(String service, String version) {
        if(SosConstants.SOS.equals(service) && Sos2Constants.SERVICEVERSION.equals(version)) {
            return Collections.unmodifiableSet(conformanceClasses);
        }
        return Collections.emptySet();
    }

    @Override
    public void doOptionsOperation(HttpServletRequest request,
                                   HttpServletResponse response)
            throws HTTPException, IOException {
        if (request.getPathInfo() == null ||
            request.getPathInfo().isEmpty()) {
            super.doOptionsOperation(request, response);
        } else {
            doOperation(request, response);
        }
    }

    @Override
    public void doDeleteOperation(HttpServletRequest request,
                                  HttpServletResponse response)
            throws HTTPException, IOException {
        doOperation(request, response);
    }

    @Override
    public void doPutOperation(HttpServletRequest request,
                               HttpServletResponse response)
            throws HTTPException, IOException {
        doOperation(request, response);
    }

    @Override
    public void doPostOperation(HttpServletRequest request,
                                HttpServletResponse response)
            throws HTTPException, IOException {
        doOperation(request, response);
    }

    @Override
    public void doGetOperation(HttpServletRequest request,
                               HttpServletResponse response)
            throws HTTPException, IOException {
        doOperation(request, response);
    }

    @Override
	public ServiceResponse handleOwsExceptionReport(HttpServletRequest request, HttpServletResponse response,
			OwsExceptionReport oer) throws HTTPException {
		try {
			 return encodeOwsExceptionReport(oer);
		} catch (IOException e) {
			throw new HTTPException(HTTPStatus.INTERNAL_SERVER_ERROR, e);
		}
	}

	/*
     * (non-Javadoc)
     *
     * @see org.n52.sos.binding.rest.Binding#doGetOperation(javax.servlet.http.
     * HttpServletRequest)
     *
     * INPUT: /rest/RESOURCE{/id|?kvps}
     *
     * EXAMPLE: /rest/sensors/0815 => OGC::DescribeSensor in format SensorML
     * 1.0.1 /rest/features?bbox=52.0,7.0,50.0,8.0 for all features in the given
     * BBox
     */
    protected void doOperation(HttpServletRequest request,
                               HttpServletResponse response)
            throws HTTPException, IOException{
        ServiceResponse serviceResponse;
        try {
            serviceResponse = handleRequest(request, response);
        } catch (final OwsExceptionReport oer) {
            LOGGER.error("Error while processing rest request. Exception thrown: {}",
                         oer.getClass().getSimpleName());
            this.eventBus.submit(new ExceptionEvent(oer));
            serviceResponse = encodeOwsExceptionReport(oer);
        }
        getHttpUtils().writeObject(request, response, serviceResponse, this);
    }

    private ServiceResponse encodeOwsExceptionReport(OwsExceptionReport oer) throws HTTPException, IOException {
        try {
            ExceptionEncoderKey key = new ExceptionEncoderKey(MediaTypes.TEXT_XML);
            Encoder<XmlObject, OwsExceptionReport> encoder =
                    getEncoderRepository().getEncoder(key);
            if (encoder == null) {
                throw new HTTPException(HTTPStatus.INTERNAL_SERVER_ERROR,
                        new NoEncoderForKeyException(key));
            }
            XmlObject encoded = encoder.encode(oer);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            encoded.save(baos, getXmlOptions());
            baos.flush();
            return new ServiceResponse(null, MediaTypes.TEXT_XML, getResponseCode(oer));
        } catch (OwsExceptionReport ex) {
            throw new HTTPException(INTERNAL_SERVER_ERROR, ex);
        }
    }

    private boolean isOperationNotSupportedException(final CodedException owsE) {
        return owsE.hasMessage() &&
               owsE.getMessage().contains(bindingConstants.getSosErrorMessageOperationNotSupportedStart()) &&
               owsE.getMessage().contains(bindingConstants.getSosErrorMessageOperationNotSupportedEnd());
    }

    private boolean isMethodeNotAllowedExceptionForResourceType(final CodedException owsE) {
        return owsE.hasMessage() &&
               owsE.getMessage().contains(bindingConstants.getHttpOperationNotAllowedForResourceTypeMessagePart());
    }

    private RestRequest decodeHttpRequest(final HttpServletRequest request) throws OwsExceptionReport
    {
        final Decoder<RestRequest, HttpServletRequest> decoder = getDecoder();

        if (decoder == null) {
            final String exceptionText = String.format("No decoder implementation is available for RestBinding and namespace \"%s\"!",
                    bindingConstants.getEncodingNamespace());
            LOGGER.debug(exceptionText);
            throw new NoApplicableCodeException().withMessage(exceptionText);
        }

        // 2 decode request
        final RestRequest restRequest = decoder.decode(request);

        if (restRequest == null) {
            final String exceptionText = "Decoding of request failed but no exception is thrown.";
            LOGGER.debug(exceptionText);
            throw new NoApplicableCodeException().withMessage(exceptionText).setStatus(INTERNAL_SERVER_ERROR);
        }
        return restRequest;
    }

    private ServiceResponse encodeRestResponse(final RestResponse restResponse) throws OwsExceptionReport
    {
        ServiceResponse response;
        final RestEncoder encoder = getEncoder();

        if (encoder == null) {
            final String exceptionText = String.format("No encoder implementation is available for RestBinding and namespace \"%s\"!",
                    bindingConstants.getEncodingNamespace());
            LOGGER.debug(exceptionText);
            throw new NoApplicableCodeException().withMessage(exceptionText);
        }

        response = encoder.encode(restResponse);

        if (response == null) {
            final String exceptionText = String.format("Encoding of response \"%s\" failed with encoder \"%s\" but no exception is thrown.",
                    restResponse.getClass().getName(),
                    encoder.getClass().getName());
            LOGGER.debug(exceptionText);
            throw new NoApplicableCodeException().withMessage(exceptionText);
        }
        return response;
    }

    private RestResponse handleRestRequest(final RestRequest restRequest) throws OwsExceptionReport
    {
        // 3 get request handler
        final RequestHandler requestHandler = getRequestHandler(restRequest);
        LOGGER.debug("RequestHandler of type {} found for RestRequest of type {}",
                     requestHandler.getClass().getName(),
                     restRequest != null ? restRequest.getClass().getName() : restRequest);

        try {
            RestResponse sdcResponse = requestHandler.handleRequest(restRequest);
            if (sdcResponse == null) {
                final String exceptionText = "Processing of rest request failed but no exception is thrown.";
                LOGGER.debug(exceptionText);
                throw new NoApplicableCodeException().withMessage(exceptionText);
             }
            return sdcResponse;
        } catch(XmlException | IOException xe) {
            final String exceptionText = String.format("Processing of rest request response failed. Exception thrown: %s",
                    xe.getMessage());
            LOGGER.debug(exceptionText,xe);
            throw new NoApplicableCodeException().withMessage(exceptionText).causedBy(xe);
        }
    }

    private RestEncoder getEncoder() throws OwsExceptionReport
    {
    	final EncoderKey key = new XmlEncoderKey(bindingConstants.getEncodingNamespace(), RestResponse.class);
        final Encoder<?,?> encoder = getEncoderRepository().getEncoder(key);
        if (encoder instanceof RestEncoder) {
            return (RestEncoder) encoder;
        }
        return null;
    }

    private RequestHandler getRequestHandler(final RestRequest restRequest)
            throws OwsExceptionReport {
        if ((restRequest != null)) {
            if (restRequest instanceof ISensorsRequest) {
                return new SensorsRequestHandler();
            } else if (restRequest instanceof IObservationsRequest) {
                return new ObservationsRequestHandler();
            } else if (restRequest instanceof CapabilitiesRequest) {
                return new CapabilitiesRequestHandler();
            } else if (restRequest instanceof OfferingsRequest) {
                return new OfferingsRequestHandler();
            } else if (restRequest instanceof FeaturesRequest) {
                return new FeaturesRequestHandler();
            } else if (restRequest instanceof OptionsRestRequest) {
                return new OptionsRequestHandler();
            } else if (restRequest instanceof ServiceEndpointRequest) {
                return new ServiceEndpointRequestHandler();
            }
        }
        throw new MissingParameterValueException(bindingConstants.getResourceType());
    }

	private RestDecoder getDecoder() throws OwsExceptionReport {
        final Set<Decoder<?, ?>> decoders = this.decoderRepository.getDecoders();
        for (final Decoder<?,?> decoder : decoders) {
            if (decoder instanceof RestDecoder) {
                return (RestDecoder) decoder;
            }
        }
        return null;
    }

    private HTTPStatus getResponseCode(final OwsExceptionReport oer) {
        for (final CodedException e : oer.getExceptions()) {
            if (e.getCode().equals(OwsExceptionCode.OperationNotSupported)) {
                if (isOperationNotSupportedException(e)) {
                    return HTTPStatus.BAD_REQUEST;
                }
            } else if (e.getCode().equals(OwsExceptionCode.NoApplicableCode)) {
                if (isMethodeNotAllowedExceptionForResourceType(e)) {
                    return HTTPStatus.METHOD_NOT_ALLOWED;
                } else if (e.getCause() instanceof BadRequestException) {
                    return HTTPStatus.BAD_REQUEST;
                } else if (e.hasMessage() &&
                           e.getMessage().contains(bindingConstants.getErrorMessageWrongContentType())) {
                    return HTTPStatus.UNSUPPORTED_MEDIA_TYPE;
                } else if (e.hasMessage() &&
                           e.getMessage().contains(bindingConstants.getErrorMessageWrongContentTypeInAcceptHeader())) {
                    return HTTPStatus.NOT_ACCEPTABLE;
                } else if (e.hasMessage() &&
                           e.getMessage().contains("HTTP method") &&
                           e.getMessage().contains("not allowed")) {
                    return HTTPStatus.METHOD_NOT_ALLOWED;
                }
            } else if (e.getCode().equals(OwsExceptionCode.InvalidParameterValue)) {
                return HTTPStatus.BAD_REQUEST;
            }
        }
        return HTTPStatus.INTERNAL_SERVER_ERROR;
    }

    private ServiceResponse handleRequest(HttpServletRequest request,
                                          HttpServletResponse response)
            throws OwsExceptionReport {
        ServiceResponse serviceResponse;
        LOGGER.debug("Start handling of REST request. URI:{}",
                     request.getRequestURI());
        // Decode the request
        final RestRequest restRequest = decodeHttpRequest(request);
        LOGGER.debug("Rest request decoded to {}",
                     restRequest != null ? restRequest.getClass().getName() : null);
        // Handle the request
        final RestResponse restResponse = handleRestRequest(restRequest);
        LOGGER.debug("Rest request handled. DeleteObservationResponse received: {}",
                     restResponse.getClass().getName());
        // Encode the response
        serviceResponse = encodeRestResponse(restResponse);
        LOGGER.debug("Rest response encoded. DeleteObservationResponse received: {}",
                     response != null ? response.getClass().getName() : null);

        LOGGER.debug("Handling of REST request finished. Returning response to web tier");
        return serviceResponse;
    }

}
