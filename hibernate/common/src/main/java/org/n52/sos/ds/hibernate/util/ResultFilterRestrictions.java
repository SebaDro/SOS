/**
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
package org.n52.sos.ds.hibernate.util;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.HibernateCriterionHelper;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.n52.sos.ds.hibernate.entities.observation.BaseObservation;
import org.n52.sos.ds.hibernate.entities.observation.ValuedObservation;
import org.n52.sos.exception.CodedException;
import org.n52.sos.exception.ows.InvalidParameterValueException;
import org.n52.sos.exception.ows.NoApplicableCodeException;
import org.n52.sos.ogc.filter.ComparisonFilter;
import org.n52.sos.ogc.sos.ResultFilterConstants;

public class ResultFilterRestrictions {
    
    public static void addResultFilterExpression(Criteria c, ComparisonFilter resultFilter, ResultFilterClasses resultFilterClasses, String column) throws CodedException {
        addResultFilterExpression(c, resultFilter, resultFilterClasses, column, column);
    }

    public static void addResultFilterExpression(Criteria c, ComparisonFilter resultFilter,
            ResultFilterClasses resultFilterClasses, String subqueryColumn, String column)
            throws CodedException {
        List<DetachedCriteria> list = new LinkedList<>();
        List<DetachedCriteria> complexLlist = new LinkedList<>();
        switch (resultFilter.getOperator()) {
            case PropertyIsEqualTo:
                if (isNumeric(resultFilter.getValue())) {
                    list.add(createEqDC(createDC(resultFilterClasses.getNumeric()),
                            Double.parseDouble(resultFilter.getValue()), column));
                    complexLlist.add(createEqDC(createDC(resultFilterClasses.getNumeric()),
                            Double.parseDouble(resultFilter.getValue()), BaseObservation.OBS_ID));
                }
                if (isCount(resultFilter.getValue())) {
                    list.add(createEqDC(createDC(resultFilterClasses.getCount()),
                            Integer.parseInt(resultFilter.getValue()), column));
                    complexLlist.add(createEqDC(createDC(resultFilterClasses.getCount()),
                            Integer.parseInt(resultFilter.getValue()), BaseObservation.OBS_ID));
                }
                if (!isNumeric(resultFilter.getValue()) && !isCount(resultFilter.getValue())) {
                    list.add(createEqDC(createDC(resultFilterClasses.getCategory()), resultFilter.getValue(), column));
                    list.add(createEqDC(createDC(resultFilterClasses.getText()), resultFilter.getValue(), column));
                    complexLlist.add(createEqDC(createDC(resultFilterClasses.getCategory()), resultFilter.getValue(),
                            BaseObservation.OBS_ID));
                    complexLlist.add(createEqDC(createDC(resultFilterClasses.getText()), resultFilter.getValue(),
                            BaseObservation.OBS_ID));
                }
                break;
            case PropertyIsBetween:
                if (isCount(resultFilter.getValue()) && isCount(resultFilter.getValueUpper())) {
                    list.add(createBetweenDC(createDC(resultFilterClasses.getCount()),
                            Integer.parseInt(resultFilter.getValue()), Integer.parseInt(resultFilter.getValueUpper()),
                            column));
                    complexLlist.add(createBetweenDC(createDC(resultFilterClasses.getCount()),
                            Integer.parseInt(resultFilter.getValue()), Integer.parseInt(resultFilter.getValueUpper()),
                            BaseObservation.OBS_ID));
                }
                if (isNumeric(resultFilter.getValue()) && isNumeric(resultFilter.getValueUpper())) {
                    list.add(createBetweenDC(createDC(resultFilterClasses.getNumeric()),
                            Double.parseDouble(resultFilter.getValue()),
                            Double.parseDouble(resultFilter.getValueUpper()), column));
                    complexLlist.add(createBetweenDC(createDC(resultFilterClasses.getNumeric()),
                            Double.parseDouble(resultFilter.getValue()),
                            Double.parseDouble(resultFilter.getValueUpper()), BaseObservation.OBS_ID));
                }
                if (!isNumeric(resultFilter.getValue()) && !isCount(resultFilter.getValue())) {
                    throw new NoApplicableCodeException();
                }
                break;
            case PropertyIsGreaterThan:
                if (isCount(resultFilter.getValue())) {
                    list.add(createGtDC(createDC(resultFilterClasses.getCount()),
                            Integer.parseInt(resultFilter.getValue()), column));
                    complexLlist.add(createGtDC(createDC(resultFilterClasses.getCount()),
                            Integer.parseInt(resultFilter.getValue()), BaseObservation.OBS_ID));
                }
                if (isNumeric(resultFilter.getValue())) {
                    list.add(createGtDC(createDC(resultFilterClasses.getNumeric()),
                            Double.parseDouble(resultFilter.getValue()), column));
                    complexLlist.add(createGtDC(createDC(resultFilterClasses.getNumeric()),
                            Double.parseDouble(resultFilter.getValue()), BaseObservation.OBS_ID));
                }
                if (!isNumeric(resultFilter.getValue()) && !isCount(resultFilter.getValue())) {
                    throw new NoApplicableCodeException();
                }
                break;
            case PropertyIsGreaterThanOrEqualTo:
                if (isCount(resultFilter.getValue())) {
                    list.add(createGeDC(createDC(resultFilterClasses.getCount()),
                            Integer.parseInt(resultFilter.getValue()), column));
                    complexLlist.add(createGeDC(createDC(resultFilterClasses.getCount()),
                            Integer.parseInt(resultFilter.getValue()), BaseObservation.OBS_ID));
                }
                if (isNumeric(resultFilter.getValue())) {
                    list.add(createGeDC(createDC(resultFilterClasses.getNumeric()),
                            Double.parseDouble(resultFilter.getValue()), column));
                    complexLlist.add(createGeDC(createDC(resultFilterClasses.getNumeric()),
                            Double.parseDouble(resultFilter.getValue()), BaseObservation.OBS_ID));
                }
                if (!isNumeric(resultFilter.getValue()) && !isCount(resultFilter.getValue())) {
                    throw new NoApplicableCodeException();
                }
                break;
            case PropertyIsLessThan:
                if (isCount(resultFilter.getValue())) {
                    list.add(createLtDC(createDC(resultFilterClasses.getCount()),
                            Integer.parseInt(resultFilter.getValue()), column));
                    complexLlist.add(createLtDC(createDC(resultFilterClasses.getCount()),
                            Integer.parseInt(resultFilter.getValue()), BaseObservation.OBS_ID));
                }
                if (isNumeric(resultFilter.getValue())) {
                    list.add(createLtDC(createDC(resultFilterClasses.getNumeric()),
                            Double.parseDouble(resultFilter.getValue()), column));
                    complexLlist.add(createLtDC(createDC(resultFilterClasses.getNumeric()),
                            Double.parseDouble(resultFilter.getValue()), BaseObservation.OBS_ID));
                }
                if (!isNumeric(resultFilter.getValue()) && !isCount(resultFilter.getValue())) {
                    throw new NoApplicableCodeException();
                }
                break;
            case PropertyIsLessThanOrEqualTo:
                if (isCount(resultFilter.getValue())) {
                    list.add(createLeDC(createDC(resultFilterClasses.getCount()),
                            Integer.parseInt(resultFilter.getValue()), column));
                    complexLlist.add(createLeDC(createDC(resultFilterClasses.getCount()),
                            Integer.parseInt(resultFilter.getValue()), BaseObservation.OBS_ID));
                }
                if (isNumeric(resultFilter.getValue())) {
                    list.add(createLeDC(createDC(resultFilterClasses.getNumeric()),
                            Double.parseDouble(resultFilter.getValue()), column));
                    complexLlist.add(createLeDC(createDC(resultFilterClasses.getNumeric()),
                            Double.parseDouble(resultFilter.getValue()), BaseObservation.OBS_ID));
                }
                if (!isNumeric(resultFilter.getValue()) && !isCount(resultFilter.getValue())) {
                    throw new NoApplicableCodeException();
                }
                break;
            case PropertyIsLike:
                list.add(createLikeDC(createDC(resultFilterClasses.getCategory()), resultFilter, column));
                list.add(createLikeDC(createDC(resultFilterClasses.getText()), resultFilter, column));
                complexLlist.add(createLikeDC(createDC(resultFilterClasses.getCategory()), resultFilter,
                        BaseObservation.OBS_ID));
                complexLlist.add(
                        createLikeDC(createDC(resultFilterClasses.getText()), resultFilter, BaseObservation.OBS_ID));
                break;
            default:
                throw new InvalidParameterValueException(ResultFilterConstants.RESULT_FILTER + ".operator",
                        resultFilter.getOperator().toString());
        }
        if (!complexLlist.isEmpty()) {
            list.add(createProfileDC(createDC(resultFilterClasses.getProfile(), "po"), complexLlist, column));
            list.add(createComplexDC(createDC(resultFilterClasses.getComplex(), "co"), complexLlist, column));
        }
        if (!list.isEmpty()) {
            if (list.size() > 1) {
                Disjunction d = Restrictions.disjunction();
                for (DetachedCriteria dc : list) {
                    d.add(getSubquery(dc, subqueryColumn));
                }
                c.add(d);
            } else {
                c.add(getSubquery(list.iterator().next(), subqueryColumn));
            }
        }
    }

    private static boolean isNumeric(String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean isCount(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static Criterion getSubquery(DetachedCriteria dc, String column) {
        return Subqueries.propertyIn(column, dc);
    }

    private static DetachedCriteria createDC(Class<?> clazz) {
        return DetachedCriteria.forClass(clazz);
    }
    
    private static DetachedCriteria createDC(Class<?> clazz, String alias) {
        return DetachedCriteria.forClass(clazz, alias);
    }

    private static DetachedCriteria createEqDC(DetachedCriteria dc, Object value, String column) {
        return dc.add(Restrictions.eq(ValuedObservation.VALUE, value)).setProjection(Projections.property(column));
    }

    private static DetachedCriteria createGtDC(DetachedCriteria dc, Object value, String column) {
        return dc.add(Restrictions.gt(ValuedObservation.VALUE, value)).setProjection(Projections.property(column));
    }

    private static DetachedCriteria createGeDC(DetachedCriteria dc, Object value, String column) {
        return dc.add(Restrictions.ge(ValuedObservation.VALUE, value)).setProjection(Projections.property(column));
    }

    private static DetachedCriteria createLtDC(DetachedCriteria dc, Object value, String column) {
        return dc.add(Restrictions.lt(ValuedObservation.VALUE, value)).setProjection(Projections.property(column));
    }

    private static DetachedCriteria createLeDC(DetachedCriteria dc, Object value, String column) {
        return dc.add(Restrictions.le(ValuedObservation.VALUE, value)).setProjection(Projections.property(column));
    }

    private static DetachedCriteria createBetweenDC(DetachedCriteria dc, Object lower, Object upper, String column) {
        return dc.add(Restrictions.between(ValuedObservation.VALUE, lower, upper))
                .setProjection(Projections.property(column));
    }

    private static DetachedCriteria createLikeDC(DetachedCriteria dc, ComparisonFilter resultFilter, String column) {
        String value = resultFilter.getValue().replaceAll(resultFilter.getSingleChar(), "_")
                .replaceAll(resultFilter.getWildCard(), "%");
        return dc
                .add(HibernateCriterionHelper.getLikeExpression(ValuedObservation.VALUE, value,
                        resultFilter.getEscapeString(), resultFilter.isMatchCase()))
                .setProjection(Projections.property(column));
    }

    private static DetachedCriteria createProfileDC(DetachedCriteria dc, List<DetachedCriteria> list,
            String column) {
        return createDC(dc, list, column, "pv");
    }

    private static DetachedCriteria createComplexDC(DetachedCriteria dc, List<DetachedCriteria> list,
            String column) {
        return createDC(dc, list, column, "cv");
    }
    
    private static DetachedCriteria createDC(DetachedCriteria dc, List<DetachedCriteria> list,
            String column, String alias) {
        DetachedCriteria complex = dc.setProjection(Projections.property(column)).createAlias(ValuedObservation.VALUE, alias);
        if (list.size() > 1) {
            Disjunction d = Restrictions.disjunction();
            for (DetachedCriteria ldc : list) {
                d.add(Subqueries.propertyIn(alias + "." + BaseObservation.OBS_ID, ldc));
            }
            complex.add(d);
        } else {
            complex.add(Subqueries.propertyIn(alias + "." + BaseObservation.OBS_ID, list.iterator().next()));
        }
        return complex;
    }

}
