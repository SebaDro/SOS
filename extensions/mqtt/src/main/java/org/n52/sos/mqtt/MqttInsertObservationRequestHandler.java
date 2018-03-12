/*
 * Copyright (C) 2018 52°North Initiative for Geospatial Open Source Software GmbH
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
package org.n52.sos.mqtt;

import org.n52.iceland.exception.ows.concrete.InvalidAcceptVersionsParameterException;
import org.n52.iceland.exception.ows.concrete.InvalidServiceOrVersionException;
import org.n52.iceland.exception.ows.concrete.InvalidServiceParameterException;
import org.n52.iceland.exception.ows.concrete.VersionNotSupportedException;
import org.n52.iceland.service.operator.ServiceOperator;
import org.n52.iceland.service.operator.ServiceOperatorRepository;
import org.n52.shetland.ogc.ows.exception.CompositeOwsException;
import org.n52.shetland.ogc.ows.exception.MissingServiceParameterException;
import org.n52.shetland.ogc.ows.exception.MissingVersionParameterException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceKey;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.sos.cache.SosContentCache;
import org.n52.sos.service.Configurator;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class MqttInsertObservationRequestHandler {
    
     private SosContentCache getCache() {
        return Configurator.getInstance().getCache();
    }

    protected boolean isProcedureRegistered(String procedure) {
        return getCache().getProcedures().contains(procedure);
    }

    protected ServiceOperator getServiceOperator(OwsServiceKey sokt) throws OwsExceptionReport {
        return getServiceOperatorRepository().getServiceOperator(sokt);
    }

    protected ServiceOperator getServiceOperator(OwsServiceRequest request) throws OwsExceptionReport {
        checkServiceOperatorKeyTypes(request);
        for (OwsServiceKey sokt : request.getServiceOperatorKeys()) {
            ServiceOperator so = getServiceOperator(sokt);
            if (so != null) {
                return so;
            }
        }
        throw new InvalidServiceOrVersionException(request.getService(), request.getVersion());
    }

    protected void checkServiceOperatorKeyTypes(OwsServiceRequest request) throws OwsExceptionReport {
        CompositeOwsException exceptions = new CompositeOwsException();
        for (OwsServiceKey sokt : request.getServiceOperatorKeys()) {
            if (sokt.hasService()) {
                if (sokt.getService().isEmpty()) {
                    exceptions.add(new MissingServiceParameterException());
                } else if (!getServiceOperatorRepository().isServiceSupported(sokt.getService())) {
                    exceptions.add(new InvalidServiceParameterException(sokt.getService()));
                }
            }
            if (request instanceof GetCapabilitiesRequest) {
                GetCapabilitiesRequest gcr = (GetCapabilitiesRequest) request;
                if (gcr.isSetAcceptVersions()) {
                    boolean hasSupportedVersion = false;
                    for (String acceptVersion : gcr.getAcceptVersions()) {
                        if (isVersionSupported(request.getService(), acceptVersion)) {
                            hasSupportedVersion = true;
                        }
                    }
                    if (!hasSupportedVersion) {
                        exceptions.add(new InvalidAcceptVersionsParameterException(gcr.getAcceptVersions()));
                    }
                }
            } else if (sokt.hasVersion()) {
                if (sokt.getVersion().isEmpty()) {
                    exceptions.add(new MissingVersionParameterException());
                } else if (!isVersionSupported(sokt.getService(), sokt.getVersion())) {
                    exceptions.add(new VersionNotSupportedException());
                }
            }
        }
        exceptions.throwIfNotEmpty();
    }

    protected boolean isVersionSupported(String service, String acceptVersion) {
        return getServiceOperatorRepository().isVersionSupported(service, acceptVersion);
    }

    protected boolean isServiceSupported(String service) {
        return getServiceOperatorRepository().isServiceSupported(service);
    }

    protected ServiceOperatorRepository getServiceOperatorRepository() {
        return ServiceOperatorRepository.getInstance();
    }
    
}
