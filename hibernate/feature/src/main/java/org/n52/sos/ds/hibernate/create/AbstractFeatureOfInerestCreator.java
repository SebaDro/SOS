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
package org.n52.sos.ds.hibernate.create;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.n52.series.db.beans.AbstractFeatureEntity;
import org.n52.series.db.beans.FeatureEntity;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.sos.ds.hibernate.util.FeatureParameterAdder;
import org.n52.sos.util.SosHelper;

public abstract class AbstractFeatureOfInerestCreator<T extends FeatureEntity> extends AbstractFeatureCreator<T> {

        public AbstractFeatureOfInerestCreator(FeatureVisitorContext context) {
        super(context);
    }

        public AbstractFeature createFeature(FeatureEntity f) throws OwsExceptionReport {
            final CodeWithAuthority identifier = getIdentifier(f);
            if (!SosHelper.checkFeatureOfInterestIdentifierForSosV2(f.getIdentifier(), getContext().getVersion())) {
                identifier.setValue(null);
            }
            final AbstractFeature absFeat = getFeatureType(identifier);
            addNameAndDescription(getContext().getRequestedLanguage(), f, absFeat);
            if (absFeat instanceof AbstractSamplingFeature) {
                AbstractSamplingFeature absSampFeat = (AbstractSamplingFeature) absFeat;
                absSampFeat.setGeometry(createGeometryFrom(f));
                absSampFeat.setFeatureType(f.getFeatureType().getFormat());
                absSampFeat.setUrl(f.getUrl());
                if (f.isSetXml()) {
                    absSampFeat.setXml(f.getXml());
                }
                addParameter(absSampFeat, f);
                final Set<FeatureEntity> parentFeatures = f.getParents();
                if (parentFeatures != null && !parentFeatures.isEmpty()) {
                    final List<AbstractFeature> sampledFeatures = new ArrayList<AbstractFeature>(parentFeatures.size());
                    for (final AbstractFeatureEntity parentFeature : parentFeatures) {
                        sampledFeatures.add(new HibernateFeatureVisitor(getContext()).visit(parentFeature));
                    }
                    absSampFeat.setSampledFeatures(sampledFeatures);
                }
            }
            return absFeat;
        }

        protected void addParameter(AbstractSamplingFeature absSampFeat, FeatureEntity f) throws OwsExceptionReport {
            new FeatureParameterAdder(absSampFeat, f).add();
        }

        protected abstract AbstractFeature getFeatureType(CodeWithAuthority identifier);
}
