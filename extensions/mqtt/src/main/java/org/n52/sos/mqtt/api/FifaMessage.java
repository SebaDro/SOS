package org.n52.sos.mqtt.api;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

public class FifaMessage implements MqttMessage {

    public static final String OFFERING = "offering";
    public static final String PROCEDURE = "procedure";
    public static final String FEATURE_OF_INTEREST = "featureOfInterest";
    public static final String OBSERVED_PROPERTY = "observedProperty";
    public static final String PHENOMENON_TIME = "phenomenonTime";
    public static final String RESULT = "result";
    public static final String PARAMETER = "parameter";
    
    private String offering;
    private String procedure;
    private String featureOfInterest;
    private String observedProperty;
    private String result;
    private DateTime phenomenonTime;
    private Map<String, Object> parameter = new HashMap<>();

    /**
     * @return the offering
     */
    public String getOffering() {
        return offering;
    }

    /**
     * @param offering
     *            the offering to set
     * @return 
     */
    public FifaMessage setOffering(String offering) {
        this.offering = offering;
        return this;
    }

    /**
     * @return the procedure
     */
    public String getProcedure() {
        return procedure;
    }

    /**
     * @param procedure
     *            the procedure to set
     * @return 
     */
    public FifaMessage setProcedure(String procedure) {
        this.procedure = procedure;
        return this;
    }

    /**
     * @return the featureOfInterest
     */
    public String getFeatureOfInterest() {
        return featureOfInterest;
    }

    /**
     * @param featureOfInterest
     *            the featureOfInterest to set
     * @return 
     */
    public FifaMessage setFeatureOfInterest(String featureOfInterest) {
        this.featureOfInterest = featureOfInterest;
        return this;
    }

    /**
     * @return the observedProperty
     */
    public String getObservedProperty() {
        return observedProperty;
    }

    /**
     * @param observedProperty
     *            the observedProperty to set
     * @return 
     */
    public FifaMessage setObservedProperty(String observedProperty) {
        this.observedProperty = observedProperty;
        return this;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result
     *            the result to set
     * @return 
     */
    public FifaMessage setResult(String result) {
        this.result = result;
        return this;
    }

    /**
     * @return the phenomenonTime
     */
    public DateTime getPhenomenonTime() {
        return phenomenonTime;
    }

    /**
     * @param phenomenonTime
     *            the phenomenonTime to set
     * @return 
     */
    public FifaMessage setPhenomenonTime(DateTime phenomenonTime) {
        this.phenomenonTime = phenomenonTime;
        return this;
    }

    /**
     * @return the parameter
     */
    public Map<String, Object> getParameter() {
        return parameter;
    }

    /**
     * @param parameter
     *            the parameter to set
     * @return 
     */
    public FifaMessage addParameter(String key, Object value) {
        this.parameter.put(key, value);
        return this;
    }
    
    public FifaMessage setParameter(Map<String, Object> parameter) {
        this.parameter.clear();
        this.parameter.putAll(parameter);
        return this;
    }

}
