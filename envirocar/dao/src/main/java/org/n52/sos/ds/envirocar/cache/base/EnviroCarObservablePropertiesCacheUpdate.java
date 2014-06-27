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
package org.n52.sos.ds.envirocar.cache.base;

import java.util.Collection;
import java.util.Map;

import javax.management.monitor.MonitorSettingException;

import org.envirocar.server.core.dao.SensorDao;
import org.envirocar.server.mongo.dao.MongoSensorDao;
import org.n52.sos.ds.envirocar.cache.AbstractEnviroCarThreadableDatasourceCacheUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnviroCarObservablePropertiesCacheUpdate extends AbstractEnviroCarThreadableDatasourceCacheUpdate {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnviroCarObservablePropertiesCacheUpdate.class);

    @Override
    public void execute() {
        LOGGER.debug("Executing EnviroCarObservablePropertiesCacheUpdate");
        startStopwatch();
          Collection<String> ops = getEnviroCarDaoFactory().getPhenomenonDAO().getIdentifier();
          Map<String, Collection<String>> phenomenonSensorsMap = ((MongoSensorDao)getEnviroCarDaoFactory().getSensorDAO()).getPhenomenonSensorsMap();
            for (String obsPropIdentifier : ops) {
                 if (phenomenonSensorsMap.containsKey(obsPropIdentifier)) {
                     getCache().setOfferingsForObservableProperty(obsPropIdentifier, phenomenonSensorsMap.get(obsPropIdentifier));
                     getCache().setProceduresForObservableProperty(obsPropIdentifier, phenomenonSensorsMap.get(obsPropIdentifier));
                 }
            }
        LOGGER.debug("Executing EnviroCarObservablePropertiesCacheUpdate ({})", getStopwatchResult());
    }
}