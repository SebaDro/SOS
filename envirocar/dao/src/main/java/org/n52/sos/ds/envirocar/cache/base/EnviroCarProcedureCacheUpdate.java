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
import java.util.Set;

import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.mongo.dao.MongoPhenomenonDao;
import org.envirocar.server.mongo.dao.MongoTrackDao;
import org.envirocar.server.mongo.dao.MongoTrackDao.TimeExtrema;
import org.n52.sos.ds.envirocar.cache.AbstractEnvirCarQueueingDatasourceCacheUpdate;
import org.n52.sos.ds.envirocar.cache.AbstractEnviroCarThreadableDatasourceCacheUpdate;
import org.n52.sos.util.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class EnviroCarProcedureCacheUpdate extends AbstractEnviroCarThreadableDatasourceCacheUpdate {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnviroCarProcedureCacheUpdate.class);
    
    @Override
    public void execute() {
            LOGGER.debug("Executing EnviroCarProcedureCacheUpdate (Single Threaded Tasks)");
            startStopwatch();
            
            // Observable properties
            Map<String, Collection<String>> obsPropsMap =
                    ((MongoPhenomenonDao) getEnviroCarDaoFactory().getPhenomenonDAO())
                            .getSensorPhenomenonsMap();
            // time ranges
            MongoTrackDao dao = (MongoTrackDao) getEnviroCarDaoFactory().getTrackDAO();
            Map<String, TimeExtrema> ote = dao.getOfferingTimeExtrema();
            if (ote != null) {
                for (Sensor sensor :  getProcedureToUpdate()){ 
                    if (shouldProcedureBeProcessed(sensor, ote, obsPropsMap)) {
                        String procedureId =sensor.getIdentifier();
                        String prefixedProcedureId = CacheHelper.addPrefixOrGetProcedureIdentifier(procedureId);
                        getCache().addProcedure(prefixedProcedureId);
                        if (ote.containsKey(procedureId)) {
                            TimeExtrema te = ote.get(procedureId);
                            getCache().setMinPhenomenonTimeForProcedure(procedureId, te.getMinPhenomenonTime());
                            getCache().setMaxPhenomenonTimeForProcedure(procedureId, te.getMaxPhenomenonTime());
                        }
                        // procedures
                        getCache().setOfferingsForProcedure(procedureId, Sets.newHashSet(procedureId));
                        getCache().setObservablePropertiesForProcedure(procedureId, obsPropsMap.get(procedureId));
                    }
                }
            }
            LOGGER.debug("Finished executing EnviroCarProcedureCacheUpdate (Single Threaded Tasks) ({})", getStopwatchResult());
    }

    
    private Sensors getProcedureToUpdate() {
        return getEnviroCarDaoFactory().getSensorDAO().get(new Pagination());
    }
    
    protected boolean shouldProcedureBeProcessed(Sensor sensor, Map<String, TimeExtrema> ote, Map<String, Collection<String>> obsPropsMap ) {
        if (ote.containsKey(sensor.getIdentifier()) && obsPropsMap.containsKey(sensor.getIdentifier())) {
            return true;
        }
        return false;
    }

}
