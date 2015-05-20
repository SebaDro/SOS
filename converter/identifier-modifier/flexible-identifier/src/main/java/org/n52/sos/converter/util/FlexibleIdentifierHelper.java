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
package org.n52.sos.converter.util;

import java.util.concurrent.locks.ReentrantLock;

import org.n52.iceland.config.SettingsManager;
import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.ogc.ows.Extension;
import org.n52.iceland.ogc.ows.Extensions;
import org.n52.iceland.ogc.swe.simpleType.SweBoolean;
import org.n52.iceland.util.JavaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configurable
public class FlexibleIdentifierHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlexibleIdentifierHelper.class);

    public static final String RETURN_HUMAN_READABLE_IDENTIFIER = "returnHumanReadableIdentifier";

    private static FlexibleIdentifierHelper instance;

    private static ReentrantLock creationLock = new ReentrantLock();

    private boolean returnHumanReadableIdentifier = false;

    private boolean includeOffering = true;

    private boolean includeProcedure = true;

    private boolean includeObservableProperty = true;

    private boolean includeFeatureOfInterest = true;

    /**
     * Private constructor
     */
    private FlexibleIdentifierHelper() {
    }

    /**
     * @return Returns a singleton instance of the GeometryHandler.
     */
    public static FlexibleIdentifierHelper getInstance() {
        if (instance == null) {
            creationLock.lock();
            try {
                if (instance == null) {
                    // don't set instance before configuring, or other threads
                    // can get access to unconfigured instance!
                    final FlexibleIdentifierHelper newInstance = new FlexibleIdentifierHelper();
                    SettingsManager.getInstance().configure(newInstance);
                    instance = newInstance;
                }
            } finally {
                creationLock.unlock();
            }
        }
        return instance;
    }

    @Setting(FlexibleIdentifierSettings.RETURN_HUMAN_READABLE_IDENTIFIER_KEY)
    public void setReturnHumanReadableIdentifier(final boolean returnHumanReadableIdentifier)
            throws ConfigurationException {
        this.returnHumanReadableIdentifier = returnHumanReadableIdentifier;
    }

    public boolean isSetReturnHumanReadableIdentifier() {
        return returnHumanReadableIdentifier;
    }

    @Setting(FlexibleIdentifierSettings.INCLUDE_OFFERING_KEY)
    public void setIncludeOffering(final boolean includeOffering) throws ConfigurationException {
        this.includeOffering = includeOffering;
    }

    public boolean isSetIncludeOffering() {
        return includeOffering;
    }

    @Setting(FlexibleIdentifierSettings.INCLUDE_PROCEDURE_KEY)
    public void setIncludeProcedure(final boolean includeProcedure) throws ConfigurationException {
        this.includeProcedure = includeProcedure;
    }

    public boolean isSetIncludeProcedure() {
        return includeProcedure;
    }

    @Setting(FlexibleIdentifierSettings.INCLUDE_OBSERVABLE_PROPERTY_KEY)
    public void setIncludeObservableProperty(final boolean includeObservableProperty) throws ConfigurationException {
        this.includeObservableProperty = includeObservableProperty;
    }

    public boolean isSetIncludeObservableProperty() {
        return includeObservableProperty;
    }

    @Setting(FlexibleIdentifierSettings.INCLUDE_FEATURE_OF_INTEREST_KEY)
    public void setIncludeFeatureOfInterest(final boolean includeFeatureOfInterest) throws ConfigurationException {
        this.includeFeatureOfInterest = includeFeatureOfInterest;
    }

    public boolean isSetIncludeFeatureOfInterest() {
        return includeFeatureOfInterest;
    }

    public boolean checkIsReturnHumanReadableIdentifierFlagExtensionSet(Extensions extensions)
            throws InvalidParameterValueException {
        if (extensions != null && extensions.containsExtension(RETURN_HUMAN_READABLE_IDENTIFIER)) {
           Extension<?> extension = extensions.getExtension(RETURN_HUMAN_READABLE_IDENTIFIER);
            if (extension.getValue() instanceof SweBoolean) {
                return true;
            } else {
                throw new InvalidParameterValueException(RETURN_HUMAN_READABLE_IDENTIFIER,
                        JavaHelper.asString(extension.getValue()));
            }
        }
        return false;
    }

    public boolean checkForReturnHumanReadableIdentifierFlagExtension(Extensions extensions)
            throws InvalidParameterValueException {
        if (checkIsReturnHumanReadableIdentifierFlagExtensionSet(extensions)) {
            return ((SweBoolean) extensions.getExtension(RETURN_HUMAN_READABLE_IDENTIFIER).getValue()).getValue();
        }
        return false;
    }

}