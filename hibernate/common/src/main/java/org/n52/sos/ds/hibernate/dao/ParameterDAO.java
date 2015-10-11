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
package org.n52.sos.ds.hibernate.dao;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.n52.sos.ds.hibernate.entities.HibernateRelations.HasUnit;
import org.n52.sos.ds.hibernate.entities.HibernateRelations.HasValue;
import org.n52.sos.ds.hibernate.entities.Offering;
import org.n52.sos.ds.hibernate.entities.TOffering;
import org.n52.sos.ds.hibernate.entities.Unit;
import org.n52.sos.ds.hibernate.entities.parameter.Parameter;
import org.n52.sos.ds.hibernate.entities.parameter.ParameterFactory;
import org.n52.sos.ds.hibernate.util.HibernateHelper;
import org.n52.sos.exception.ows.NoApplicableCodeException;
import org.n52.sos.ogc.gml.ReferenceType;
import org.n52.sos.ogc.om.NamedValue;
import org.n52.sos.ogc.om.values.BooleanValue;
import org.n52.sos.ogc.om.values.CategoryValue;
import org.n52.sos.ogc.om.values.ComplexValue;
import org.n52.sos.ogc.om.values.CountValue;
import org.n52.sos.ogc.om.values.GeometryValue;
import org.n52.sos.ogc.om.values.HrefAttributeValue;
import org.n52.sos.ogc.om.values.NilTemplateValue;
import org.n52.sos.ogc.om.values.QuantityValue;
import org.n52.sos.ogc.om.values.ReferenceValue;
import org.n52.sos.ogc.om.values.SweDataArrayValue;
import org.n52.sos.ogc.om.values.TVPValue;
import org.n52.sos.ogc.om.values.TextValue;
import org.n52.sos.ogc.om.values.UnknownValue;
import org.n52.sos.ogc.om.values.Value;
import org.n52.sos.ogc.om.values.visitor.ValueVisitor;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.util.JavaHelper;
import org.n52.sos.w3c.xlink.W3CHrefAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;


/**
 * Hibernate DAO class to om:pramameter
 * 
 * @since 4.0.0
 * 
 */
public class ParameterDAO {
    
    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(ParameterDAO.class);

    public Set<Parameter<?>> getOrInsertParameters(Collection<NamedValue<?>> parameters,
            Map<String, Unit> units, Session session) throws OwsExceptionReport {
        Set<Parameter<?>> params = Sets.newHashSetWithExpectedSize(parameters.size());
        for (NamedValue<?> namedValue : parameters) {
            Parameter<?> insertParameter = getOrInsertParameter(namedValue, units, session);
            if (insertParameter != null && insertParameter instanceof Parameter) {
                params.add(insertParameter);
            }
        }
        return params;
    }

    public Parameter<?> getOrInsertParameter(NamedValue<?> parameter, Map<String, Unit> unitCache, Session session)
            throws OwsExceptionReport {
        if (!Sos2Constants.HREF_PARAMETER_SPATIAL_FILTERING_PROFILE.equals(parameter.getName().getHref())) {
            Parameter<?> param = getParameter(parameter, session);
            if (param == null) {
                ParameterPersister persister = new ParameterPersister(this, parameter, unitCache, session);
                return parameter.getValue().accept(persister);
            }
            return param;
        }
        return null;
    }
    
    public Parameter<?> getParameter(NamedValue<?> parameter, Session session) {
        Criteria c = session.createCriteria(Parameter.class)
                .add(Restrictions.eq(Parameter.NAME, parameter.getName().getHref()))
                .add(Restrictions.eq(Parameter.VALUE, getValue(parameter)));
        log.debug("QUERY getParameter(): {}", HibernateHelper.getSqlString(c));
        return (Parameter<?>) c.uniqueResult();
    }

    private String getValue(NamedValue<?> parameter) {
        Object value = parameter.getValue().getValue();
        if (value instanceof ReferenceType) {
            return ((ReferenceType) value).getHref();
        } else if (value instanceof W3CHrefAttribute) {
            return ((W3CHrefAttribute) value).getHref();
        }
        return JavaHelper.asString(value);
    }

    /**
     * If the local unit cache isn't null, use it when retrieving unit.
     *
     * @param unit
     *            Unit
     * @param localCache
     *            Cache (possibly null)
     * @param session
     * @return Unit
     */
    protected Unit getUnit(String unit, Map<String, Unit> localCache, Session session) {
        if (localCache != null && localCache.containsKey(unit)) {
            return localCache.get(unit);
        } else {
            // query unit and set cache
            Unit hUnit = new UnitDAO().getOrInsertUnit(unit, session);
            if (localCache != null) {
                localCache.put(unit, hUnit);
            }
            return hUnit;
        }
    }

