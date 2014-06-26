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

import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.core.filter.PhenomenonFilter;
import org.envirocar.server.mongo.dao.MongoPhenomenonDao;
import org.envirocar.server.mongo.dao.MongoTrackDao;
import org.envirocar.server.mongo.dao.MongoTrackDao.TimeExtrema;
import org.n52.sos.ds.envirocar.cache.AbstractEnviroCarThreadableDatasourceCacheUpdate;

import com.google.common.collect.Sets;

public class EnviroCarProcedureCacheUpdateTask extends AbstractEnviroCarThreadableDatasourceCacheUpdate {

    private Sensor sensor;
    
    public EnviroCarProcedureCacheUpdateTask(Sensor sensor) {
        this.sensor = sensor;
    }
    
    @Override
    public void execute() {
        String procedureId = sensor.getIdentifier();
        getCache().addProcedure(procedureId);
        getCache().setOfferingsForProcedure(procedureId, Sets.newHashSet(procedureId));
        Collection<Object> fromMeasurments =
                ((MongoPhenomenonDao) getEnviroCarDaoFactory().getPhenomenonDAO())
                        .getFromMeasurments(new PhenomenonFilter().setSensors(Sensors.from(Sets.newHashSet(sensor))
                                .build()));
//        getCache().setObservablePropertiesForProcedure(procedureIdentifier, DatasourceCacheUpdateHelper
//                .getAllObservablePropertyIdentifiersFromObservationConstellationInfos(ocis));
//        
        
        MongoTrackDao dao = (MongoTrackDao) getEnviroCarDaoFactory().getTrackDAO();
        TimeExtrema ote = dao.getTimeExtrema(procedureId);
        if (ote != null) {
            getCache().setMinPhenomenonTimeForProcedure(procedureId, ote.getMinPhenomenonTime());
            getCache().setMaxPhenomenonTimeForProcedure(procedureId, ote.getMaxPhenomenonTime());
        }
    }

}
