/**
 * Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.ds.envirocar;

import org.envirocar.server.core.entities.Sensor;
import org.n52.sos.ds.AbstractDescribeSensorDAO;
import org.n52.sos.ds.EnviroCarConstants;
import org.n52.sos.ds.envirocar.util.EnviroCarProcedureDescriptionGenerator;
import org.n52.sos.exception.ows.InvalidParameterValueException;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sensorML.SensorMLConstants;
import org.n52.sos.ogc.sos.Sos1Constants;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.ogc.sos.SosProcedureDescription;
import org.n52.sos.request.DescribeSensorRequest;
import org.n52.sos.response.DescribeSensorResponse;
import org.n52.sos.util.http.MediaType;

public class EnviroCarDescribeSensorDAO extends AbstractDescribeSensorDAO {

    private final EnviroCarDaoFactoryHolder daoFacHolder = new EnviroCarDaoFactoryHolder();

    public EnviroCarDescribeSensorDAO() {
        super(SosConstants.SOS);
    }

    @Override
    public String getDatasourceDaoIdentifier() {
        return EnviroCarConstants.ENVIROCAR_DATASOURCE_DAO_IDENTIFIER;
    }

    @Override
    public DescribeSensorResponse getSensorDescription(DescribeSensorRequest request) throws OwsExceptionReport {
        checkProcedureDescriptionFormat(request.getProcedureDescriptionFormat(), request.getVersion());
        EnviroCarDaoFactory enviroCarDaoFactory = null;
        try {
            final DescribeSensorResponse response = new DescribeSensorResponse();
            response.setService(request.getService());
            response.setVersion(request.getVersion());
            response.setOutputFormat(request.getProcedureDescriptionFormat());
            enviroCarDaoFactory = daoFacHolder.getEnviroCarDaoFactory();
            response.addSensorDescription(getProcedureDescription(request, enviroCarDaoFactory));
            return response;
        } finally {
            daoFacHolder.returnEnviroCarDaoFactory(enviroCarDaoFactory);
        }
    }

    private void checkProcedureDescriptionFormat(String procedureDescriptionFormat, String version) throws InvalidParameterValueException {
        if (!SensorMLConstants.NS_SML.equals(procedureDescriptionFormat)
                && SensorMLConstants.SENSORML_CONTENT_TYPE.equals(MediaType.parse(procedureDescriptionFormat))) {
            Enum<?> parameter = Sos2Constants.DescribeSensorParams.procedureDescriptionFormat;
            if (Sos1Constants.SERVICEVERSION.equals(version)) {
                parameter = Sos1Constants.DescribeSensorParams.outputFormat;
            }
            throw new InvalidParameterValueException(parameter, procedureDescriptionFormat);
        }
    }

    private SosProcedureDescription getProcedureDescription(DescribeSensorRequest request,
            EnviroCarDaoFactory enviroCarDaoFactory) throws OwsExceptionReport {
        Sensor sensor = enviroCarDaoFactory.getSensorDAO().getByIdentifier(request.getProcedure());
        return new EnviroCarProcedureDescriptionGenerator(sensor, enviroCarDaoFactory).create();
    }

}
