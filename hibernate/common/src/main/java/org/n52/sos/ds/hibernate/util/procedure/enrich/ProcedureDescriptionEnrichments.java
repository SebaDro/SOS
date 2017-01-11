/*
 * Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.ds.hibernate.util.procedure.enrich;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Session;

import org.n52.iceland.util.LocalizedProducer;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.ows.OwsServiceProvider;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.sos.ds.hibernate.entities.Procedure;
import org.n52.sos.ds.hibernate.util.procedure.HibernateProcedureConverter;
import org.n52.sos.ds.procedure.enrich.AbstractProcedureDescriptionEnrichments;
import org.n52.sos.ds.procedure.enrich.AbstractRelatedProceduresEnrichment;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class ProcedureDescriptionEnrichments extends AbstractProcedureDescriptionEnrichments<Procedure>  {

    public ProcedureDescriptionEnrichments(Locale locale, LocalizedProducer<OwsServiceProvider> serviceProvider) {
        super(locale, serviceProvider);
    }

    public AbstractRelatedProceduresEnrichment createRelatedProceduresEnrichment() {
        return setValues(new RelatedProceduresEnrichment())
                .setConverter(getConverter())
                .setProcedure(getProcedure())
                .setProcedureDescriptionFormat(getProcedureDescriptionFormat())
                .setValidTime(getValidTime())
                .setI18NDAORepository(getI18NDAORepository());
    }
}