    public ParameterFactory getParameterFactory() {
        return ParameterFactory.getInstance();
    }

    public static class ParameterPersister implements ValueVisitor<Parameter<?>> {
        private final Caches caches;
        private final Session session;
        private final NamedValue<?> namedValue;
        private final DAOs daos;
        private final ParameterFactory parameterFactory;

        public ParameterPersister(ParameterDAO parameterDAO, NamedValue<?> namedValue, Map<String, Unit> unitCache, Session session) {
            this(new DAOs(parameterDAO),
                    new Caches(unitCache),
                    namedValue,
                    session);
        }
        
        public ParameterPersister(DAOs daos, Caches caches, NamedValue<?> namedValue, Session session) {
            this.caches = caches;
            this.session = session;
            this.daos = daos;
            this.namedValue = namedValue;
            this.parameterFactory = daos.parameter.getParameterFactory();
        }

        private static class Caches {
            private final Map<String, Unit> units;

            Caches( Map<String, Unit> units) {
                this.units = units;
            }

            public Map<String, Unit> units() {
                return units;
            }
        }
    
        private static class DAOs {
            private final ParameterDAO parameter;

            DAOs(ParameterDAO parameter) {
                this.parameter = parameter;
            }

            public ParameterDAO parameter() {
                return this.parameter;
            }
        }
        
        private <V, T extends Parameter<V>> T addUnitAnd(T parameter, Value<V> value) throws OwsExceptionReport {
            if (parameter instanceof HasUnit) {
                ((HasUnit)parameter).setUnit(getUnit(value));
            }
            return parameter;
        }
        
        private Unit getUnit(Value<?> value) {
            return value.isSetUnit() ? daos.parameter().getUnit(value.getUnit(), caches.units(), session) : null;
        }
        
        private <V, T extends Parameter<V>> T persist(T parameter, Value<V> value) throws OwsExceptionReport {
            return persist(parameter, value.getValue().toString());
        }
        
        private <V, T extends Parameter<V>> T persist(T parameter, String value) throws OwsExceptionReport {
            if (parameter instanceof HasUnit && !((HasUnit)parameter).isSetUnit()) {
                ((HasUnit)parameter).setUnit(getUnit(namedValue.getValue()));
            }
            parameter.setName(namedValue.getName().getHref());
            parameter.setValue(value);
            session.saveOrUpdate(parameter);
            session.flush();
            session.refresh(parameter);
            return parameter;
        }

        @Override
        public Parameter<?> visit(BooleanValue value) throws OwsExceptionReport {
            return persist(parameterFactory.truth(), Boolean.toString(value.getValue()));
        }

        @Override
        public Parameter<?> visit(CategoryValue value) throws OwsExceptionReport {
            return persist(addUnitAnd(parameterFactory.category(), value), value.getValue());
        }

        @Override
        public Parameter<?> visit(ComplexValue value) throws OwsExceptionReport {
            throw notSupported(value);
        }

        @Override
        public Parameter<?> visit(CountValue value) throws OwsExceptionReport {
            return persist(parameterFactory.count(), Integer.toString(value.getValue()));
        }

        @Override
        public Parameter<?> visit(GeometryValue value) throws OwsExceptionReport {
            throw notSupported(value);
        }

        @Override
        public Parameter<?> visit(HrefAttributeValue value) throws OwsExceptionReport {
            throw notSupported(value);
        }

        @Override
        public Parameter<?> visit(NilTemplateValue value) throws OwsExceptionReport {
            throw notSupported(value);
        }

        @Override
        public Parameter<?> visit(QuantityValue value) throws OwsExceptionReport {
            return persist(addUnitAnd(parameterFactory.quantity(), value), Double.toString(value.getValue()));
        }

        @Override
        public Parameter<?> visit(ReferenceValue value) throws OwsExceptionReport {
            return persist(parameterFactory.category(), value.getValue().getHref());
        }

        @Override
        public Parameter<?> visit(SweDataArrayValue value) throws OwsExceptionReport {
            throw notSupported(value);
        }

        @Override
        public Parameter<?> visit(TVPValue value) throws OwsExceptionReport {
            throw notSupported(value);
        }

        @Override
        public Parameter<?> visit(TextValue value) throws OwsExceptionReport {
            return persist(parameterFactory.text(), value);
        }

        @Override
        public Parameter<?> visit(UnknownValue value) throws OwsExceptionReport {
            throw notSupported(value);
        }
        
        private OwsExceptionReport notSupported(Value<?> value)
                throws OwsExceptionReport {
            throw new NoApplicableCodeException()
                    .withMessage("Unsupported om:parameter value %s", value
                                 .getClass().getCanonicalName());
        }
    }

}
