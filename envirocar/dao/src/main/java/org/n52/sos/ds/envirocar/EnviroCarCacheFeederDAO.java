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

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.core.filter.SensorFilter;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.n52.sos.cache.WritableContentCache;
import org.n52.sos.ds.CacheFeederDAO;
import org.n52.sos.ds.EnviroCarConstants;
import org.n52.sos.ds.envirocar.cache.EnviroCarInitialCacheUpdate;
import org.n52.sos.ogc.ows.CompositeOwsException;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.util.CollectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public class EnviroCarCacheFeederDAO extends EnviroCarDaoFactoryHolder implements CacheFeederDAO {


    private static final Logger LOGGER = LoggerFactory.getLogger(EnviroCarCacheFeederDAO.class);

    /**
     * Defines the number of threads available in the thread pool of the cache
     * update executor service.
     */
    private int cacheThreadCount = 5;

    public int getCacheThreadCount() {
        return cacheThreadCount;
    }


    @Override
    public void updateCache(WritableContentCache cache) throws OwsExceptionReport {
        checkCacheNotNull(cache);
        List<OwsExceptionReport> errors = CollectionHelper.synchronizedList();
        EnviroCarDaoFactory enviroCarDaoFactory = null;
        try {
            EnviroCarInitialCacheUpdate update = new EnviroCarInitialCacheUpdate(getCacheThreadCount());
            enviroCarDaoFactory = getEnviroCarDaoFactory();
            update.setCache(cache);
            update.setErrors(errors);
            update.setEnviroCarDaoFactory(enviroCarDaoFactory);

            LOGGER.info("Starting cache update");
            long cacheUpdateStartTime = System.currentTimeMillis();

            update.execute();

            logCacheLoadTime(cacheUpdateStartTime);
        } finally {
            returnEnviroCarDaoFactory(enviroCarDaoFactory);
        }
        if (!errors.isEmpty()) {
            throw new CompositeOwsException(errors);
        }
    }

    @Override
    public void updateCacheOfferings(WritableContentCache cache, Collection<String> offeringsNeedingUpdate)
            throws OwsExceptionReport {
        checkCacheNotNull(cache);
//        if (CollectionHelper.isEmpty(offeringsNeedingUpdate)) {
//            return;
//        }
//        List<OwsExceptionReport> errors = CollectionHelper.synchronizedList();
//        Session session = getSession();
//        EnviroCarOfferingCacheUpdate update = new EnviroCarOfferingCacheUpdate(getCacheThreadCount(), offeringsNeedingUpdate);
//        update.setCache(cache);
//        update.setErrors(errors);
//        update.setSession(session);
//        
//        LOGGER.info("Starting offering cache update for {} offering(s)", offeringsNeedingUpdate.size());
//        long cacheUpdateStartTime = System.currentTimeMillis();
//
//        try {
//            update.execute();
//        } catch (HibernateException he) {
//            LOGGER.error("Error while updating ContentCache!", he);
//        } finally {
//            returnSession(session);
//        }
//
//        logCacheLoadTime(cacheUpdateStartTime);
//
//        if (!errors.isEmpty()) {
//            throw new CompositeOwsException(errors);
//        }
    }

    private void checkCacheNotNull(WritableContentCache cache) {
        if (cache == null) {
            throw new NullPointerException("cache is null");
        }        
    }

    private void logCacheLoadTime(long startTime) {
        Period cacheLoadPeriod = new Period(startTime, System.currentTimeMillis());
        LOGGER.info("Cache load finished in {} ({} seconds)",
                PeriodFormat.getDefault().print(cacheLoadPeriod.normalizedStandard()),
                cacheLoadPeriod.toStandardSeconds());
    }

    @Override
    public String getDatasourceDaoIdentifier() {
        return EnviroCarConstants.ENVIROCAR_DATASOURCE_DAO_IDENTIFIER;
    }


//    private void getOfferingsAndProcedures(WritableContentCache cache) throws OwsExceptionReport {
//        SensorDao sensorDAO = getEnviroCarDaoFactory().getSensorDAO();
//        Set<String> s = Sets.newHashSet(sensorDAO.getTypes());
//        cache.setOfferings(s);
//        cache.setProcedures(s);
//        for (String offProc : s) {
//            cache.setNameForOffering(offProc, offProc);
//            
//            // procedures
//            cache.setProceduresForOffering(offProc, Sets.newHashSet(offProc));
//            cache.setOfferingsForProcedure(offProc, Sets.newHashSet(offProc));
//            
//            // Observable properties
//            Sensors sensors = getSensors(offProc, getEnviroCarDaoFactory());
//            Collection<String> observablePropertyIdentifier = getObservablePropertyIdentifier(sensors, getEnviroCarDaoFactory());
//            cache.setObservablePropertiesForOffering(offProc, observablePropertyIdentifier);
//            cache.setObservablePropertiesForProcedure(offProc, observablePropertyIdentifier);
//
//            // Observation types
//            cache.setObservationTypesForOffering(offProc, Sets.newHashSet(OmConstants.OBS_TYPE_MEASUREMENT));
//
//            // Features of Interest
//            List<String> featureOfInterestIdentifiers = Lists.newArrayList();
//            Tracks tracks = getEnviroCarDaoFactory().getTrackDAO().get(new TrackFilter().setSensorType(offProc));
//            for (Track track : tracks) {
//                featureOfInterestIdentifiers.add(track.getIdentifier());
//            }
//            cache.setFeaturesOfInterestForOffering(offProc, featureOfInterestIdentifiers);
//            cache.setFeatureOfInterestTypesForOffering(offProc, Sets.newHashSet(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE));
//            
            // Spatial Envelope
//            SosEnvelope envelopeForOffering = getEnvelopeForOffering(featureOfInterestIdentifiers, getEnviroCarDaoFactory());
//            cache.setEnvelopeForOffering(offProc, envelopeForOffering);
//            cache.setGlobalEnvelope(envelopeForOffering);
            
            // Spatial Filtering Profile Spatial Envelope
//            cache.setSpatialFilteringProfileEnvelopeForOffering(offProc, envelopeForOffering);
//            
//            MongoTrackDao dao = (MongoTrackDao)getEnviroCarDaoFactory().getTrackDAO();
//            
//            AggregationOutput ao = dao.getTimeExtrema();
//            for (DBObject r : ao.results()) {
//                cache.setMinPhenomenonTimeForOffering(offProc, getDateTime(r.get("phenStart")));
//                cache.setMaxPhenomenonTimeForOffering(offProc, getDateTime(r.get("phenEnd")));
//                cache.setMinResultTimeForOffering(offProc, getDateTime(r.get("resultStart")));
//                cache.setMaxResultTimeForOffering(offProc, getDateTime(r.get("resultEnd")));
//                
//                cache.setMinPhenomenonTimeForProcedure(offProc, getDateTime(r.get("phenStart")));
//                cache.setMaxPhenomenonTimeForProcedure(offProc, getDateTime(r.get("phenEnd")));
//                
//                cache.setMinPhenomenonTime(getDateTime(r.get("phenStart")));
//                cache.setMaxPhenomenonTime(getDateTime(r.get("phenEnd")));
//                
//                cache.setMinResultTime(getDateTime(r.get("resultStart")));
//                cache.setMaxResultTime(getDateTime(r.get("resultEnd")));
//                
//            }
//    
//        }
//        // TODO requires functionality in the enviroCar-server#
//        cache.setGlobalEnvelope(getEnvelopeForOffering(getEnviroCarDaoFactory().getTrackDAO().getIdentifier(), getEnviroCarDaoFactory()));
////        cache.setGlobalEnvelope(new SosEnvelope(new Envelope(-90, 90, -180, 180), 4326));
//
//        
//    }


    private DateTime getDateTime(Object object) {
           return new DateTime(object); 
    }
    
    private Sensors getSensors(String offering, EnviroCarDaoFactory enviroCarDaoFactory) {
       return enviroCarDaoFactory.getSensorDAO().get(new SensorFilter(offering, null, null));
    }


    private Collection<String> getObservablePropertyIdentifier(Sensors sensors, EnviroCarDaoFactory enviroCarDaoFactory) {
        Set<String> ops = Sets.newHashSet(enviroCarDaoFactory.getPhenomenonDAO().getIdentifier());
//        Phenomenons phenomenons = enviroCarDaoFactory.getPhenomenonDAO().get(new PhenomenonFilter().setSensors(sensors));
//        for (Phenomenon phenomenon : phenomenons) {
//            ops.add(phenomenon.getName());
//        }
        return ops;
    }


    private void getObservableProperties(WritableContentCache cache) {
//        List<ObservableProperty> ops = new ObservablePropertyDAO().getObservablePropertyObjects(getSession());
//        //if ObservationConstellation is supported load them all at once, otherwise query obs directly
//        if (HibernateHelper.isEntitySupported(ObservationConstellation.class, getSession())) {
//            Map<String, Collection<ObservationConstellationInfo>> ociMap = ObservationConstellationInfo.mapByObservableProperty(
//                    new ObservationConstellationDAO().getObservationConstellationInfo(getSession()));
//            for (ObservableProperty op : ops) {
//                final String obsPropIdentifier = op.getIdentifier();
//                Collection<ObservationConstellationInfo> ocis = ociMap.get(obsPropIdentifier);
//                if (CollectionHelper.isNotEmpty(ocis)) {
//                    getCache().setOfferingsForObservableProperty(obsPropIdentifier,
//                            DatasourceCacheUpdateHelper.getAllOfferingIdentifiersFromObservationConstellationInfos(ocis));
//                    getCache().setProceduresForObservableProperty(obsPropIdentifier,
//                            DatasourceCacheUpdateHelper.getAllProcedureIdentifiersFromObservationConstellationInfos(ocis));
//                }
//            }
//        } else {
//            for (ObservableProperty op : ops) {
//                final String obsPropIdentifier = op.getIdentifier();
//                try {
//                    getCache().setOfferingsForObservableProperty(obsPropIdentifier,
//                            new OfferingDAO().getOfferingIdentifiersForObservableProperty(obsPropIdentifier, getSession()));
//                } catch (CodedException e) {
//                    getErrors().add(e);
//                }
//                getCache().setProceduresForObservableProperty(obsPropIdentifier,
//                        new ProcedureDAO().getProcedureIdentifiersForObservableProperty(obsPropIdentifier, getSession()));                
//            }
//        }
    }


    private void getFeatureOfInterest(WritableContentCache cache) throws OwsExceptionReport {
        cache.setFeaturesOfInterest(getEnviroCarDaoFactory().getTrackDAO().getIdentifier());
//        getCache().setProceduresForFeatureOfInterest(featureOfInterestIdentifier,
//                procsForAllFois.get(featureOfInterestIdentifier));
        
    }
    
}
