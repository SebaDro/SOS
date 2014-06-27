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
import org.envirocar.server.mongo.entity.MongoSensor;
import org.n52.sos.ds.envirocar.cache.AbstractEnvirCarQueueingDatasourceCacheUpdate;
import org.n52.sos.util.CacheHelper;
import org.n52.sos.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class EnviroCarOfferingCacheUpdate extends
        AbstractEnvirCarQueueingDatasourceCacheUpdate<EnviroCarOfferingCacheUpdateTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnviroCarOfferingCacheUpdate.class);

    private static final String THREAD_GROUP_NAME = "offering-cache-update";

    Collection<String> offeringsIdToUpdate = Sets.newHashSet();

    private Set<Sensor> offeringsToUpdate = Sets.newHashSet();

    Map<String, Collection<String>> offObsPropMap;

    public EnviroCarOfferingCacheUpdate(int threads) {
        super(threads, THREAD_GROUP_NAME);
    }

    public EnviroCarOfferingCacheUpdate(int threads, Collection<String> offeringIdsToUpdate) {
        super(threads, THREAD_GROUP_NAME);
        this.offeringsIdToUpdate = offeringIdsToUpdate;
    }

    private Sensors getOfferingsToUpdate() {
        return getEnviroCarDaoFactory().getSensorDAO().get(new Pagination());
    }

    @Override
    public void execute() {
        LOGGER.debug("Executing EnviroCarOfferingCacheUpdate (Single Threaded Tasks)");
        startStopwatch();
        // time ranges
        MongoTrackDao dao = (MongoTrackDao) getEnviroCarDaoFactory().getTrackDAO();
        Map<String, TimeExtrema> ote = dao.getOfferingTimeExtrema();
        if (ote != null) {
            for (Sensor sensor : getOfferingsToUpdate()) {
                if (shouldOfferingBeProcessed(sensor, ote)) {
                    String offeringId = sensor.getIdentifier();
                    String prefixedOfferingId = CacheHelper.addPrefixOrGetOfferingIdentifier(offeringId);
                    getCache().addOffering(prefixedOfferingId);
                    getCache().setNameForOffering(prefixedOfferingId, getOfferingName(prefixedOfferingId, sensor));
                    if (ote.containsKey(offeringId)) {
                        TimeExtrema te = ote.get(offeringId);
                        getCache().setMinPhenomenonTimeForOffering(offeringId, te.getMinPhenomenonTime());
                        getCache().setMaxPhenomenonTimeForOffering(offeringId, te.getMaxPhenomenonTime());
                        getCache().setMinResultTimeForOffering(offeringId, te.getMinResultTime());
                        getCache().setMaxResultTimeForOffering(offeringId, te.getMaxResultTime());
                    }
                    // procedures
                    getCache().setProceduresForOffering(offeringId, Sets.newHashSet(offeringId));
                }
            }
        }
        LOGGER.debug("Finished executing EnviroCarOfferingCacheUpdate (Single Threaded Tasks) ({})",
                getStopwatchResult());

        // execute multi-threaded updates
        LOGGER.debug("Executing EnviroCarOfferingCacheUpdate (Multi-Threaded Tasks)");
        startStopwatch();
        super.execute();
        LOGGER.debug("Finished executing EnviroCarOfferingCacheUpdate (Multi-Threaded Tasks) ({})",
                getStopwatchResult());
    }

    private String getOfferingName(String prefixedOfferingId, Sensor sensor) {
        StringBuilder builder = new StringBuilder("Timeseries for ");
        if (sensor.hasProperties()) {
            Map<String, Object> properties = sensor.getProperties();
            if (properties.containsKey(MongoSensor.PROPERTY_MODEL)
                    && properties.containsKey(MongoSensor.PROPERTY_MANUFACTURER)) {
                if (properties.containsKey(MongoSensor.PROPERTY_CONSTRUCTION_YEAR)) {
                    builder.append(properties.get(MongoSensor.PROPERTY_CONSTRUCTION_YEAR))
                            .append(Constants.BLANK_CHAR);
                }
                builder.append(properties.get(MongoSensor.PROPERTY_MANUFACTURER)).append(Constants.BLANK_CHAR);
                builder.append(properties.get(MongoSensor.PROPERTY_MODEL));
            } else {
                builder.append(sensor.getIdentifier());
            }
        } else {
            builder.append(sensor.getIdentifier());
        }
        return builder.toString();
    }

    @Override
    protected EnviroCarOfferingCacheUpdateTask[] getUpdatesToExecute() {
        Collection<EnviroCarOfferingCacheUpdateTask> offeringUpdateTasks = Lists.newArrayList();
        for (Sensor sensor : offeringsToUpdate) {
            offeringUpdateTasks.add(new EnviroCarOfferingCacheUpdateTask(sensor, getOfferingObservablePropertyMap()
                    .get(sensor.getIdentifier())));
        }
        return offeringUpdateTasks.toArray(new EnviroCarOfferingCacheUpdateTask[offeringUpdateTasks.size()]);
    }

    protected boolean shouldOfferingBeProcessed(Sensor sensor, Map<String, TimeExtrema> ote) {
        if (ote.containsKey(sensor.getIdentifier())) {
            offeringsToUpdate.add(sensor);
            return true;
        }
        return false;
    }

    private Map<String, Collection<String>> getOfferingObservablePropertyMap() {
        if (offObsPropMap == null) {
            offObsPropMap =
                    ((MongoPhenomenonDao) getEnviroCarDaoFactory().getPhenomenonDAO()).getSensorPhenomenonsMap();
        }
        return offObsPropMap;
    }

}
