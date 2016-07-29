--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.2
-- Dumped by pg_dump version 9.5.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: sos_rawdb_2807; Type: SCHEMA; Schema: -; Owner: observations_p
--

CREATE SCHEMA sos_rawdb_2807;


ALTER SCHEMA sos_rawdb_2807 OWNER TO observations_p;

SET search_path = sos_rawdb_2807, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: blobvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE blobvalue (
    observationid bigint NOT NULL,
    value oid
);


ALTER TABLE blobvalue OWNER TO observations_p;

--
-- Name: TABLE blobvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE blobvalue IS 'Value table for blob observation';


--
-- Name: COLUMN blobvalue.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN blobvalue.observationid IS 'Foreign Key (FK) to the related observation from the observation table. Contains "observation".observationid';


--
-- Name: COLUMN blobvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN blobvalue.value IS 'Blob observation value';


--
-- Name: booleanfeatparamvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE booleanfeatparamvalue (
    parameterid bigint NOT NULL,
    value character(1),
    CONSTRAINT booleanfeatparamvalue_value_check CHECK ((value = ANY (ARRAY['T'::bpchar, 'F'::bpchar])))
);


ALTER TABLE booleanfeatparamvalue OWNER TO observations_p;

--
-- Name: TABLE booleanfeatparamvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE booleanfeatparamvalue IS 'Value table for boolean parameter';


--
-- Name: COLUMN booleanfeatparamvalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN booleanfeatparamvalue.parameterid IS 'Foreign Key (FK) to the related parameter from the parameter table. Contains "featureParameter".parameterid';


--
-- Name: COLUMN booleanfeatparamvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN booleanfeatparamvalue.value IS 'Boolean parameter value';


--
-- Name: booleanparametervalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE booleanparametervalue (
    parameterid bigint NOT NULL,
    value character(1),
    CONSTRAINT booleanparametervalue_value_check CHECK ((value = ANY (ARRAY['T'::bpchar, 'F'::bpchar])))
);


ALTER TABLE booleanparametervalue OWNER TO observations_p;

--
-- Name: TABLE booleanparametervalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE booleanparametervalue IS 'Value table for boolean parameter';


--
-- Name: COLUMN booleanparametervalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN booleanparametervalue.parameterid IS 'Foreign Key (FK) to the related parameter from the parameter table. Contains "parameter".parameterid';


--
-- Name: COLUMN booleanparametervalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN booleanparametervalue.value IS 'Boolean parameter value';


--
-- Name: booleanseriesparamvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE booleanseriesparamvalue (
    parameterid bigint NOT NULL,
    value character(1),
    CONSTRAINT booleanseriesparamvalue_value_check CHECK ((value = ANY (ARRAY['T'::bpchar, 'F'::bpchar])))
);


ALTER TABLE booleanseriesparamvalue OWNER TO observations_p;

--
-- Name: TABLE booleanseriesparamvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE booleanseriesparamvalue IS 'Value table for boolean parameter';


--
-- Name: COLUMN booleanseriesparamvalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN booleanseriesparamvalue.parameterid IS 'Foreign Key (FK) to the related parameter from the series parameter table. Contains seriesparameter.parameterid';


--
-- Name: COLUMN booleanseriesparamvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN booleanseriesparamvalue.value IS 'Boolean parameter value';


--
-- Name: booleanvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE booleanvalue (
    observationid bigint NOT NULL,
    value character(1),
    CONSTRAINT booleanvalue_value_check CHECK ((value = ANY (ARRAY['T'::bpchar, 'F'::bpchar]))),
    CONSTRAINT booleanvalue_value_check1 CHECK ((value = ANY (ARRAY['T'::bpchar, 'F'::bpchar])))
);


ALTER TABLE booleanvalue OWNER TO observations_p;

--
-- Name: TABLE booleanvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE booleanvalue IS 'Value table for boolean observation';


--
-- Name: COLUMN booleanvalue.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN booleanvalue.observationid IS 'Foreign Key (FK) to the related observation from the observation table. Contains "observation".observationid';


--
-- Name: COLUMN booleanvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN booleanvalue.value IS 'Boolean observation value';


--
-- Name: categoryfeatparamvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE categoryfeatparamvalue (
    parameterid bigint NOT NULL,
    value character varying(255),
    unitid bigint
);


ALTER TABLE categoryfeatparamvalue OWNER TO observations_p;

--
-- Name: TABLE categoryfeatparamvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE categoryfeatparamvalue IS 'Value table for category parameter';


--
-- Name: COLUMN categoryfeatparamvalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN categoryfeatparamvalue.parameterid IS 'Foreign Key (FK) to the related parameter from the parameter table. Contains "featureParameter".parameterid';


--
-- Name: COLUMN categoryfeatparamvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN categoryfeatparamvalue.value IS 'Category parameter value';


--
-- Name: COLUMN categoryfeatparamvalue.unitid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN categoryfeatparamvalue.unitid IS 'Foreign Key (FK) to the related unit of measure. Contains "unit".unitid. Optional';


--
-- Name: categoryparametervalue; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW categoryparametervalue AS
 SELECT (1)::bigint AS parameterid,
    'http://www.sandre.eaufrance.fr/?urn=urn:sandre:donnees:415::CdElement:1:::referentiel:3.1:xml'::character varying(255) AS value,
    NULL::bigint AS unitid
UNION
 SELECT (2)::bigint AS parameterid,
    'http://www.sandre.eaufrance.fr/?urn=urn:sandre:donnees:414::CdElement:0:::referentiel:3.1:xml'::character varying(255) AS value,
    NULL::bigint AS unitid
  WITH NO DATA;


ALTER TABLE categoryparametervalue OWNER TO observations_p;

--
-- Name: categoryparametervalue_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE categoryparametervalue_t (
    parameterid bigint NOT NULL,
    value character varying(255),
    unitid bigint
);


ALTER TABLE categoryparametervalue_t OWNER TO observations_p;

--
-- Name: TABLE categoryparametervalue_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE categoryparametervalue_t IS 'Value table for category parameter';


--
-- Name: COLUMN categoryparametervalue_t.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN categoryparametervalue_t.parameterid IS 'Foreign Key (FK) to the related parameter from the parameter table. Contains "parameter".parameterid';


--
-- Name: COLUMN categoryparametervalue_t.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN categoryparametervalue_t.value IS 'Category parameter value';


--
-- Name: COLUMN categoryparametervalue_t.unitid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN categoryparametervalue_t.unitid IS 'Foreign Key (FK) to the related unit of measure. Contains "unit".unitid. Optional';


--
-- Name: featureofinterest; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW featureofinterest AS
 SELECT t1.id AS featureofinterestid,
    NULL::character(1) AS hibernatediscriminator,
    (1)::bigint AS featureofinteresttypeid,
    t1.identifier,
    (1)::bigint AS codespace,
    (t1.description)::text AS name,
    NULL::character varying(255) AS codespacename,
    'SF_SamplingPoint representation of French Water Information System''s EnvironmentalMonitoringFacility ''Piezometre'''::text AS description,
    t1.geom,
    (((((((((('<sams:SF_SpatialSamplingFeature xmlns:sams="http://www.opengis.net/samplingSpatial/2.0" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:sf="http://www.opengis.net/sampling/2.0" xmlns:xlink="http://www.w3.org/1999/xlink" gml:id="ssf_test_feature_9"><gml:identifier codeSpace="">'::text || (t1.identifier)::text) || '</gml:identifier><gml:name>'::text) || (t1.identifier)::text) || '</gml:name><sf:type xlink:href="http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint"/><sf:sampledFeature xlink:href="'::text) || (( SELECT foi2.identifier
           FROM rawdata.foi2
          WHERE (foi2.id = t1.sampledfeature)))::text) || '"/><sams:shape><gml:Point gml:id="point"><gml:pos srsName="http://www.opengis.net/def/crs/EPSG/0/4326">'::text) || postgis.st_x(t1.geom)) || ' '::text) || postgis.st_y(t1.geom)) || '</gml:pos></gml:Point></sams:shape></sams:SF_SpatialSamplingFeature>'::text) AS descriptionxml,
    t1.url
   FROM rawdata.foi2 t1
  WHERE (t1.sampledfeature IS NOT NULL)
UNION
 SELECT t1.id AS featureofinterestid,
    NULL::character(1) AS hibernatediscriminator,
    (2)::bigint AS featureofinteresttypeid,
    t1.identifier,
    (1)::bigint AS codespace,
    (t1.description)::text AS name,
    NULL::character varying(255) AS codespacename,
    'Aquifer (from French BD LISA) monitored by the ''Piezometre'''::text AS description,
    t1.geom,
    (((((((((('<sams:SF_SpatialSamplingFeature xmlns:sams="http://www.opengis.net/samplingSpatial/2.0" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:sf="http://www.opengis.net/sampling/2.0" xmlns:xlink="http://www.w3.org/1999/xlink" gml:id="ssf_test_feature_9"><gml:identifier codeSpace="">'::text || (t1.identifier)::text) || '</gml:identifier><gml:name>'::text) || (t1.identifier)::text) || '</gml:name><sf:type xlink:href="http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint"/><sf:sampledFeature xlink:href="'::text) || (( SELECT foi2.identifier
           FROM rawdata.foi2
          WHERE (foi2.id = t1.sampledfeature)))::text) || '"/><sams:shape><gml:Point gml:id="point"><gml:pos srsName="http://www.opengis.net/def/crs/EPSG/0/4326">'::text) || postgis.st_x(t1.geom)) || ' '::text) || postgis.st_y(t1.geom)) || '</gml:pos></gml:Point></sams:shape></sams:SF_SpatialSamplingFeature>'::text) AS descriptionxml,
    t1.url
   FROM rawdata.foi2 t1
  WHERE (t1.sampledfeature IS NULL)
  WITH NO DATA;


ALTER TABLE featureofinterest OWNER TO observations_p;

--
-- Name: series; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW series AS
 SELECT obs.id AS seriesid,
    obs.samplingfeature AS featureofinterestid,
    obs.observedproperty AS observablepropertyid,
    obs.procedure AS procedureid,
    obs.samplingfeature AS offeringid,
    'F'::character(1) AS deleted,
    'T'::character(1) AS published,
    'F'::character(1) AS hiddenchild,
    ((('http://ressource.brgm-rec.fr/obs/rawSeriePiezo/'::text || COALESCE((foi2.localidentifier)::text, (foi2.id)::text)) || '-'::text) || ((obs.id)::character varying(255))::text) AS identifier,
    'measurement'::character varying(255) AS seriestype,
    ( SELECT max(result."time") AS max
           FROM rawdata.result
          WHERE (result.observation = obs.id)) AS lasttimestamp,
    ( SELECT min(result."time") AS min
           FROM rawdata.result
          WHERE (result.observation = obs.id)) AS firsttimestamp,
    ( SELECT result.value
           FROM rawdata.result
          WHERE (result.observation = obs.id)
          ORDER BY result."time"
         LIMIT 1) AS firstnumericvalue,
    ( SELECT result.value
           FROM rawdata.result
          WHERE (result.observation = obs.id)
          ORDER BY result."time" DESC
         LIMIT 1) AS lastnumericvalue,
    ( SELECT result.uom
           FROM rawdata.result
          WHERE (result.observation = obs.id)
         LIMIT 1) AS unitid,
    (1)::bigint AS codespace,
    NULL::character varying(255) AS name,
    NULL::bigint AS codespacename,
    NULL::character varying(255) AS description
   FROM (rawdata.observation obs
     JOIN rawdata.foi2 ON ((obs.samplingfeature = foi2.id)))
  WITH NO DATA;


ALTER TABLE series OWNER TO observations_p;

--
-- Name: categoryseriesparamvalue; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW categoryseriesparamvalue AS
 SELECT series.seriesid AS parameterid,
    featureofinterest.identifier AS value,
    NULL::bigint AS unitid
   FROM (series
     JOIN featureofinterest ON ((series.featureofinterestid = featureofinterest.featureofinterestid)))
  WITH NO DATA;


ALTER TABLE categoryseriesparamvalue OWNER TO observations_p;

--
-- Name: categoryseriesparamvalue_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE categoryseriesparamvalue_t (
    parameterid bigint NOT NULL,
    value character varying(255),
    unitid bigint
);


ALTER TABLE categoryseriesparamvalue_t OWNER TO observations_p;

--
-- Name: TABLE categoryseriesparamvalue_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE categoryseriesparamvalue_t IS 'Value table for category parameter';


--
-- Name: COLUMN categoryseriesparamvalue_t.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN categoryseriesparamvalue_t.parameterid IS 'Foreign Key (FK) to the related parameter from the series parameter table. Contains seriesparameter.parameterid';


--
-- Name: COLUMN categoryseriesparamvalue_t.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN categoryseriesparamvalue_t.value IS 'Category parameter value';


--
-- Name: COLUMN categoryseriesparamvalue_t.unitid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN categoryseriesparamvalue_t.unitid IS 'Foreign Key (FK) to the related unit of measure. Contains "unit".unitid. Optional';


--
-- Name: categoryvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE categoryvalue (
    observationid bigint NOT NULL,
    value character varying(255),
    identifier character varying(255),
    name character varying(255),
    description character varying(255)
);


ALTER TABLE categoryvalue OWNER TO observations_p;

--
-- Name: TABLE categoryvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE categoryvalue IS 'Value table for category observation';


--
-- Name: COLUMN categoryvalue.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN categoryvalue.observationid IS 'Foreign Key (FK) to the related observation from the observation table. Contains "observation".observationid';


--
-- Name: COLUMN categoryvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN categoryvalue.value IS 'Category observation value';


--
-- Name: COLUMN categoryvalue.identifier; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN categoryvalue.identifier IS 'SweCategory observation identifier';


--
-- Name: COLUMN categoryvalue.name; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN categoryvalue.name IS 'SweCategory observation name';


--
-- Name: COLUMN categoryvalue.description; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN categoryvalue.description IS 'SweCategory observation description';


--
-- Name: codespace; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW codespace AS
 SELECT (1)::bigint AS codespaceid,
    'http://www.ietf.org/rfc/rfc2616'::character varying(255) AS codespace
  WITH NO DATA;


ALTER TABLE codespace OWNER TO observations_p;

--
-- Name: codespace_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE codespace_t (
    codespaceid bigint NOT NULL,
    codespace character varying(255) NOT NULL
);


ALTER TABLE codespace_t OWNER TO observations_p;

--
-- Name: TABLE codespace_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE codespace_t IS 'Table to store the gml:identifier and gml:name codespace information. Mapping file: mapping/core/Codespace.hbm.xml';


--
-- Name: COLUMN codespace_t.codespaceid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN codespace_t.codespaceid IS 'Table primary key, used for relations';


--
-- Name: COLUMN codespace_t.codespace; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN codespace_t.codespace IS 'The codespace value';


--
-- Name: codespaceid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE codespaceid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE codespaceid_seq OWNER TO observations_p;

--
-- Name: complexvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE complexvalue (
    observationid bigint NOT NULL
);


ALTER TABLE complexvalue OWNER TO observations_p;

--
-- Name: TABLE complexvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE complexvalue IS 'Value table for complex observation';


--
-- Name: COLUMN complexvalue.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN complexvalue.observationid IS 'Foreign Key (FK) to the related observation from the observation table. Contains "observation".observationid';


--
-- Name: compositeobservation; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE compositeobservation (
    observationid bigint NOT NULL,
    childobservationid bigint NOT NULL
);


ALTER TABLE compositeobservation OWNER TO observations_p;

--
-- Name: TABLE compositeobservation; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE compositeobservation IS 'Relation table for complex parent/child observations';


--
-- Name: COLUMN compositeobservation.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN compositeobservation.observationid IS 'Foreign Key (FK) to the related parent complex observation. Contains "observation".observationid';


--
-- Name: COLUMN compositeobservation.childobservationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN compositeobservation.childobservationid IS 'Foreign Key (FK) to the related child complex observation. Contains "observation".observationid';


--
-- Name: compositephenomenon; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE compositephenomenon (
    parentobservablepropertyid bigint NOT NULL,
    childobservablepropertyid bigint NOT NULL
);


ALTER TABLE compositephenomenon OWNER TO observations_p;

--
-- Name: TABLE compositephenomenon; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE compositephenomenon IS 'Relation table to store observableProperty hierarchies, aka compositePhenomenon. E.g. define a parent in a query and all childs are also contained in the response. Mapping file: mapping/transactional/TObservableProperty.hbm.xml';


--
-- Name: COLUMN compositephenomenon.parentobservablepropertyid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN compositephenomenon.parentobservablepropertyid IS 'Foreign Key (FK) to the related parent observableProperty. Contains "observableProperty".observablePropertyid';


--
-- Name: COLUMN compositephenomenon.childobservablepropertyid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN compositephenomenon.childobservablepropertyid IS 'Foreign Key (FK) to the related child observableProperty. Contains "observableProperty".observablePropertyid';


--
-- Name: countfeatparamvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE countfeatparamvalue (
    parameterid bigint NOT NULL,
    value integer
);


ALTER TABLE countfeatparamvalue OWNER TO observations_p;

--
-- Name: TABLE countfeatparamvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE countfeatparamvalue IS 'Value table for count parameter';


--
-- Name: COLUMN countfeatparamvalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN countfeatparamvalue.parameterid IS 'Foreign Key (FK) to the related parameter from the parameter table. Contains "featureParameter".parameterid';


--
-- Name: COLUMN countfeatparamvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN countfeatparamvalue.value IS 'Count parameter value';


--
-- Name: countparametervalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE countparametervalue (
    parameterid bigint NOT NULL,
    value integer
);


ALTER TABLE countparametervalue OWNER TO observations_p;

--
-- Name: TABLE countparametervalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE countparametervalue IS 'Value table for count parameter';


--
-- Name: COLUMN countparametervalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN countparametervalue.parameterid IS 'Foreign Key (FK) to the related parameter from the parameter table. Contains "parameter".parameterid';


--
-- Name: COLUMN countparametervalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN countparametervalue.value IS 'Count parameter value';


--
-- Name: countseriesparamvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE countseriesparamvalue (
    parameterid bigint NOT NULL,
    value integer
);


ALTER TABLE countseriesparamvalue OWNER TO observations_p;

--
-- Name: TABLE countseriesparamvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE countseriesparamvalue IS 'Value table for count parameter';


--
-- Name: COLUMN countseriesparamvalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN countseriesparamvalue.parameterid IS 'Foreign Key (FK) to the related parameter from the series parameter table. Contains seriesparameter.parameterid';


--
-- Name: COLUMN countseriesparamvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN countseriesparamvalue.value IS 'Count parameter value';


--
-- Name: countvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE countvalue (
    observationid bigint NOT NULL,
    value integer
);


ALTER TABLE countvalue OWNER TO observations_p;

--
-- Name: TABLE countvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE countvalue IS 'Value table for count observation';


--
-- Name: COLUMN countvalue.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN countvalue.observationid IS 'Foreign Key (FK) to the related observation from the observation table. Contains "observation".observationid';


--
-- Name: COLUMN countvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN countvalue.value IS 'Count observation value';


--
-- Name: featureofinterest_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE featureofinterest_t (
    featureofinterestid bigint NOT NULL,
    hibernatediscriminator character varying(255),
    featureofinteresttypeid bigint NOT NULL,
    identifier character varying(255),
    codespace bigint,
    name character varying(255),
    codespacename bigint,
    description character varying(255),
    geom postgis.geometry,
    descriptionxml text,
    url character varying(255)
);


ALTER TABLE featureofinterest_t OWNER TO observations_p;

--
-- Name: COLUMN featureofinterest_t.featureofinterestid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureofinterest_t.featureofinterestid IS 'Table primary key, used for relations';


--
-- Name: COLUMN featureofinterest_t.featureofinteresttypeid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureofinterest_t.featureofinteresttypeid IS 'Relation/foreign key to the featureOfInterestType table. Describes the type of the featureOfInterest. Contains "featureOfInterestType".featureOfInterestTypeId';


--
-- Name: COLUMN featureofinterest_t.identifier; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureofinterest_t.identifier IS 'The identifier of the featureOfInterest, gml:identifier. Used as parameter for queries. Optional but unique';


--
-- Name: COLUMN featureofinterest_t.codespace; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureofinterest_t.codespace IS 'Relation/foreign key to the codespace table. Contains the gml:identifier codespace. Optional';


--
-- Name: COLUMN featureofinterest_t.name; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureofinterest_t.name IS 'The name of the featureOfInterest, gml:name. Optional';


--
-- Name: COLUMN featureofinterest_t.codespacename; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureofinterest_t.codespacename IS 'The name of the featureOfInterest, gml:name. Optional';


--
-- Name: COLUMN featureofinterest_t.description; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureofinterest_t.description IS 'Description of the featureOfInterest, gml:description. Optional';


--
-- Name: COLUMN featureofinterest_t.geom; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureofinterest_t.geom IS 'The geometry of the featureOfInterest (composed of the “latitude” and “longitude”) . Optional';


--
-- Name: COLUMN featureofinterest_t.descriptionxml; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureofinterest_t.descriptionxml IS 'XML description of the feature, used when transactional profile is supported . Optional';


--
-- Name: COLUMN featureofinterest_t.url; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureofinterest_t.url IS 'Reference URL to the feature if it is stored in another service, e.g. WFS. Optional but unique';


--
-- Name: featureofinterestid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE featureofinterestid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE featureofinterestid_seq OWNER TO observations_p;

--
-- Name: featureofinteresttype; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW featureofinteresttype AS
 SELECT (1)::bigint AS featureofinteresttypeid,
    'http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint'::character varying(255) AS featureofinteresttype
UNION
 SELECT (2)::bigint AS featureofinteresttypeid,
    'http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SampledFeature'::character varying(255) AS featureofinteresttype
  WITH NO DATA;


ALTER TABLE featureofinteresttype OWNER TO observations_p;

--
-- Name: featureofinteresttype_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE featureofinteresttype_t (
    featureofinteresttypeid bigint NOT NULL,
    featureofinteresttype character varying(255) NOT NULL
);


ALTER TABLE featureofinteresttype_t OWNER TO observations_p;

--
-- Name: TABLE featureofinteresttype_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE featureofinteresttype_t IS 'Table to store the FeatureOfInterestType information. Mapping file: mapping/core/FeatureOfInterestType.hbm.xml';


--
-- Name: COLUMN featureofinteresttype_t.featureofinteresttypeid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureofinteresttype_t.featureofinteresttypeid IS 'Table primary key, used for relations';


--
-- Name: COLUMN featureofinteresttype_t.featureofinteresttype; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureofinteresttype_t.featureofinteresttype IS 'The featureOfInterestType value, e.g. http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint (OGC OM 2.0 specification) for point features';


--
-- Name: featureofinteresttypeid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE featureofinteresttypeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE featureofinteresttypeid_seq OWNER TO observations_p;

--
-- Name: featureparameter; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE featureparameter (
    parameterid bigint NOT NULL,
    featureofinterestid bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE featureparameter OWNER TO observations_p;

--
-- Name: TABLE featureparameter; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE featureparameter IS 'Table to store additional featureOfInterest information (sf:parameter). Mapping file: mapping/core/FeatureParameter.hbm.xml';


--
-- Name: COLUMN featureparameter.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureparameter.parameterid IS 'Table primary key';


--
-- Name: COLUMN featureparameter.featureofinterestid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureparameter.featureofinterestid IS 'Foreign Key (FK) to the related featureOfInterest. Contains "featureOfInterest.featureOfInterestId';


--
-- Name: COLUMN featureparameter.name; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featureparameter.name IS 'Parameter name';


--
-- Name: featurerelation; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW featurerelation AS
 SELECT foi2.sampledfeature AS parentfeatureid,
    foi2.id AS childfeatureid
   FROM rawdata.foi2
  WHERE (foi2.sampledfeature IS NOT NULL)
  WITH NO DATA;


ALTER TABLE featurerelation OWNER TO observations_p;

--
-- Name: featurerelation_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE featurerelation_t (
    parentfeatureid bigint NOT NULL,
    childfeatureid bigint NOT NULL
);


ALTER TABLE featurerelation_t OWNER TO observations_p;

--
-- Name: TABLE featurerelation_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE featurerelation_t IS 'Relation table to store feature hierarchies. E.g. define a parent in a query and all childs are also contained in the response. Mapping file: mapping/transactional/TFeatureOfInterest.hbm.xml';


--
-- Name: COLUMN featurerelation_t.parentfeatureid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featurerelation_t.parentfeatureid IS 'Foreign Key (FK) to the related parent featureOfInterest. Contains "featureOfInterest".featureOfInterestid';


--
-- Name: COLUMN featurerelation_t.childfeatureid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN featurerelation_t.childfeatureid IS 'Foreign Key (FK) to the related child featureOfInterest. Contains "featureOfInterest".featureOfInterestid';


--
-- Name: geometryvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE geometryvalue (
    observationid bigint NOT NULL,
    value postgis.geometry
);


ALTER TABLE geometryvalue OWNER TO observations_p;

--
-- Name: TABLE geometryvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE geometryvalue IS 'Value table for geometry observation';


--
-- Name: COLUMN geometryvalue.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN geometryvalue.observationid IS 'Foreign Key (FK) to the related observation from the observation table. Contains "observation".observationid';


--
-- Name: COLUMN geometryvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN geometryvalue.value IS 'Geometry observation value';


--
-- Name: metadataid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE metadataid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE metadataid_seq OWNER TO observations_p;

--
-- Name: numericfeatparamvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE numericfeatparamvalue (
    parameterid bigint NOT NULL,
    value double precision,
    unitid bigint
);


ALTER TABLE numericfeatparamvalue OWNER TO observations_p;

--
-- Name: TABLE numericfeatparamvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE numericfeatparamvalue IS 'Value table for numeric/Measurment parameter';


--
-- Name: COLUMN numericfeatparamvalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN numericfeatparamvalue.parameterid IS 'Foreign Key (FK) to the related parameter from the parameter table. Contains "featureParameter".parameterid';


--
-- Name: COLUMN numericfeatparamvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN numericfeatparamvalue.value IS 'Numeric/Quantity parameter value';


--
-- Name: COLUMN numericfeatparamvalue.unitid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN numericfeatparamvalue.unitid IS 'Foreign Key (FK) to the related unit of measure. Contains "unit".unitid. Optional';


--
-- Name: numericparametervalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE numericparametervalue (
    parameterid bigint NOT NULL,
    value double precision,
    unitid bigint
);


ALTER TABLE numericparametervalue OWNER TO observations_p;

--
-- Name: TABLE numericparametervalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE numericparametervalue IS 'Value table for numeric/Measurment parameter';


--
-- Name: COLUMN numericparametervalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN numericparametervalue.parameterid IS 'Foreign Key (FK) to the related parameter from the parameter table. Contains "parameter".parameterid';


--
-- Name: COLUMN numericparametervalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN numericparametervalue.value IS 'Numeric/Quantity parameter value';


--
-- Name: COLUMN numericparametervalue.unitid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN numericparametervalue.unitid IS 'Foreign Key (FK) to the related unit of measure. Contains "unit".unitid. Optional';


--
-- Name: numericseriesparamvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE numericseriesparamvalue (
    parameterid bigint NOT NULL,
    value double precision,
    unitid bigint
);


ALTER TABLE numericseriesparamvalue OWNER TO observations_p;

--
-- Name: TABLE numericseriesparamvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE numericseriesparamvalue IS 'Value table for numeric/Measurment parameter';


--
-- Name: COLUMN numericseriesparamvalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN numericseriesparamvalue.parameterid IS 'Foreign Key (FK) to the related parameter from the series parameter table. Contains seriesparameter.parameterid';


--
-- Name: COLUMN numericseriesparamvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN numericseriesparamvalue.value IS 'Numeric/Quantity parameter value';


--
-- Name: COLUMN numericseriesparamvalue.unitid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN numericseriesparamvalue.unitid IS 'Foreign Key (FK) to the related unit of measure. Contains "unit".unitid. Optional';


--
-- Name: numericvalue; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW numericvalue AS
 SELECT result.id AS observationid,
    result.value
   FROM rawdata.result
  WITH NO DATA;


ALTER TABLE numericvalue OWNER TO observations_p;

--
-- Name: numericvalue_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE numericvalue_t (
    observationid bigint NOT NULL,
    value double precision
);


ALTER TABLE numericvalue_t OWNER TO observations_p;

--
-- Name: TABLE numericvalue_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE numericvalue_t IS 'Value table for numeric/Measurment observation';


--
-- Name: COLUMN numericvalue_t.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN numericvalue_t.observationid IS 'Foreign Key (FK) to the related observation from the observation table. Contains "observation".observationid';


--
-- Name: COLUMN numericvalue_t.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN numericvalue_t.value IS 'Numeric/Measurment observation value';


--
-- Name: observableproperty; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW observableproperty AS
 SELECT (1)::bigint AS observablepropertyid,
    'http://id.eaufrance.fr/par/1689.xml'::character varying(255) AS identifier,
    (1)::bigint AS codespace,
    'GroundWaterLevel'::character varying(255) AS name,
    NULL::bigint AS codespacename,
    NULL::character varying(255) AS description,
    'F'::character(1) AS disabled,
    'F'::character(1) AS hiddenchild
UNION
 SELECT (2)::bigint AS observablepropertyid,
    'urn:ogc:def:property:OGC:Temperature'::character varying(255) AS identifier,
    (1)::bigint AS codespace,
    'Temperature'::character varying(255) AS name,
    NULL::bigint AS codespacename,
    NULL::character varying(255) AS description,
    'F'::character(1) AS disabled,
    'F'::character(1) AS hiddenchild
UNION
 SELECT (3)::bigint AS observablepropertyid,
    'urn:ogc:def:property:OGC:WaterFlow'::character varying(255) AS identifier,
    (1)::bigint AS codespace,
    'WaterFlow'::character varying(255) AS name,
    NULL::bigint AS codespacename,
    NULL::character varying(255) AS description,
    'F'::character(1) AS disabled,
    'F'::character(1) AS hiddenchild
UNION
 SELECT (4)::bigint AS observablepropertyid,
    'urn:ogc:def:property:OGC:RainFall'::character varying(255) AS identifier,
    (1)::bigint AS codespace,
    'RainFall'::character varying(255) AS name,
    NULL::bigint AS codespacename,
    NULL::character varying(255) AS description,
    'F'::character(1) AS disabled,
    'F'::character(1) AS hiddenchild
  WITH NO DATA;


ALTER TABLE observableproperty OWNER TO observations_p;

--
-- Name: observableproperty_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE observableproperty_t (
    observablepropertyid bigint NOT NULL,
    identifier character varying(255) NOT NULL,
    codespace bigint,
    name character varying(255),
    codespacename bigint,
    description character varying(255),
    disabled character(1) DEFAULT 'F'::bpchar NOT NULL,
    hiddenchild character(1) DEFAULT 'F'::bpchar NOT NULL,
    CONSTRAINT observableproperty_disabled_check CHECK ((disabled = ANY (ARRAY['T'::bpchar, 'F'::bpchar]))),
    CONSTRAINT observableproperty_hiddenchild_check CHECK ((hiddenchild = ANY (ARRAY['T'::bpchar, 'F'::bpchar])))
);


ALTER TABLE observableproperty_t OWNER TO observations_p;

--
-- Name: TABLE observableproperty_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE observableproperty_t IS 'Table to store the ObservedProperty/Phenomenon information. Mapping file: mapping/core/ObservableProperty.hbm.xml';


--
-- Name: COLUMN observableproperty_t.observablepropertyid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observableproperty_t.observablepropertyid IS 'Table primary key, used for relations';


--
-- Name: COLUMN observableproperty_t.identifier; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observableproperty_t.identifier IS 'The identifier of the observableProperty, gml:identifier. Used as parameter for queries. Unique';


--
-- Name: COLUMN observableproperty_t.codespace; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observableproperty_t.codespace IS 'Relation/foreign key to the codespace table. Contains the gml:identifier codespace. Optional';


--
-- Name: COLUMN observableproperty_t.name; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observableproperty_t.name IS 'The name of the observableProperty, gml:name. Optional';


--
-- Name: COLUMN observableproperty_t.codespacename; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observableproperty_t.codespacename IS 'Relation/foreign key to the codespace table. Contains the gml:name codespace. Optional';


--
-- Name: COLUMN observableproperty_t.description; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observableproperty_t.description IS 'Description of the observableProperty, gml:description. Optional';


--
-- Name: COLUMN observableproperty_t.disabled; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observableproperty_t.disabled IS 'For later use by the SOS. Indicator if this observableProperty should not be provided by the SOS.';


--
-- Name: observablepropertyid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE observablepropertyid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE observablepropertyid_seq OWNER TO observations_p;

--
-- Name: observation; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW observation AS
 SELECT r.id AS observationid,
    r.observation AS seriesid,
    r."time" AS phenomenontimestart,
    r."time" AS phenomenontimeend,
    r."time" AS resulttime,
    ((('http://ressource.brgm-rec.fr/obs/rawObservationPiezo/'::text || COALESCE((foi2.localidentifier)::text, (foi2.id)::text)) || '-'::text) || ((r.id)::character varying(255))::text) AS identifier,
    (1)::bigint AS codespace,
    NULL::character varying(255) AS name,
    NULL::bigint AS codespacename,
    NULL::bigint AS description,
    'F'::character(1) AS deleted,
    'F'::character(1) AS child,
    'F'::character(1) AS parent,
    NULL::timestamp without time zone AS validtimestart,
    NULL::timestamp without time zone AS validtimeend,
    foi2.geom AS samplinggeometry,
    r.uom AS unitid
   FROM ((rawdata.result r
     JOIN rawdata.observation ON ((observation.id = r.observation)))
     JOIN rawdata.foi2 ON ((foi2.id = observation.samplingfeature)))
  WHERE (r.parent IS NULL)
  WITH NO DATA;


ALTER TABLE observation OWNER TO observations_p;

--
-- Name: observation_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE observation_t (
    observationid bigint NOT NULL,
    seriesid bigint NOT NULL,
    phenomenontimestart timestamp without time zone NOT NULL,
    phenomenontimeend timestamp without time zone NOT NULL,
    resulttime timestamp without time zone NOT NULL,
    identifier character varying(255),
    codespace bigint,
    name character varying(255),
    codespacename bigint,
    description character varying(255),
    deleted character(1) DEFAULT 'F'::bpchar NOT NULL,
    child character(1) DEFAULT 'F'::bpchar NOT NULL,
    parent character(1) DEFAULT 'F'::bpchar NOT NULL,
    validtimestart timestamp without time zone,
    validtimeend timestamp without time zone,
    unitid bigint,
    samplinggeometry postgis.geometry,
    CONSTRAINT observation_child_check CHECK ((child = ANY (ARRAY['T'::bpchar, 'F'::bpchar]))),
    CONSTRAINT observation_deleted_check CHECK ((deleted = ANY (ARRAY['T'::bpchar, 'F'::bpchar]))),
    CONSTRAINT observation_parent_check CHECK ((parent = ANY (ARRAY['T'::bpchar, 'F'::bpchar])))
);


ALTER TABLE observation_t OWNER TO observations_p;

--
-- Name: TABLE observation_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE observation_t IS 'Stores the observations. Mapping file: mapping/series/observation/TemporalReferencedObservation.hbm.xml';


--
-- Name: COLUMN observation_t.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.observationid IS 'Table primary key, used in relations';


--
-- Name: COLUMN observation_t.seriesid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.seriesid IS 'Relation/foreign key to the associated series table. Contains "series".seriesId';


--
-- Name: COLUMN observation_t.phenomenontimestart; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.phenomenontimestart IS 'Time stamp when the observation was started or phenomenon was observed';


--
-- Name: COLUMN observation_t.phenomenontimeend; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.phenomenontimeend IS 'Time stamp when the observation was stopped or phenomenon was observed';


--
-- Name: COLUMN observation_t.resulttime; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.resulttime IS 'Time stamp when the observation was published or result was published/available';


--
-- Name: COLUMN observation_t.identifier; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.identifier IS 'The identifier of the observation, gml:identifier. Used as parameter for queries. Optional but unique';


--
-- Name: COLUMN observation_t.codespace; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.codespace IS 'Relation/foreign key to the codespace table. Contains the gml:identifier codespace. Optional';


--
-- Name: COLUMN observation_t.name; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.name IS 'The name of the observation, gml:name. Optional';


--
-- Name: COLUMN observation_t.codespacename; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.codespacename IS 'The name of the observation, gml:name. Optional';


--
-- Name: COLUMN observation_t.description; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.description IS 'Description of the observation, gml:description. Optional';


--
-- Name: COLUMN observation_t.deleted; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.deleted IS 'Flag to indicate that this observation is deleted or not (OGC SWES 2.0 - DeleteSensor operation or not specified DeleteObservation)';


--
-- Name: COLUMN observation_t.child; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.child IS 'Flag to indicate that this observation is a child observation for complex observation';


--
-- Name: COLUMN observation_t.parent; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.parent IS 'Flag to indicate that this observation is a parent observation for complex observation';


--
-- Name: COLUMN observation_t.validtimestart; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.validtimestart IS 'Start time stamp for which the observation/result is valid, e.g. used for forecasting. Optional';


--
-- Name: COLUMN observation_t.validtimeend; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.validtimeend IS 'End time stamp for which the observation/result is valid, e.g. used for forecasting. Optional';


--
-- Name: COLUMN observation_t.unitid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.unitid IS 'Foreign Key (FK) to the related unit of measure. Contains "unit".unitid. Optional';


--
-- Name: COLUMN observation_t.samplinggeometry; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observation_t.samplinggeometry IS 'Sampling geometry describes exactly where the measurement has taken place. Used for OGC SOS 2.0 Spatial Filtering Profile. Optional';


--
-- Name: procedure; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW procedure AS
 SELECT (1)::bigint AS procedureid,
    'F'::character(1) AS hibernatediscriminator,
    (2)::bigint AS proceduredescriptionformatid,
    'http://id.eaufrance.fr/met/403.xml'::character varying(255) AS identifier,
    (1)::bigint AS codespace,
    'Electronic piezometric probe'::character varying(255) AS name,
    NULL::character varying(255) AS codespacename,
    NULL::character varying(255) AS description,
    'F'::character(1) AS disabled,
    'F'::character(1) AS deleted,
    '<ompr:Process gml:id="Process.1"><ompr:inspireId><base:Identifier><base:localId>403</base:localId><base:namespace>http://id.eaufrance.fr/met/</base:namespace></base:Identifier></ompr:inspireId><ompr:name>Mesure de la profondeur piézométrique par sonde électronique </ompr:name><ompr:type>Electronic piezometric probe</ompr:type><ompr:responsibleParty><base2:RelatedParty><base2:organisationName><gco:CharacterString>SANDRE</gco:CharacterString></base2:organisationName><base2:role xlink:href="http://inspire.ec.europa.eu/codelist/RelatedPartyRoleValue/operator" xlink:title="operator"/></base2:RelatedParty></ompr:responsibleParty></ompr:Process>'::text AS descriptionfile,
    'F'::character(1) AS referenceflag,
    NULL::bigint AS typeof,
    'F'::character(1) AS istype,
    'T'::character(1) AS isaggregation,
    'F'::character(1) AS mobile,
    'T'::character(1) AS insitu
UNION
 SELECT (2)::bigint AS procedureid,
    'F'::character(1) AS hibernatediscriminator,
    (2)::bigint AS proceduredescriptionformatid,
    'urn:sandre:donnees:MET:FRA:code:403:::referentiel:2:xml'::character varying(255) AS identifier,
    (1)::bigint AS codespace,
    'Température'::character varying(255) AS name,
    NULL::character varying(255) AS codespacename,
    NULL::character varying(255) AS description,
    'F'::character(1) AS disabled,
    'F'::character(1) AS deleted,
    '<ompr:Process gml:id="Process.2"><ompr:inspireId><base:Identifier><base:localId>urn:sandre:donnees:79::CdElement:4:::referentiel:3.1:xml</base:localId><base:namespace>http://www.sandre.eaufrance.fr/?urn=</base:namespace></base:Identifier></ompr:inspireId><ompr:name>Enregistreur numérique télétransmis</ompr:name><ompr:type>Automated GroundWater level monitoring</ompr:type><ompr:responsibleParty><base2:RelatedParty><base2:organisationName><gco:CharacterString>SANDRE</gco:CharacterString></base2:organisationName><base2:role xlink:href="http://inspire.ec.europa.eu/codelist/RelatedPartyRoleValue/operator" xlink:title="operator"/></base2:RelatedParty></ompr:responsibleParty></ompr:Process>'::text AS descriptionfile,
    'F'::character(1) AS referenceflag,
    NULL::bigint AS typeof,
    'F'::character(1) AS istype,
    'T'::character(1) AS isaggregation,
    'F'::character(1) AS mobile,
    'T'::character(1) AS insitu
UNION
 SELECT (3)::bigint AS procedureid,
    'F'::character(1) AS hibernatediscriminator,
    (2)::bigint AS proceduredescriptionformatid,
    'urn:sandre:donnees:MET:FRA:code:403:::referentiel:1:xml'::character varying(255) AS identifier,
    (1)::bigint AS codespace,
    'Pluviométrie'::character varying(255) AS name,
    NULL::character varying(255) AS codespacename,
    NULL::character varying(255) AS description,
    'F'::character(1) AS disabled,
    'F'::character(1) AS deleted,
    '<ompr:Process gml:id="Process.1"><ompr:inspireId><base:Identifier><base:localId>urn:sandre:donnees:79::CdElement:4:::referentiel:3.1:xml</base:localId><base:namespace>http://www.sandre.eaufrance.fr/?urn=</base:namespace></base:Identifier></ompr:inspireId><ompr:name>Enregistreur numérique télétransmis</ompr:name><ompr:type>Automated GroundWater level monitoring</ompr:type><ompr:responsibleParty><base2:RelatedParty><base2:organisationName><gco:CharacterString>SANDRE</gco:CharacterString></base2:organisationName><base2:role xlink:href="http://inspire.ec.europa.eu/codelist/RelatedPartyRoleValue/operator" xlink:title="operator"/></base2:RelatedParty></ompr:responsibleParty></ompr:Process>'::text AS descriptionfile,
    'F'::character(1) AS referenceflag,
    NULL::bigint AS typeof,
    'F'::character(1) AS istype,
    'T'::character(1) AS isaggregation,
    'F'::character(1) AS mobile,
    'T'::character(1) AS insitu
  WITH NO DATA;


ALTER TABLE procedure OWNER TO observations_p;

--
-- Name: observationconstellation; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW observationconstellation AS
 SELECT row_number() OVER (ORDER BY observation.samplingfeature) AS observationconstellationid,
    observation.observedproperty AS observablepropertyid,
    procedure.procedureid,
    (1)::bigint AS observationtypeid,
    observation.samplingfeature AS offeringid,
    'F'::character(1) AS deleted,
    'F'::character(1) AS hiddenchild
   FROM rawdata.observation,
    procedure
  WHERE (observation.procedure = procedure.procedureid)
  ORDER BY observation.samplingfeature
  WITH NO DATA;


ALTER TABLE observationconstellation OWNER TO observations_p;

--
-- Name: observationconstellation_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE observationconstellation_t (
    observationconstellationid bigint NOT NULL,
    observablepropertyid bigint NOT NULL,
    procedureid bigint NOT NULL,
    observationtypeid bigint,
    offeringid bigint NOT NULL,
    deleted character(1) DEFAULT 'F'::bpchar NOT NULL,
    hiddenchild character(1) DEFAULT 'F'::bpchar NOT NULL,
    CONSTRAINT observationconstellation_deleted_check CHECK ((deleted = ANY (ARRAY['T'::bpchar, 'F'::bpchar]))),
    CONSTRAINT observationconstellation_hiddenchild_check CHECK ((hiddenchild = ANY (ARRAY['T'::bpchar, 'F'::bpchar])))
);


ALTER TABLE observationconstellation_t OWNER TO observations_p;

--
-- Name: TABLE observationconstellation_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE observationconstellation_t IS 'Table to store the ObservationConstellation information. Contains information about the constellation of observableProperty, procedure, offering and the observationType. Mapping file: mapping/core/ObservationConstellation.hbm.xml';


--
-- Name: COLUMN observationconstellation_t.observationconstellationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observationconstellation_t.observationconstellationid IS 'Table primary key, used for relations';


--
-- Name: COLUMN observationconstellation_t.observablepropertyid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observationconstellation_t.observablepropertyid IS 'Foreign Key (FK) to the related observableProperty. Contains "observableproperty".observablepropertyid';


--
-- Name: COLUMN observationconstellation_t.procedureid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observationconstellation_t.procedureid IS 'Foreign Key (FK) to the related procedure. Contains "procedure".procedureid';


--
-- Name: COLUMN observationconstellation_t.observationtypeid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observationconstellation_t.observationtypeid IS 'Foreign Key (FK) to the related observation type. Contains "observationtype".observationtypeid';


--
-- Name: COLUMN observationconstellation_t.offeringid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observationconstellation_t.offeringid IS 'Foreign Key (FK) to the related offering. Contains "offering".offeringid';


--
-- Name: COLUMN observationconstellation_t.deleted; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observationconstellation_t.deleted IS 'Flag to indicate that this observationConstellation is deleted or not. Set if the related procedure is deleted via DeleteSensor operation (OGC SWES 2.0 - DeleteSensor operation)';


--
-- Name: COLUMN observationconstellation_t.hiddenchild; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observationconstellation_t.hiddenchild IS 'Flag to indicate that this observationConstellations procedure is a child procedure of another procedure. If true, the related procedure is not contained in OGC SOS 2.0 Capabilities but in OGC SOS 1.0.0 Capabilities.';


--
-- Name: observationconstellationid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE observationconstellationid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE observationconstellationid_seq OWNER TO observations_p;

--
-- Name: observationhasoffering; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW observationhasoffering AS
 SELECT result.id AS observationid,
    observation.samplingfeature AS offeringid
   FROM rawdata.result,
    rawdata.observation
  WHERE (result.observation = observation.id)
  WITH NO DATA;


ALTER TABLE observationhasoffering OWNER TO observations_p;

--
-- Name: observationhasoffering_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE observationhasoffering_t (
    observationid bigint NOT NULL,
    offeringid bigint NOT NULL
);


ALTER TABLE observationhasoffering_t OWNER TO observations_p;

--
-- Name: COLUMN observationhasoffering_t.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observationhasoffering_t.observationid IS 'Foreign Key (FK) to the related observation. Contains "observation".observationid';


--
-- Name: COLUMN observationhasoffering_t.offeringid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observationhasoffering_t.offeringid IS 'Foreign Key (FK) to the related offering. Contains "offering".offeringid';


--
-- Name: observationid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE observationid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE observationid_seq OWNER TO observations_p;

--
-- Name: observationtype; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW observationtype AS
 SELECT (1)::bigint AS observationtypeid,
    'http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement'::character varying(255) AS observationtype
UNION
 SELECT (2)::bigint AS observationtypeid,
    'http://www.opengis.net/def/observationType/OGC-GWML/2.2/GW_GeologyLogCoverage'::character varying(255) AS observationtype
UNION
 SELECT (3)::bigint AS observationtypeid,
    'http://inspire.ec.europa.eu/featureconcept/PointObservation'::character varying(255) AS observationtype
UNION
 SELECT (4)::bigint AS observationtypeid,
    'http://inspire.ec.europa.eu/featureconcept/MultiPointObservation'::character varying(255) AS observationtype
UNION
 SELECT (5)::bigint AS observationtypeid,
    'http://inspire.ec.europa.eu/featureconcept/ProfileObservation'::character varying(255) AS observationtype
UNION
 SELECT (6)::bigint AS observationtypeid,
    'http://inspire.ec.europa.eu/featureconcept/TrajectoryObservation'::character varying(255) AS observationtype
UNION
 SELECT (7)::bigint AS observationtypeid,
    'http://inspire.ec.europa.eu/featureconcept/PointTimeSeriesObservation'::character varying(255) AS observationtype
  WITH NO DATA;


ALTER TABLE observationtype OWNER TO observations_p;

--
-- Name: observationtype_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE observationtype_t (
    observationtypeid bigint NOT NULL,
    observationtype character varying(255) NOT NULL
);


ALTER TABLE observationtype_t OWNER TO observations_p;

--
-- Name: TABLE observationtype_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE observationtype_t IS 'Table to store the observationTypes. Mapping file: mapping/core/ObservationType.hbm.xml';


--
-- Name: COLUMN observationtype_t.observationtypeid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observationtype_t.observationtypeid IS 'Table primary key, used for relations';


--
-- Name: COLUMN observationtype_t.observationtype; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN observationtype_t.observationtype IS 'The observationType value, e.g. http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement (OGC OM 2.0 specification) for OM_Measurement';


--
-- Name: observationtypeid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE observationtypeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE observationtypeid_seq OWNER TO observations_p;

--
-- Name: offering; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW offering AS
 SELECT foi2.id AS offeringid,
    'F'::character(1) AS hibernatediscriminator,
    ('http://ressource.brgm-rec.fr/obs/rawOfferingPiezo/'::text || COALESCE((foi2.localidentifier)::text, (foi2.id)::text)) AS identifier,
    NULL::bigint AS codespace,
    foi2.description AS name,
    NULL::character varying(255) AS codespacename,
    foi2.description,
    'F'::character(1) AS disabled
   FROM rawdata.foi2
  WHERE (foi2.sampledfeature IS NOT NULL)
  WITH NO DATA;


ALTER TABLE offering OWNER TO observations_p;

--
-- Name: offering_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE offering_t (
    offeringid bigint NOT NULL,
    hibernatediscriminator character(1) NOT NULL,
    identifier character varying(255) NOT NULL,
    codespace bigint,
    name character varying(255),
    codespacename bigint,
    description character varying(255),
    disabled character(1) DEFAULT 'F'::bpchar NOT NULL,
    CONSTRAINT offering_disabled_check CHECK ((disabled = ANY (ARRAY['T'::bpchar, 'F'::bpchar])))
);


ALTER TABLE offering_t OWNER TO observations_p;

--
-- Name: TABLE offering_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE offering_t IS 'Table to store the offering information. Mapping file: mapping/core/Offering.hbm.xml';


--
-- Name: COLUMN offering_t.offeringid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN offering_t.offeringid IS 'Table primary key, used for relations';


--
-- Name: COLUMN offering_t.identifier; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN offering_t.identifier IS 'The identifier of the offering, gml:identifier. Used as parameter for queries. Unique';


--
-- Name: COLUMN offering_t.codespace; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN offering_t.codespace IS 'Relation/foreign key to the codespace table. Contains the gml:identifier codespace. Optional';


--
-- Name: COLUMN offering_t.name; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN offering_t.name IS 'The name of the offering, gml:name. If available, displyed in the contents of the Capabilites. Optional';


--
-- Name: COLUMN offering_t.codespacename; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN offering_t.codespacename IS 'Relation/foreign key to the codespace table. Contains the gml:name codespace. Optional';


--
-- Name: COLUMN offering_t.description; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN offering_t.description IS 'Description of the offering, gml:description. Optional';


--
-- Name: COLUMN offering_t.disabled; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN offering_t.disabled IS 'For later use by the SOS. Indicator if this offering should not be provided by the SOS.';


--
-- Name: offeringallowedobservationtype; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW offeringallowedobservationtype AS
 SELECT offering.offeringid,
    (7)::bigint AS observationtypeid
   FROM offering
UNION
 SELECT offering.offeringid,
    (1)::bigint AS observationtypeid
   FROM offering
  WITH NO DATA;


ALTER TABLE offeringallowedobservationtype OWNER TO observations_p;

--
-- Name: offeringallowedobservationtype_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE offeringallowedobservationtype_t (
    offeringid bigint NOT NULL,
    observationtypeid bigint NOT NULL
);


ALTER TABLE offeringallowedobservationtype_t OWNER TO observations_p;

--
-- Name: TABLE offeringallowedobservationtype_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE offeringallowedobservationtype_t IS 'Table to store relations between offering and allowed observationTypes, defined in InsertSensor request. Mapping file: mapping/transactional/TOffering.hbm.xml';


--
-- Name: COLUMN offeringallowedobservationtype_t.offeringid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN offeringallowedobservationtype_t.offeringid IS 'Foreign Key (FK) to the related offering. Contains "offering".offeringid';


--
-- Name: COLUMN offeringallowedobservationtype_t.observationtypeid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN offeringallowedobservationtype_t.observationtypeid IS 'Foreign Key (FK) to the related observationType. Contains "observationType".observationTypeId';


--
-- Name: offeringid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE offeringid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE offeringid_seq OWNER TO observations_p;

--
-- Name: offeringrelation; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE offeringrelation (
    parentofferingid bigint NOT NULL,
    childofferingid bigint NOT NULL
);


ALTER TABLE offeringrelation OWNER TO observations_p;

--
-- Name: TABLE offeringrelation; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE offeringrelation IS 'Relation table to store offering hierarchies. E.g. define a parent in a query and all childs are also contained in the response. Mapping file: mapping/core/Offering.hbm.xml';


--
-- Name: COLUMN offeringrelation.parentofferingid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN offeringrelation.parentofferingid IS 'Foreign Key (FK) to the related parent offering. Contains "offering".offeringid';


--
-- Name: COLUMN offeringrelation.childofferingid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN offeringrelation.childofferingid IS 'Foreign Key (FK) to the related child offering. Contains "offering".offeringid';


--
-- Name: parameter; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW parameter AS
 SELECT (1)::bigint AS parameterid,
    result.id AS observationid,
    'observationStatus'::character varying(255) AS name
   FROM rawdata.result
UNION
 SELECT (2)::bigint AS parameterid,
    result.id AS observationid,
    'observationQualification'::character varying(255) AS name
   FROM rawdata.result
  WITH NO DATA;


ALTER TABLE parameter OWNER TO observations_p;

--
-- Name: parameter_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE parameter_t (
    parameterid bigint NOT NULL,
    observationid bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE parameter_t OWNER TO observations_p;

--
-- Name: TABLE parameter_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE parameter_t IS 'Table to store additional obervation information (om:parameter). Mapping file: mapping/core/Parameter.hbm.xml';


--
-- Name: COLUMN parameter_t.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN parameter_t.parameterid IS 'Table primary key';


--
-- Name: COLUMN parameter_t.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN parameter_t.observationid IS 'Foreign Key (FK) to the related observation. Contains "observation".observationId';


--
-- Name: COLUMN parameter_t.name; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN parameter_t.name IS 'Parameter name';


--
-- Name: parameterid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE parameterid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE parameterid_seq OWNER TO observations_p;

--
-- Name: procdescformatid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE procdescformatid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE procdescformatid_seq OWNER TO observations_p;

--
-- Name: procedure_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE procedure_t (
    procedureid bigint NOT NULL,
    hibernatediscriminator character(1) NOT NULL,
    proceduredescriptionformatid bigint NOT NULL,
    identifier character varying(255) NOT NULL,
    codespace bigint,
    name character varying(255),
    codespacename bigint,
    description character varying(255),
    deleted character(1) DEFAULT 'F'::bpchar NOT NULL,
    disabled character(1) DEFAULT 'F'::bpchar NOT NULL,
    descriptionfile text,
    referenceflag character(1) DEFAULT 'F'::bpchar,
    typeof bigint,
    istype character(1) DEFAULT 'F'::bpchar,
    isaggregation character(1) DEFAULT 'T'::bpchar,
    mobile character(1) DEFAULT 'F'::bpchar,
    insitu character(1) DEFAULT 'T'::bpchar,
    CONSTRAINT procedure_deleted_check CHECK ((deleted = ANY (ARRAY['T'::bpchar, 'F'::bpchar]))),
    CONSTRAINT procedure_disabled_check CHECK ((disabled = ANY (ARRAY['T'::bpchar, 'F'::bpchar]))),
    CONSTRAINT procedure_insitu_check CHECK ((insitu = ANY (ARRAY['T'::bpchar, 'F'::bpchar]))),
    CONSTRAINT procedure_isaggregation_check CHECK ((isaggregation = ANY (ARRAY['T'::bpchar, 'F'::bpchar]))),
    CONSTRAINT procedure_istype_check CHECK ((istype = ANY (ARRAY['T'::bpchar, 'F'::bpchar]))),
    CONSTRAINT procedure_mobile_check CHECK ((mobile = ANY (ARRAY['T'::bpchar, 'F'::bpchar]))),
    CONSTRAINT procedure_referenceflag_check CHECK ((referenceflag = ANY (ARRAY['T'::bpchar, 'F'::bpchar])))
);


ALTER TABLE procedure_t OWNER TO observations_p;

--
-- Name: TABLE procedure_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE procedure_t IS 'Table to store the procedure/sensor. Mapping file: mapping/core/Procedure.hbm.xml';


--
-- Name: COLUMN procedure_t.procedureid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.procedureid IS 'Table primary key, used for relations';


--
-- Name: COLUMN procedure_t.proceduredescriptionformatid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.proceduredescriptionformatid IS 'Relation/foreign key to the procedureDescriptionFormat table. Describes the format of the procedure description.';


--
-- Name: COLUMN procedure_t.identifier; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.identifier IS 'The identifier of the procedure, gml:identifier. Used as parameter for queries. Unique';


--
-- Name: COLUMN procedure_t.codespace; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.codespace IS 'Relation/foreign key to the codespace table. Contains the gml:identifier codespace. Optional';


--
-- Name: COLUMN procedure_t.name; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.name IS 'The name of the procedure, gml:name. Optional';


--
-- Name: COLUMN procedure_t.codespacename; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.codespacename IS 'Relation/foreign key to the codespace table. Contains the gml:name codespace. Optional';


--
-- Name: COLUMN procedure_t.description; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.description IS 'Description of the procedure, gml:description. Optional';


--
-- Name: COLUMN procedure_t.deleted; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.deleted IS 'Flag to indicate that this procedure is deleted or not (OGC SWES 2.0 - DeleteSensor operation)';


--
-- Name: COLUMN procedure_t.disabled; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.disabled IS 'For later use by the SOS. Indicator if this procedure should not be provided by the SOS.';


--
-- Name: COLUMN procedure_t.descriptionfile; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.descriptionfile IS 'Field for full (XML) encoded procedure description or link to a procedure description file. Optional';


--
-- Name: COLUMN procedure_t.referenceflag; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.referenceflag IS 'Flag to indicate that this procedure is a reference procedure of another procedure. Not used by the SOS but by the Sensor Web REST-API';


--
-- Name: COLUMN procedure_t.typeof; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.typeof IS 'Relation/foreign key to procedure table. Optional, contains procedureId if this procedure is typeOf another procedure';


--
-- Name: COLUMN procedure_t.istype; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.istype IS 'Flag to indicate that this procedure is a type description, has no observations.';


--
-- Name: COLUMN procedure_t.isaggregation; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.isaggregation IS 'Flag to indicate that this procedure is an aggregation (e.g. System, PhysicalSystem).';


--
-- Name: COLUMN procedure_t.mobile; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.mobile IS 'Flag to indicate that this procedure is stationary (false) or mobile (true). Optional';


--
-- Name: COLUMN procedure_t.insitu; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN procedure_t.insitu IS 'Flag to indicate that this procedure is insitu (true) or remote (false). Optional';


--
-- Name: proceduredescriptionformat; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW proceduredescriptionformat AS
 SELECT (1)::bigint AS proceduredescriptionformatid,
    'http://www.opengis.net/waterml/2.0/observationProcess'::character varying(255) AS proceduredescriptionformat
UNION
 SELECT (2)::bigint AS proceduredescriptionformatid,
    'http://inspire.ec.europa.eu/featureconcept/Process'::character varying(255) AS proceduredescriptionformat
  WITH NO DATA;


ALTER TABLE proceduredescriptionformat OWNER TO observations_p;

--
-- Name: proceduredescriptionformat_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE proceduredescriptionformat_t (
    proceduredescriptionformatid bigint NOT NULL,
    proceduredescriptionformat character varying(255) NOT NULL
);


ALTER TABLE proceduredescriptionformat_t OWNER TO observations_p;

--
-- Name: TABLE proceduredescriptionformat_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE proceduredescriptionformat_t IS 'Table to store the ProcedureDescriptionFormat information of procedures. Mapping file: mapping/core/ProcedureDescriptionFormat.hbm.xml';


--
-- Name: COLUMN proceduredescriptionformat_t.proceduredescriptionformatid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN proceduredescriptionformat_t.proceduredescriptionformatid IS 'Table primary key, used for relations';


--
-- Name: COLUMN proceduredescriptionformat_t.proceduredescriptionformat; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN proceduredescriptionformat_t.proceduredescriptionformat IS 'The procedureDescriptionFormat value, e.g. http://www.opengis.net/sensorML/1.0.1 for procedures descriptions as specified in OGC SensorML 1.0.1';


--
-- Name: procedureid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE procedureid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE procedureid_seq OWNER TO observations_p;

--
-- Name: profileobservation; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE profileobservation (
    observationid bigint NOT NULL,
    childobservationid bigint NOT NULL
);


ALTER TABLE profileobservation OWNER TO observations_p;

--
-- Name: TABLE profileobservation; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE profileobservation IS 'Relation table for complex parent/child observations';


--
-- Name: COLUMN profileobservation.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN profileobservation.observationid IS 'Foreign Key (FK) to the related parent complex observation. Contains "observation".observationid';


--
-- Name: COLUMN profileobservation.childobservationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN profileobservation.childobservationid IS 'Foreign Key (FK) to the related child complex observation. Contains "observation".observationid';


--
-- Name: profilevalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE profilevalue (
    observationid bigint NOT NULL,
    fromlevel double precision,
    tolevel double precision,
    levelunitid bigint
);


ALTER TABLE profilevalue OWNER TO observations_p;

--
-- Name: TABLE profilevalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE profilevalue IS 'Value table for profile observation';


--
-- Name: COLUMN profilevalue.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN profilevalue.observationid IS 'Foreign Key (FK) to the related observation from the observation table. Contains "observation".observationid';


--
-- Name: COLUMN profilevalue.fromlevel; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN profilevalue.fromlevel IS 'Value of fromLevel';


--
-- Name: COLUMN profilevalue.tolevel; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN profilevalue.tolevel IS 'Value of toLevel';


--
-- Name: COLUMN profilevalue.levelunitid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN profilevalue.levelunitid IS 'Foreign Key (FK) to the related unit of measure. Contains "unit".unitid. Optional';


--
-- Name: relatedobservation; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE relatedobservation (
    relatedobservationid bigint NOT NULL,
    observationid bigint,
    relatedobservation bigint,
    role character varying(255),
    relatedurl character varying(255)
);


ALTER TABLE relatedobservation OWNER TO observations_p;

--
-- Name: TABLE relatedobservation; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE relatedobservation IS 'The related observation of an observation.';


--
-- Name: COLUMN relatedobservation.relatedobservationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN relatedobservation.relatedobservationid IS 'Table primary key';


--
-- Name: COLUMN relatedobservation.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN relatedobservation.observationid IS 'Foreign Key (FK) to the observation. Contains "observation".observationId';


--
-- Name: COLUMN relatedobservation.relatedobservation; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN relatedobservation.relatedobservation IS 'Relation/foreign key to the associated observation table. Contains "observation".observationId';


--
-- Name: COLUMN relatedobservation.role; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN relatedobservation.role IS 'The role of the relation';


--
-- Name: COLUMN relatedobservation.relatedurl; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN relatedobservation.relatedurl IS 'An url to a related observation';


--
-- Name: relatedobservationid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE relatedobservationid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE relatedobservationid_seq OWNER TO observations_p;

--
-- Name: relatedseries; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE relatedseries (
    relationid bigint NOT NULL,
    seriesid bigint NOT NULL,
    relatedseries bigint,
    role character varying(255),
    relatedurl character varying(255)
);


ALTER TABLE relatedseries OWNER TO observations_p;

--
-- Name: TABLE relatedseries; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE relatedseries IS 'The series relation should be used if the series table represents a timeseries.';


--
-- Name: COLUMN relatedseries.relationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN relatedseries.relationid IS 'Table primary key';


--
-- Name: COLUMN relatedseries.seriesid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN relatedseries.seriesid IS 'Foreign Key (FK) to the series. Contains "series".seriesId';


--
-- Name: COLUMN relatedseries.relatedseries; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN relatedseries.relatedseries IS 'Relation/foreign key to the associated series table. Contains "series".seriesId';


--
-- Name: COLUMN relatedseries.role; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN relatedseries.role IS 'The role of the relation';


--
-- Name: COLUMN relatedseries.relatedurl; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN relatedseries.relatedurl IS 'An url to a related observation';


--
-- Name: sensorsystem; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE sensorsystem (
    parentsensorid bigint NOT NULL,
    childsensorid bigint NOT NULL
);


ALTER TABLE sensorsystem OWNER TO observations_p;

--
-- Name: TABLE sensorsystem; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE sensorsystem IS 'Relation table to store procedure hierarchies. E.g. define a parent in a query and all childs are also contained in the response. Mapping file: mapping/transactional/TProcedure.hbm.xml';


--
-- Name: COLUMN sensorsystem.parentsensorid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN sensorsystem.parentsensorid IS 'Foreign Key (FK) to the related parent procedure. Contains "procedure".procedureid';


--
-- Name: COLUMN sensorsystem.childsensorid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN sensorsystem.childsensorid IS 'Foreign Key (FK) to the related child procedure. Contains "procedure".procedureid';


--
-- Name: series_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE series_t (
    seriesid bigint NOT NULL,
    featureofinterestid bigint NOT NULL,
    observablepropertyid bigint NOT NULL,
    procedureid bigint NOT NULL,
    offeringid bigint,
    deleted character(1) DEFAULT 'F'::bpchar NOT NULL,
    published character(1) DEFAULT 'T'::bpchar NOT NULL,
    hiddenchild character(1) DEFAULT 'F'::bpchar NOT NULL,
    firsttimestamp timestamp without time zone,
    lasttimestamp timestamp without time zone,
    firstnumericvalue double precision,
    lastnumericvalue double precision,
    unitid bigint,
    identifier character varying(255),
    codespace bigint,
    name character varying(255),
    codespacename bigint,
    description character varying(255),
    seriestype character varying(255),
    CONSTRAINT series_deleted_check CHECK ((deleted = ANY (ARRAY['T'::bpchar, 'F'::bpchar]))),
    CONSTRAINT series_hiddenchild_check CHECK ((hiddenchild = ANY (ARRAY['T'::bpchar, 'F'::bpchar]))),
    CONSTRAINT series_published_check CHECK ((published = ANY (ARRAY['T'::bpchar, 'F'::bpchar])))
);


ALTER TABLE series_t OWNER TO observations_p;

--
-- Name: TABLE series_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE series_t IS 'Table to store a (time-) series which consists of featureOfInterest, observableProperty, and procedure. Mapping file: mapping/series/Series.hbm.xml';


--
-- Name: COLUMN series_t.seriesid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.seriesid IS 'Table primary key, used for relations';


--
-- Name: COLUMN series_t.featureofinterestid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.featureofinterestid IS 'Foreign Key (FK) to the related featureOfInterest. Contains "featureOfInterest".featureOfInterestId';


--
-- Name: COLUMN series_t.observablepropertyid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.observablepropertyid IS 'Foreign Key (FK) to the related observableProperty. Contains "observableproperty".observablepropertyid';


--
-- Name: COLUMN series_t.procedureid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.procedureid IS 'Foreign Key (FK) to the related procedure. Contains "procedure".procedureid';


--
-- Name: COLUMN series_t.offeringid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.offeringid IS 'Foreign Key (FK) to the related offering. Contains "offering".offeringid';


--
-- Name: COLUMN series_t.deleted; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.deleted IS 'Flag to indicate that this series is deleted or not. Set if the related procedure is deleted via DeleteSensor operation (OGC SWES 2.0 - DeleteSensor operation)';


--
-- Name: COLUMN series_t.published; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.published IS 'Flag to indicate that this series is published or not. A not published series is not contained in GetObservation and GetDataAvailability responses';


--
-- Name: COLUMN series_t.hiddenchild; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.hiddenchild IS 'TODO';


--
-- Name: COLUMN series_t.firsttimestamp; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.firsttimestamp IS 'The time stamp of the first (temporal) observation associated to this series';


--
-- Name: COLUMN series_t.lasttimestamp; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.lasttimestamp IS 'The time stamp of the last (temporal) observation associated to this series';


--
-- Name: COLUMN series_t.firstnumericvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.firstnumericvalue IS 'The value of the first (temporal) observation associated to this series';


--
-- Name: COLUMN series_t.lastnumericvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.lastnumericvalue IS 'The value of the last (temporal) observation associated to this series';


--
-- Name: COLUMN series_t.unitid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.unitid IS 'Foreign Key (FK) to the related unit of the first/last numeric values . Contains "unit".unitid';


--
-- Name: COLUMN series_t.identifier; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.identifier IS 'The identifier of the series, gml:identifier. Unique';


--
-- Name: COLUMN series_t.codespace; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.codespace IS 'Relation/foreign key to the codespace table. Contains the gml:identifier codespace. Optional';


--
-- Name: COLUMN series_t.name; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.name IS 'The name of the series, gml:name. Optional';


--
-- Name: COLUMN series_t.codespacename; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.codespacename IS 'Relation/foreign key to the codespace table. Contains the gml:name codespace. Optional';


--
-- Name: COLUMN series_t.description; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.description IS 'Description of the series, gml:description. Optional';


--
-- Name: COLUMN series_t.seriestype; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN series_t.seriestype IS 'Definition of the series type, e.g. measurement for OM_Measurement. Optional';


--
-- Name: seriesid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE seriesid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seriesid_seq OWNER TO observations_p;

--
-- Name: seriesmetadata; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE seriesmetadata (
    metadataid bigint NOT NULL,
    seriesid bigint NOT NULL,
    identifier character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    domain character varying(255) NOT NULL
);


ALTER TABLE seriesmetadata OWNER TO observations_p;

--
-- Name: COLUMN seriesmetadata.metadataid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN seriesmetadata.metadataid IS 'Table primary key, used for relations';


--
-- Name: COLUMN seriesmetadata.seriesid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN seriesmetadata.seriesid IS 'Foreign Key (FK) to the related series. Contains "series".seriesId';


--
-- Name: COLUMN seriesmetadata.identifier; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN seriesmetadata.identifier IS 'The identifier of the metadata value.';


--
-- Name: COLUMN seriesmetadata.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN seriesmetadata.value IS 'The metadata value.';


--
-- Name: COLUMN seriesmetadata.domain; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN seriesmetadata.domain IS 'The metadata value domain.';


--
-- Name: seriesparameter; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW seriesparameter AS
 SELECT q.parameterid,
    q.parameterid AS seriesid,
    'RelatedMonitoringFeature'::character varying(255) AS name
   FROM ( SELECT series.seriesid AS parameterid,
            featureofinterest.identifier AS value,
            NULL::bigint AS unitid
           FROM (series
             JOIN featureofinterest ON ((series.featureofinterestid = featureofinterest.featureofinterestid)))) q
  WITH NO DATA;


ALTER TABLE seriesparameter OWNER TO observations_p;

--
-- Name: seriesparameter_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE seriesparameter_t (
    parameterid bigint NOT NULL,
    seriesid bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE seriesparameter_t OWNER TO observations_p;

--
-- Name: TABLE seriesparameter_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE seriesparameter_t IS 'Table to store additional obervation information (om:parameter). Mapping file: mapping/series/metadata/SeriesParameter.hbm.xml';


--
-- Name: COLUMN seriesparameter_t.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN seriesparameter_t.parameterid IS 'Table primary key';


--
-- Name: COLUMN seriesparameter_t.seriesid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN seriesparameter_t.seriesid IS 'Foreign Key (FK) to the related series. Contains "series".seriesId';


--
-- Name: COLUMN seriesparameter_t.name; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN seriesparameter_t.name IS 'Parameter name';


--
-- Name: seriesrelationid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE seriesrelationid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seriesrelationid_seq OWNER TO observations_p;

--
-- Name: swedataarrayvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE swedataarrayvalue (
    observationid bigint NOT NULL,
    value text
);


ALTER TABLE swedataarrayvalue OWNER TO observations_p;

--
-- Name: TABLE swedataarrayvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE swedataarrayvalue IS 'Value table for SweDataArray observation';


--
-- Name: COLUMN swedataarrayvalue.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN swedataarrayvalue.observationid IS 'Foreign Key (FK) to the related observation from the observation table. Contains "observation".observationid';


--
-- Name: COLUMN swedataarrayvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN swedataarrayvalue.value IS 'SweDataArray observation value';


--
-- Name: textfeatparamvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE textfeatparamvalue (
    parameterid bigint NOT NULL,
    value character varying(255)
);


ALTER TABLE textfeatparamvalue OWNER TO observations_p;

--
-- Name: TABLE textfeatparamvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE textfeatparamvalue IS 'Value table for text parameter';


--
-- Name: COLUMN textfeatparamvalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN textfeatparamvalue.parameterid IS 'Foreign Key (FK) to the related parameter from the parameter table. Contains "featureParameter".parameterid';


--
-- Name: COLUMN textfeatparamvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN textfeatparamvalue.value IS 'Text parameter value';


--
-- Name: textparametervalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE textparametervalue (
    parameterid bigint NOT NULL,
    value character varying(255)
);


ALTER TABLE textparametervalue OWNER TO observations_p;

--
-- Name: TABLE textparametervalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE textparametervalue IS 'Value table for text parameter';


--
-- Name: COLUMN textparametervalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN textparametervalue.parameterid IS 'Foreign Key (FK) to the related parameter from the parameter table. Contains "parameter".parameterid';


--
-- Name: COLUMN textparametervalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN textparametervalue.value IS 'Text parameter value';


--
-- Name: textseriesparamvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE textseriesparamvalue (
    parameterid bigint NOT NULL,
    value character varying(255)
);


ALTER TABLE textseriesparamvalue OWNER TO observations_p;

--
-- Name: TABLE textseriesparamvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE textseriesparamvalue IS 'Value table for text parameter';


--
-- Name: COLUMN textseriesparamvalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN textseriesparamvalue.parameterid IS 'Foreign Key (FK) to the related parameter from the series parameter table. Contains seriesparameter.parameterid';


--
-- Name: COLUMN textseriesparamvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN textseriesparamvalue.value IS 'Text parameter value';


--
-- Name: textvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE textvalue (
    observationid bigint NOT NULL,
    value text,
    identifier character varying(255),
    name character varying(255),
    description character varying(255)
);


ALTER TABLE textvalue OWNER TO observations_p;

--
-- Name: TABLE textvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE textvalue IS 'Value table for text observation';


--
-- Name: COLUMN textvalue.observationid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN textvalue.observationid IS 'Foreign Key (FK) to the related observation from the observation table. Contains "observation".observationid';


--
-- Name: COLUMN textvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN textvalue.value IS 'Text observation value';


--
-- Name: COLUMN textvalue.identifier; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN textvalue.identifier IS 'SweText observation identifier';


--
-- Name: COLUMN textvalue.name; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN textvalue.name IS 'SweText observation name';


--
-- Name: COLUMN textvalue.description; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN textvalue.description IS 'SweText observation description';


--
-- Name: unit; Type: MATERIALIZED VIEW; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE MATERIALIZED VIEW unit AS
 SELECT unitofmeasure.id AS unitid,
    (unitofmeasure.unit)::character varying(255) AS unit,
    NULL::character varying(255) AS name,
    NULL::character varying(255) AS link
   FROM rawdata.unitofmeasure
  WITH NO DATA;


ALTER TABLE unit OWNER TO observations_p;

--
-- Name: unit_t; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE unit_t (
    unitid bigint NOT NULL,
    unit character varying(255) NOT NULL,
    name character varying(255),
    link character varying(255)
);


ALTER TABLE unit_t OWNER TO observations_p;

--
-- Name: TABLE unit_t; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE unit_t IS 'Table to store the unit of measure information, used in observations. Mapping file: mapping/core/Unit.hbm.xml';


--
-- Name: COLUMN unit_t.unitid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN unit_t.unitid IS 'Table primary key, used for relations';


--
-- Name: COLUMN unit_t.unit; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN unit_t.unit IS 'The unit of measure of observations. See http://unitsofmeasure.org/ucum.html';


--
-- Name: COLUMN unit_t.name; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN unit_t.name IS 'The name of the unit of measure of observations';


--
-- Name: COLUMN unit_t.link; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN unit_t.link IS 'The link to an external description of the unit of measure of observations, e.g. a vocabulary';


--
-- Name: unitid_seq; Type: SEQUENCE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE SEQUENCE unitid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE unitid_seq OWNER TO observations_p;

--
-- Name: xmlfeatparamvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE xmlfeatparamvalue (
    parameterid bigint NOT NULL,
    value text
);


ALTER TABLE xmlfeatparamvalue OWNER TO observations_p;

--
-- Name: TABLE xmlfeatparamvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE xmlfeatparamvalue IS 'Value table for XML parameter';


--
-- Name: COLUMN xmlfeatparamvalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN xmlfeatparamvalue.parameterid IS 'Foreign Key (FK) to the related parameter from the parameter table. Contains "featureParameter".parameterid';


--
-- Name: COLUMN xmlfeatparamvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN xmlfeatparamvalue.value IS 'XML parameter value';


--
-- Name: xmlparametervalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE xmlparametervalue (
    parameterid bigint NOT NULL,
    value text
);


ALTER TABLE xmlparametervalue OWNER TO observations_p;

--
-- Name: TABLE xmlparametervalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE xmlparametervalue IS 'Value table for XML parameter';


--
-- Name: COLUMN xmlparametervalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN xmlparametervalue.parameterid IS 'Foreign Key (FK) to the related parameter from the parameter table. Contains "parameter".parameterid';


--
-- Name: COLUMN xmlparametervalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN xmlparametervalue.value IS 'XML parameter value';


--
-- Name: xmlseriesparamvalue; Type: TABLE; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE TABLE xmlseriesparamvalue (
    parameterid bigint NOT NULL,
    value text
);


ALTER TABLE xmlseriesparamvalue OWNER TO observations_p;

--
-- Name: TABLE xmlseriesparamvalue; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON TABLE xmlseriesparamvalue IS 'Value table for XML parameter';


--
-- Name: COLUMN xmlseriesparamvalue.parameterid; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN xmlseriesparamvalue.parameterid IS 'Foreign Key (FK) to the related parameter from the series parameter table. Contains seriesparameter.parameterid';


--
-- Name: COLUMN xmlseriesparamvalue.value; Type: COMMENT; Schema: sos_rawdb_2807; Owner: observations_p
--

COMMENT ON COLUMN xmlseriesparamvalue.value IS 'XML parameter value';


--
-- Data for Name: blobvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY blobvalue (observationid, value) FROM stdin;
\.


--
-- Data for Name: booleanfeatparamvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY booleanfeatparamvalue (parameterid, value) FROM stdin;
\.


--
-- Data for Name: booleanparametervalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY booleanparametervalue (parameterid, value) FROM stdin;
\.


--
-- Data for Name: booleanseriesparamvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY booleanseriesparamvalue (parameterid, value) FROM stdin;
\.


--
-- Data for Name: booleanvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY booleanvalue (observationid, value) FROM stdin;
\.


--
-- Data for Name: categoryfeatparamvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY categoryfeatparamvalue (parameterid, value, unitid) FROM stdin;
\.


--
-- Data for Name: categoryparametervalue_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY categoryparametervalue_t (parameterid, value, unitid) FROM stdin;
\.


--
-- Data for Name: categoryseriesparamvalue_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY categoryseriesparamvalue_t (parameterid, value, unitid) FROM stdin;
\.


--
-- Data for Name: categoryvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY categoryvalue (observationid, value, identifier, name, description) FROM stdin;
\.


--
-- Data for Name: codespace_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY codespace_t (codespaceid, codespace) FROM stdin;
\.


--
-- Name: codespaceid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('codespaceid_seq', 1, false);


--
-- Data for Name: complexvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY complexvalue (observationid) FROM stdin;
\.


--
-- Data for Name: compositeobservation; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY compositeobservation (observationid, childobservationid) FROM stdin;
\.


--
-- Data for Name: compositephenomenon; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY compositephenomenon (parentobservablepropertyid, childobservablepropertyid) FROM stdin;
\.


--
-- Data for Name: countfeatparamvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY countfeatparamvalue (parameterid, value) FROM stdin;
\.


--
-- Data for Name: countparametervalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY countparametervalue (parameterid, value) FROM stdin;
\.


--
-- Data for Name: countseriesparamvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY countseriesparamvalue (parameterid, value) FROM stdin;
\.


--
-- Data for Name: countvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY countvalue (observationid, value) FROM stdin;
\.


--
-- Data for Name: featureofinterest_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY featureofinterest_t (featureofinterestid, hibernatediscriminator, featureofinteresttypeid, identifier, codespace, name, codespacename, description, geom, descriptionxml, url) FROM stdin;
\.


--
-- Name: featureofinterestid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('featureofinterestid_seq', 1, false);


--
-- Data for Name: featureofinteresttype_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY featureofinteresttype_t (featureofinteresttypeid, featureofinteresttype) FROM stdin;
\.


--
-- Name: featureofinteresttypeid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('featureofinteresttypeid_seq', 1, false);


--
-- Data for Name: featureparameter; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY featureparameter (parameterid, featureofinterestid, name) FROM stdin;
\.


--
-- Data for Name: featurerelation_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY featurerelation_t (parentfeatureid, childfeatureid) FROM stdin;
\.


--
-- Data for Name: geometryvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY geometryvalue (observationid, value) FROM stdin;
\.


--
-- Name: metadataid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('metadataid_seq', 1, false);


--
-- Data for Name: numericfeatparamvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY numericfeatparamvalue (parameterid, value, unitid) FROM stdin;
\.


--
-- Data for Name: numericparametervalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY numericparametervalue (parameterid, value, unitid) FROM stdin;
\.


--
-- Data for Name: numericseriesparamvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY numericseriesparamvalue (parameterid, value, unitid) FROM stdin;
\.


--
-- Data for Name: numericvalue_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY numericvalue_t (observationid, value) FROM stdin;
\.


--
-- Data for Name: observableproperty_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY observableproperty_t (observablepropertyid, identifier, codespace, name, codespacename, description, disabled, hiddenchild) FROM stdin;
\.


--
-- Name: observablepropertyid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('observablepropertyid_seq', 1, false);


--
-- Data for Name: observation_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY observation_t (observationid, seriesid, phenomenontimestart, phenomenontimeend, resulttime, identifier, codespace, name, codespacename, description, deleted, child, parent, validtimestart, validtimeend, unitid, samplinggeometry) FROM stdin;
\.


--
-- Data for Name: observationconstellation_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY observationconstellation_t (observationconstellationid, observablepropertyid, procedureid, observationtypeid, offeringid, deleted, hiddenchild) FROM stdin;
\.


--
-- Name: observationconstellationid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('observationconstellationid_seq', 1, false);


--
-- Data for Name: observationhasoffering_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY observationhasoffering_t (observationid, offeringid) FROM stdin;
\.


--
-- Name: observationid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('observationid_seq', 1, false);


--
-- Data for Name: observationtype_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY observationtype_t (observationtypeid, observationtype) FROM stdin;
\.


--
-- Name: observationtypeid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('observationtypeid_seq', 1, false);


--
-- Data for Name: offering_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY offering_t (offeringid, hibernatediscriminator, identifier, codespace, name, codespacename, description, disabled) FROM stdin;
\.


--
-- Data for Name: offeringallowedobservationtype_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY offeringallowedobservationtype_t (offeringid, observationtypeid) FROM stdin;
\.


--
-- Name: offeringid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('offeringid_seq', 1, false);


--
-- Data for Name: offeringrelation; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY offeringrelation (parentofferingid, childofferingid) FROM stdin;
\.


--
-- Data for Name: parameter_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY parameter_t (parameterid, observationid, name) FROM stdin;
\.


--
-- Name: parameterid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('parameterid_seq', 1, false);


--
-- Name: procdescformatid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('procdescformatid_seq', 1, false);


--
-- Data for Name: procedure_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY procedure_t (procedureid, hibernatediscriminator, proceduredescriptionformatid, identifier, codespace, name, codespacename, description, deleted, disabled, descriptionfile, referenceflag, typeof, istype, isaggregation, mobile, insitu) FROM stdin;
\.


--
-- Data for Name: proceduredescriptionformat_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY proceduredescriptionformat_t (proceduredescriptionformatid, proceduredescriptionformat) FROM stdin;
\.


--
-- Name: procedureid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('procedureid_seq', 1, false);


--
-- Data for Name: profileobservation; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY profileobservation (observationid, childobservationid) FROM stdin;
\.


--
-- Data for Name: profilevalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY profilevalue (observationid, fromlevel, tolevel, levelunitid) FROM stdin;
\.


--
-- Data for Name: relatedobservation; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY relatedobservation (relatedobservationid, observationid, relatedobservation, role, relatedurl) FROM stdin;
\.


--
-- Name: relatedobservationid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('relatedobservationid_seq', 1, false);


--
-- Data for Name: relatedseries; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY relatedseries (relationid, seriesid, relatedseries, role, relatedurl) FROM stdin;
\.


--
-- Data for Name: sensorsystem; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY sensorsystem (parentsensorid, childsensorid) FROM stdin;
\.


--
-- Data for Name: series_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY series_t (seriesid, featureofinterestid, observablepropertyid, procedureid, offeringid, deleted, published, hiddenchild, firsttimestamp, lasttimestamp, firstnumericvalue, lastnumericvalue, unitid, identifier, codespace, name, codespacename, description, seriestype) FROM stdin;
\.


--
-- Name: seriesid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('seriesid_seq', 1, false);


--
-- Data for Name: seriesmetadata; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY seriesmetadata (metadataid, seriesid, identifier, value, domain) FROM stdin;
\.


--
-- Data for Name: seriesparameter_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY seriesparameter_t (parameterid, seriesid, name) FROM stdin;
\.


--
-- Name: seriesrelationid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('seriesrelationid_seq', 1, false);


--
-- Data for Name: swedataarrayvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY swedataarrayvalue (observationid, value) FROM stdin;
\.


--
-- Data for Name: textfeatparamvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY textfeatparamvalue (parameterid, value) FROM stdin;
\.


--
-- Data for Name: textparametervalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY textparametervalue (parameterid, value) FROM stdin;
\.


--
-- Data for Name: textseriesparamvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY textseriesparamvalue (parameterid, value) FROM stdin;
\.


--
-- Data for Name: textvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY textvalue (observationid, value, identifier, name, description) FROM stdin;
\.


--
-- Data for Name: unit_t; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY unit_t (unitid, unit, name, link) FROM stdin;
\.


--
-- Name: unitid_seq; Type: SEQUENCE SET; Schema: sos_rawdb_2807; Owner: observations_p
--

SELECT pg_catalog.setval('unitid_seq', 1, false);


--
-- Data for Name: xmlfeatparamvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY xmlfeatparamvalue (parameterid, value) FROM stdin;
\.


--
-- Data for Name: xmlparametervalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY xmlparametervalue (parameterid, value) FROM stdin;
\.


--
-- Data for Name: xmlseriesparamvalue; Type: TABLE DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

COPY xmlseriesparamvalue (parameterid, value) FROM stdin;
\.


--
-- Name: blobvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY blobvalue
    ADD CONSTRAINT blobvalue_pkey PRIMARY KEY (observationid);


--
-- Name: booleanfeatparamvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY booleanfeatparamvalue
    ADD CONSTRAINT booleanfeatparamvalue_pkey PRIMARY KEY (parameterid);


--
-- Name: booleanparametervalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY booleanparametervalue
    ADD CONSTRAINT booleanparametervalue_pkey PRIMARY KEY (parameterid);


--
-- Name: booleanseriesparamvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY booleanseriesparamvalue
    ADD CONSTRAINT booleanseriesparamvalue_pkey PRIMARY KEY (parameterid);


--
-- Name: booleanvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY booleanvalue
    ADD CONSTRAINT booleanvalue_pkey PRIMARY KEY (observationid);


--
-- Name: categoryfeatparamvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY categoryfeatparamvalue
    ADD CONSTRAINT categoryfeatparamvalue_pkey PRIMARY KEY (parameterid);


--
-- Name: categoryparametervalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY categoryparametervalue_t
    ADD CONSTRAINT categoryparametervalue_pkey PRIMARY KEY (parameterid);


--
-- Name: categoryseriesparamvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY categoryseriesparamvalue_t
    ADD CONSTRAINT categoryseriesparamvalue_pkey PRIMARY KEY (parameterid);


--
-- Name: categoryvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY categoryvalue
    ADD CONSTRAINT categoryvalue_pkey PRIMARY KEY (observationid);


--
-- Name: codespace_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY codespace_t
    ADD CONSTRAINT codespace_pkey PRIMARY KEY (codespaceid);


--
-- Name: codespaceuk; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY codespace_t
    ADD CONSTRAINT codespaceuk UNIQUE (codespace);


--
-- Name: complexvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY complexvalue
    ADD CONSTRAINT complexvalue_pkey PRIMARY KEY (observationid);


--
-- Name: compositeobservation_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY compositeobservation
    ADD CONSTRAINT compositeobservation_pkey PRIMARY KEY (observationid, childobservationid);


--
-- Name: compositephenomenon_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY compositephenomenon
    ADD CONSTRAINT compositephenomenon_pkey PRIMARY KEY (childobservablepropertyid, parentobservablepropertyid);


--
-- Name: countfeatparamvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY countfeatparamvalue
    ADD CONSTRAINT countfeatparamvalue_pkey PRIMARY KEY (parameterid);


--
-- Name: countparametervalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY countparametervalue
    ADD CONSTRAINT countparametervalue_pkey PRIMARY KEY (parameterid);


--
-- Name: countseriesparamvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY countseriesparamvalue
    ADD CONSTRAINT countseriesparamvalue_pkey PRIMARY KEY (parameterid);


--
-- Name: countvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY countvalue
    ADD CONSTRAINT countvalue_pkey PRIMARY KEY (observationid);


--
-- Name: featureofinterest_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY featureofinterest_t
    ADD CONSTRAINT featureofinterest_pkey PRIMARY KEY (featureofinterestid);


--
-- Name: featureofinteresttype_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY featureofinteresttype_t
    ADD CONSTRAINT featureofinteresttype_pkey PRIMARY KEY (featureofinteresttypeid);


--
-- Name: featureparameter_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY featureparameter
    ADD CONSTRAINT featureparameter_pkey PRIMARY KEY (parameterid);


--
-- Name: featurerelation_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY featurerelation_t
    ADD CONSTRAINT featurerelation_pkey PRIMARY KEY (childfeatureid, parentfeatureid);


--
-- Name: featuretypeuk; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY featureofinteresttype_t
    ADD CONSTRAINT featuretypeuk UNIQUE (featureofinteresttype);


--
-- Name: featureurl; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY featureofinterest_t
    ADD CONSTRAINT featureurl UNIQUE (url);


--
-- Name: foiidentifieruk; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY featureofinterest_t
    ADD CONSTRAINT foiidentifieruk UNIQUE (identifier);


--
-- Name: geometryvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY geometryvalue
    ADD CONSTRAINT geometryvalue_pkey PRIMARY KEY (observationid);


--
-- Name: numericfeatparamvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY numericfeatparamvalue
    ADD CONSTRAINT numericfeatparamvalue_pkey PRIMARY KEY (parameterid);


--
-- Name: numericparametervalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY numericparametervalue
    ADD CONSTRAINT numericparametervalue_pkey PRIMARY KEY (parameterid);


--
-- Name: numericseriesparamvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY numericseriesparamvalue
    ADD CONSTRAINT numericseriesparamvalue_pkey PRIMARY KEY (parameterid);


--
-- Name: numericvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY numericvalue_t
    ADD CONSTRAINT numericvalue_pkey PRIMARY KEY (observationid);


--
-- Name: observableproperty_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observableproperty_t
    ADD CONSTRAINT observableproperty_pkey PRIMARY KEY (observablepropertyid);


--
-- Name: observation_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observation_t
    ADD CONSTRAINT observation_pkey PRIMARY KEY (observationid);


--
-- Name: observationconstellation_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observationconstellation_t
    ADD CONSTRAINT observationconstellation_pkey PRIMARY KEY (observationconstellationid);


--
-- Name: observationhasoffering_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observationhasoffering_t
    ADD CONSTRAINT observationhasoffering_pkey PRIMARY KEY (observationid, offeringid);


--
-- Name: observationtype_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observationtype_t
    ADD CONSTRAINT observationtype_pkey PRIMARY KEY (observationtypeid);


--
-- Name: observationtypeuk; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observationtype_t
    ADD CONSTRAINT observationtypeuk UNIQUE (observationtype);


--
-- Name: obsidentifieruk; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observation_t
    ADD CONSTRAINT obsidentifieruk UNIQUE (identifier);


--
-- Name: obsnconstellationidentity; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observationconstellation_t
    ADD CONSTRAINT obsnconstellationidentity UNIQUE (observablepropertyid, procedureid, offeringid);


--
-- Name: obspropidentifieruk; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observableproperty_t
    ADD CONSTRAINT obspropidentifieruk UNIQUE (identifier);


--
-- Name: offering_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY offering_t
    ADD CONSTRAINT offering_pkey PRIMARY KEY (offeringid);


--
-- Name: offeringallowedobservationtype_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY offeringallowedobservationtype_t
    ADD CONSTRAINT offeringallowedobservationtype_pkey PRIMARY KEY (offeringid, observationtypeid);


--
-- Name: offeringrelation_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY offeringrelation
    ADD CONSTRAINT offeringrelation_pkey PRIMARY KEY (childofferingid, parentofferingid);


--
-- Name: offidentifieruk; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY offering_t
    ADD CONSTRAINT offidentifieruk UNIQUE (identifier);


--
-- Name: parameter_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY parameter_t
    ADD CONSTRAINT parameter_pkey PRIMARY KEY (parameterid);


--
-- Name: procdescformatuk; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY proceduredescriptionformat_t
    ADD CONSTRAINT procdescformatuk UNIQUE (proceduredescriptionformat);


--
-- Name: procedure_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY procedure_t
    ADD CONSTRAINT procedure_pkey PRIMARY KEY (procedureid);


--
-- Name: proceduredescriptionformat_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY proceduredescriptionformat_t
    ADD CONSTRAINT proceduredescriptionformat_pkey PRIMARY KEY (proceduredescriptionformatid);


--
-- Name: procidentifieruk; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY procedure_t
    ADD CONSTRAINT procidentifieruk UNIQUE (identifier);


--
-- Name: profileobservation_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY profileobservation
    ADD CONSTRAINT profileobservation_pkey PRIMARY KEY (observationid, childobservationid);


--
-- Name: profilevalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY profilevalue
    ADD CONSTRAINT profilevalue_pkey PRIMARY KEY (observationid);


--
-- Name: relatedobservation_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY relatedobservation
    ADD CONSTRAINT relatedobservation_pkey PRIMARY KEY (relatedobservationid);


--
-- Name: relatedseries_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY relatedseries
    ADD CONSTRAINT relatedseries_pkey PRIMARY KEY (relationid);


--
-- Name: sensorsystem_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY sensorsystem
    ADD CONSTRAINT sensorsystem_pkey PRIMARY KEY (childsensorid, parentsensorid);


--
-- Name: series_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY series_t
    ADD CONSTRAINT series_pkey PRIMARY KEY (seriesid);


--
-- Name: seriesidentifieruk; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY series_t
    ADD CONSTRAINT seriesidentifieruk UNIQUE (identifier);


--
-- Name: seriesidentity; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY series_t
    ADD CONSTRAINT seriesidentity UNIQUE (featureofinterestid, observablepropertyid, procedureid);


--
-- Name: seriesmetadata_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY seriesmetadata
    ADD CONSTRAINT seriesmetadata_pkey PRIMARY KEY (metadataid);


--
-- Name: seriesparameter_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY seriesparameter_t
    ADD CONSTRAINT seriesparameter_pkey PRIMARY KEY (parameterid);


--
-- Name: swedataarrayvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY swedataarrayvalue
    ADD CONSTRAINT swedataarrayvalue_pkey PRIMARY KEY (observationid);


--
-- Name: textfeatparamvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY textfeatparamvalue
    ADD CONSTRAINT textfeatparamvalue_pkey PRIMARY KEY (parameterid);


--
-- Name: textparametervalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY textparametervalue
    ADD CONSTRAINT textparametervalue_pkey PRIMARY KEY (parameterid);


--
-- Name: textseriesparamvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY textseriesparamvalue
    ADD CONSTRAINT textseriesparamvalue_pkey PRIMARY KEY (parameterid);


--
-- Name: textvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY textvalue
    ADD CONSTRAINT textvalue_pkey PRIMARY KEY (observationid);


--
-- Name: unit_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY unit_t
    ADD CONSTRAINT unit_pkey PRIMARY KEY (unitid);


--
-- Name: unituk; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY unit_t
    ADD CONSTRAINT unituk UNIQUE (unit);


--
-- Name: xmlfeatparamvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY xmlfeatparamvalue
    ADD CONSTRAINT xmlfeatparamvalue_pkey PRIMARY KEY (parameterid);


--
-- Name: xmlparametervalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY xmlparametervalue
    ADD CONSTRAINT xmlparametervalue_pkey PRIMARY KEY (parameterid);


--
-- Name: xmlseriesparamvalue_pkey; Type: CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY xmlseriesparamvalue
    ADD CONSTRAINT xmlseriesparamvalue_pkey PRIMARY KEY (parameterid);


--
-- Name: booleanfeatparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX booleanfeatparamidx ON booleanfeatparamvalue USING btree (value);


--
-- Name: booleanparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX booleanparamidx ON booleanparametervalue USING btree (value);


--
-- Name: categoryfeatparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX categoryfeatparamidx ON categoryfeatparamvalue USING btree (value);


--
-- Name: categoryparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX categoryparamidx ON categoryparametervalue_t USING btree (value);


--
-- Name: countfeatparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX countfeatparamidx ON countfeatparamvalue USING btree (value);


--
-- Name: countparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX countparamidx ON countparametervalue USING btree (value);


--
-- Name: featparamnameidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX featparamnameidx ON featureparameter USING btree (name);


--
-- Name: featuregeomidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX featuregeomidx ON featureofinterest_t USING gist (geom);


--
-- Name: obsconstobspropidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX obsconstobspropidx ON observationconstellation_t USING btree (observablepropertyid);


--
-- Name: obsconstofferingidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX obsconstofferingidx ON observationconstellation_t USING btree (offeringid);


--
-- Name: obsconstprocedureidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX obsconstprocedureidx ON observationconstellation_t USING btree (procedureid);


--
-- Name: obshasoffobservationidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX obshasoffobservationidx ON observationhasoffering_t USING btree (observationid);


--
-- Name: obshasoffobservationidx_2807; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX obshasoffobservationidx_2807 ON observationhasoffering USING btree (observationid);


--
-- Name: obshasoffofferingidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX obshasoffofferingidx ON observationhasoffering_t USING btree (offeringid);


--
-- Name: obshasoffofferingidx_2807; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX obshasoffofferingidx_2807 ON observationhasoffering USING btree (offeringid);


--
-- Name: obsphentimeendidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX obsphentimeendidx ON observation_t USING btree (phenomenontimeend);


--
-- Name: obsphentimestartidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX obsphentimestartidx ON observation_t USING btree (phenomenontimestart);


--
-- Name: obsresulttimeidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX obsresulttimeidx ON observation_t USING btree (resulttime);


--
-- Name: obsseriesidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX obsseriesidx ON observation_t USING btree (seriesid);


--
-- Name: paramnameidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX paramnameidx ON parameter_t USING btree (name);


--
-- Name: quantityfeatparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX quantityfeatparamidx ON numericfeatparamvalue USING btree (value);


--
-- Name: quantityparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX quantityparamidx ON numericparametervalue USING btree (value);


--
-- Name: relatedobsobsidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX relatedobsobsidx ON relatedobservation USING btree (observationid);


--
-- Name: samplinggeomidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX samplinggeomidx ON observation_t USING gist (samplinggeometry);


--
-- Name: seriesbooleanparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX seriesbooleanparamidx ON booleanseriesparamvalue USING btree (value);


--
-- Name: seriescategoryparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX seriescategoryparamidx ON categoryseriesparamvalue_t USING btree (value);


--
-- Name: seriescountparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX seriescountparamidx ON countseriesparamvalue USING btree (value);


--
-- Name: seriesfeatureidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX seriesfeatureidx ON series_t USING btree (featureofinterestid);


--
-- Name: seriesobspropidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX seriesobspropidx ON series_t USING btree (observablepropertyid);


--
-- Name: seriesofferingidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX seriesofferingidx ON series_t USING btree (offeringid);


--
-- Name: seriesparamnameidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX seriesparamnameidx ON seriesparameter_t USING btree (name);


--
-- Name: seriesprocedureidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX seriesprocedureidx ON series_t USING btree (procedureid);


--
-- Name: seriesquantityparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX seriesquantityparamidx ON numericseriesparamvalue USING btree (value);


--
-- Name: seriesrelationidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX seriesrelationidx ON relatedseries USING btree (seriesid);


--
-- Name: seriestextparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX seriestextparamidx ON textseriesparamvalue USING btree (value);


--
-- Name: seriesxmlparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX seriesxmlparamidx ON xmlseriesparamvalue USING btree (value);


--
-- Name: textfeatparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX textfeatparamidx ON textfeatparamvalue USING btree (value);


--
-- Name: textparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX textparamidx ON textparametervalue USING btree (value);


--
-- Name: xmlfeatparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX xmlfeatparamidx ON xmlfeatparamvalue USING btree (value);


--
-- Name: xmlparamidx; Type: INDEX; Schema: sos_rawdb_2807; Owner: observations_p
--

CREATE INDEX xmlparamidx ON xmlparametervalue USING btree (value);


--
-- Name: catfeatparamvalueunitfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY categoryfeatparamvalue
    ADD CONSTRAINT catfeatparamvalueunitfk FOREIGN KEY (unitid) REFERENCES unit_t(unitid);


--
-- Name: catparamvalueunitfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY categoryparametervalue_t
    ADD CONSTRAINT catparamvalueunitfk FOREIGN KEY (unitid) REFERENCES unit_t(unitid);


--
-- Name: featparambooleanvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY booleanfeatparamvalue
    ADD CONSTRAINT featparambooleanvaluefk FOREIGN KEY (parameterid) REFERENCES featureparameter(parameterid);


--
-- Name: featparamcategoryvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY categoryfeatparamvalue
    ADD CONSTRAINT featparamcategoryvaluefk FOREIGN KEY (parameterid) REFERENCES featureparameter(parameterid);


--
-- Name: featparamcountvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY countfeatparamvalue
    ADD CONSTRAINT featparamcountvaluefk FOREIGN KEY (parameterid) REFERENCES featureparameter(parameterid);


--
-- Name: featparamnumericvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY numericfeatparamvalue
    ADD CONSTRAINT featparamnumericvaluefk FOREIGN KEY (parameterid) REFERENCES featureparameter(parameterid);


--
-- Name: featparamtextvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY textfeatparamvalue
    ADD CONSTRAINT featparamtextvaluefk FOREIGN KEY (parameterid) REFERENCES featureparameter(parameterid);


--
-- Name: featparamxmlvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY xmlfeatparamvalue
    ADD CONSTRAINT featparamxmlvaluefk FOREIGN KEY (parameterid) REFERENCES featureparameter(parameterid);


--
-- Name: featurecodespaceidentifierfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY featureofinterest_t
    ADD CONSTRAINT featurecodespaceidentifierfk FOREIGN KEY (codespace) REFERENCES codespace_t(codespaceid);


--
-- Name: featurecodespacenamefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY featureofinterest_t
    ADD CONSTRAINT featurecodespacenamefk FOREIGN KEY (codespacename) REFERENCES codespace_t(codespaceid);


--
-- Name: featurefeaturetypefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY featureofinterest_t
    ADD CONSTRAINT featurefeaturetypefk FOREIGN KEY (featureofinteresttypeid) REFERENCES featureofinteresttype_t(featureofinteresttypeid);


--
-- Name: featureofinterestchildfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY featurerelation_t
    ADD CONSTRAINT featureofinterestchildfk FOREIGN KEY (childfeatureid) REFERENCES featureofinterest_t(featureofinterestid);


--
-- Name: featureofinterestparentfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY featurerelation_t
    ADD CONSTRAINT featureofinterestparentfk FOREIGN KEY (parentfeatureid) REFERENCES featureofinterest_t(featureofinterestid);


--
-- Name: fk_3v5iovcndi9w0hgh827hcvivw; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY parameter_t
    ADD CONSTRAINT fk_3v5iovcndi9w0hgh827hcvivw FOREIGN KEY (observationid) REFERENCES observation_t(observationid);


--
-- Name: fk_9ex7hawh3dbplkllmw5w3kvej; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observationhasoffering_t
    ADD CONSTRAINT fk_9ex7hawh3dbplkllmw5w3kvej FOREIGN KEY (observationid) REFERENCES observation_t(observationid);


--
-- Name: fk_demhrjbxu9pernp88kgdot1yv; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY relatedobservation
    ADD CONSTRAINT fk_demhrjbxu9pernp88kgdot1yv FOREIGN KEY (observationid) REFERENCES observation_t(observationid);


--
-- Name: fk_ikwfee6lsskhogu07yuj2gyo3; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY featureparameter
    ADD CONSTRAINT fk_ikwfee6lsskhogu07yuj2gyo3 FOREIGN KEY (featureofinterestid) REFERENCES featureofinterest_t(featureofinterestid);


--
-- Name: fk_lkljeohulvu7cr26pduyp5bd0; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY offeringallowedobservationtype_t
    ADD CONSTRAINT fk_lkljeohulvu7cr26pduyp5bd0 FOREIGN KEY (offeringid) REFERENCES offering_t(offeringid);


--
-- Name: fk_o9xdeieoy6b6gtbyuycgtqpvd; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY relatedobservation
    ADD CONSTRAINT fk_o9xdeieoy6b6gtbyuycgtqpvd FOREIGN KEY (relatedobservation) REFERENCES observation_t(observationid);


--
-- Name: obscodespaceidentifierfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observation_t
    ADD CONSTRAINT obscodespaceidentifierfk FOREIGN KEY (codespace) REFERENCES codespace_t(codespaceid);


--
-- Name: obscodespacenamefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observation_t
    ADD CONSTRAINT obscodespacenamefk FOREIGN KEY (codespacename) REFERENCES codespace_t(codespaceid);


--
-- Name: obsconstobservationiypefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observationconstellation_t
    ADD CONSTRAINT obsconstobservationiypefk FOREIGN KEY (observationtypeid) REFERENCES observationtype_t(observationtypeid);


--
-- Name: obsconstobspropfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observationconstellation_t
    ADD CONSTRAINT obsconstobspropfk FOREIGN KEY (observablepropertyid) REFERENCES observableproperty_t(observablepropertyid);


--
-- Name: obsconstofferingfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observationconstellation_t
    ADD CONSTRAINT obsconstofferingfk FOREIGN KEY (offeringid) REFERENCES offering_t(offeringid);


--
-- Name: observablepropertychildfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY compositephenomenon
    ADD CONSTRAINT observablepropertychildfk FOREIGN KEY (childobservablepropertyid) REFERENCES observableproperty_t(observablepropertyid);


--
-- Name: observablepropertyparentfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY compositephenomenon
    ADD CONSTRAINT observablepropertyparentfk FOREIGN KEY (parentobservablepropertyid) REFERENCES observableproperty_t(observablepropertyid);


--
-- Name: observationblobvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY blobvalue
    ADD CONSTRAINT observationblobvaluefk FOREIGN KEY (observationid) REFERENCES observation_t(observationid);


--
-- Name: observationbooleanvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY booleanvalue
    ADD CONSTRAINT observationbooleanvaluefk FOREIGN KEY (observationid) REFERENCES observation_t(observationid);


--
-- Name: observationcategoryvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY categoryvalue
    ADD CONSTRAINT observationcategoryvaluefk FOREIGN KEY (observationid) REFERENCES observation_t(observationid);


--
-- Name: observationchildfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY compositeobservation
    ADD CONSTRAINT observationchildfk FOREIGN KEY (childobservationid) REFERENCES observation_t(observationid);


--
-- Name: observationcomplexvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY complexvalue
    ADD CONSTRAINT observationcomplexvaluefk FOREIGN KEY (observationid) REFERENCES observation_t(observationid);


--
-- Name: observationcountvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY countvalue
    ADD CONSTRAINT observationcountvaluefk FOREIGN KEY (observationid) REFERENCES observation_t(observationid);


--
-- Name: observationgeometryvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY geometryvalue
    ADD CONSTRAINT observationgeometryvaluefk FOREIGN KEY (observationid) REFERENCES observation_t(observationid);


--
-- Name: observationnumericvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY numericvalue_t
    ADD CONSTRAINT observationnumericvaluefk FOREIGN KEY (observationid) REFERENCES observation_t(observationid);


--
-- Name: observationofferingfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observationhasoffering_t
    ADD CONSTRAINT observationofferingfk FOREIGN KEY (offeringid) REFERENCES offering_t(offeringid);


--
-- Name: observationparentfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY compositeobservation
    ADD CONSTRAINT observationparentfk FOREIGN KEY (observationid) REFERENCES complexvalue(observationid);


--
-- Name: observationprofilevaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY profilevalue
    ADD CONSTRAINT observationprofilevaluefk FOREIGN KEY (observationid) REFERENCES observation_t(observationid);


--
-- Name: observationseriesfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observation_t
    ADD CONSTRAINT observationseriesfk FOREIGN KEY (seriesid) REFERENCES series_t(seriesid);


--
-- Name: observationswedataarrayvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY swedataarrayvalue
    ADD CONSTRAINT observationswedataarrayvaluefk FOREIGN KEY (observationid) REFERENCES observation_t(observationid);


--
-- Name: observationtextvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY textvalue
    ADD CONSTRAINT observationtextvaluefk FOREIGN KEY (observationid) REFERENCES observation_t(observationid);


--
-- Name: observationunitfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observation_t
    ADD CONSTRAINT observationunitfk FOREIGN KEY (unitid) REFERENCES unit_t(unitid);


--
-- Name: obsnconstprocedurefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observationconstellation_t
    ADD CONSTRAINT obsnconstprocedurefk FOREIGN KEY (procedureid) REFERENCES procedure_t(procedureid);


--
-- Name: obspropcodespaceidentifierfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observableproperty_t
    ADD CONSTRAINT obspropcodespaceidentifierfk FOREIGN KEY (codespace) REFERENCES codespace_t(codespaceid);


--
-- Name: obspropcodespacenamefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY observableproperty_t
    ADD CONSTRAINT obspropcodespacenamefk FOREIGN KEY (codespacename) REFERENCES codespace_t(codespaceid);


--
-- Name: offcodespaceidentifierfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY offering_t
    ADD CONSTRAINT offcodespaceidentifierfk FOREIGN KEY (codespace) REFERENCES codespace_t(codespaceid);


--
-- Name: offcodespacenamefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY offering_t
    ADD CONSTRAINT offcodespacenamefk FOREIGN KEY (codespacename) REFERENCES codespace_t(codespaceid);


--
-- Name: offeringchildfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY offeringrelation
    ADD CONSTRAINT offeringchildfk FOREIGN KEY (childofferingid) REFERENCES offering_t(offeringid);


--
-- Name: offeringobservationtypefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY offeringallowedobservationtype_t
    ADD CONSTRAINT offeringobservationtypefk FOREIGN KEY (observationtypeid) REFERENCES observationtype_t(observationtypeid);


--
-- Name: offeringparenffk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY offeringrelation
    ADD CONSTRAINT offeringparenffk FOREIGN KEY (parentofferingid) REFERENCES offering_t(offeringid);


--
-- Name: parameterbooleanvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY booleanparametervalue
    ADD CONSTRAINT parameterbooleanvaluefk FOREIGN KEY (parameterid) REFERENCES parameter_t(parameterid);


--
-- Name: parametercategoryvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY categoryparametervalue_t
    ADD CONSTRAINT parametercategoryvaluefk FOREIGN KEY (parameterid) REFERENCES parameter_t(parameterid);


--
-- Name: parametercountvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY countparametervalue
    ADD CONSTRAINT parametercountvaluefk FOREIGN KEY (parameterid) REFERENCES parameter_t(parameterid);


--
-- Name: parameternumericvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY numericparametervalue
    ADD CONSTRAINT parameternumericvaluefk FOREIGN KEY (parameterid) REFERENCES parameter_t(parameterid);


--
-- Name: parametertextvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY textparametervalue
    ADD CONSTRAINT parametertextvaluefk FOREIGN KEY (parameterid) REFERENCES parameter_t(parameterid);


--
-- Name: parameterxmlvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY xmlparametervalue
    ADD CONSTRAINT parameterxmlvaluefk FOREIGN KEY (parameterid) REFERENCES parameter_t(parameterid);


--
-- Name: proccodespaceidentifierfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY procedure_t
    ADD CONSTRAINT proccodespaceidentifierfk FOREIGN KEY (codespace) REFERENCES codespace_t(codespaceid);


--
-- Name: proccodespacenamefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY procedure_t
    ADD CONSTRAINT proccodespacenamefk FOREIGN KEY (codespacename) REFERENCES codespace_t(codespaceid);


--
-- Name: procedurechildfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY sensorsystem
    ADD CONSTRAINT procedurechildfk FOREIGN KEY (childsensorid) REFERENCES procedure_t(procedureid);


--
-- Name: procedureparenffk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY sensorsystem
    ADD CONSTRAINT procedureparenffk FOREIGN KEY (parentsensorid) REFERENCES procedure_t(procedureid);


--
-- Name: procprocdescformatfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY procedure_t
    ADD CONSTRAINT procprocdescformatfk FOREIGN KEY (proceduredescriptionformatid) REFERENCES proceduredescriptionformat_t(proceduredescriptionformatid);


--
-- Name: profileobschildfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY profileobservation
    ADD CONSTRAINT profileobschildfk FOREIGN KEY (childobservationid) REFERENCES observation_t(observationid);


--
-- Name: profileobsparentfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY profileobservation
    ADD CONSTRAINT profileobsparentfk FOREIGN KEY (observationid) REFERENCES profilevalue(observationid);


--
-- Name: profileunitfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY profilevalue
    ADD CONSTRAINT profileunitfk FOREIGN KEY (levelunitid) REFERENCES unit_t(unitid);


--
-- Name: quanfeatparamvalueunitfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY numericfeatparamvalue
    ADD CONSTRAINT quanfeatparamvalueunitfk FOREIGN KEY (unitid) REFERENCES unit_t(unitid);


--
-- Name: quanparamvalueunitfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY numericparametervalue
    ADD CONSTRAINT quanparamvalueunitfk FOREIGN KEY (unitid) REFERENCES unit_t(unitid);


--
-- Name: relatedseriesfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY relatedseries
    ADD CONSTRAINT relatedseriesfk FOREIGN KEY (relatedseries) REFERENCES series_t(seriesid);


--
-- Name: seriescatparamvalueunitfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY categoryseriesparamvalue_t
    ADD CONSTRAINT seriescatparamvalueunitfk FOREIGN KEY (unitid) REFERENCES unit_t(unitid);


--
-- Name: seriescodespaceidentifierfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY series_t
    ADD CONSTRAINT seriescodespaceidentifierfk FOREIGN KEY (codespace) REFERENCES codespace_t(codespaceid);


--
-- Name: seriescodespacenamefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY series_t
    ADD CONSTRAINT seriescodespacenamefk FOREIGN KEY (codespacename) REFERENCES codespace_t(codespaceid);


--
-- Name: seriesfeaturefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY series_t
    ADD CONSTRAINT seriesfeaturefk FOREIGN KEY (featureofinterestid) REFERENCES featureofinterest_t(featureofinterestid);


--
-- Name: seriesobpropfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY series_t
    ADD CONSTRAINT seriesobpropfk FOREIGN KEY (observablepropertyid) REFERENCES observableproperty_t(observablepropertyid);


--
-- Name: seriesofferingfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY series_t
    ADD CONSTRAINT seriesofferingfk FOREIGN KEY (offeringid) REFERENCES offering_t(offeringid);


--
-- Name: seriesparambooleanvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY booleanseriesparamvalue
    ADD CONSTRAINT seriesparambooleanvaluefk FOREIGN KEY (parameterid) REFERENCES seriesparameter_t(parameterid);


--
-- Name: seriesparamcategoryvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY categoryseriesparamvalue_t
    ADD CONSTRAINT seriesparamcategoryvaluefk FOREIGN KEY (parameterid) REFERENCES seriesparameter_t(parameterid);


--
-- Name: seriesparamcountvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY countseriesparamvalue
    ADD CONSTRAINT seriesparamcountvaluefk FOREIGN KEY (parameterid) REFERENCES seriesparameter_t(parameterid);


--
-- Name: seriesparamnumericvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY numericseriesparamvalue
    ADD CONSTRAINT seriesparamnumericvaluefk FOREIGN KEY (parameterid) REFERENCES seriesparameter_t(parameterid);


--
-- Name: seriesparamtextvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY textseriesparamvalue
    ADD CONSTRAINT seriesparamtextvaluefk FOREIGN KEY (parameterid) REFERENCES seriesparameter_t(parameterid);


--
-- Name: seriesparamxmlvaluefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY xmlseriesparamvalue
    ADD CONSTRAINT seriesparamxmlvaluefk FOREIGN KEY (parameterid) REFERENCES seriesparameter_t(parameterid);


--
-- Name: seriesprocedurefk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY series_t
    ADD CONSTRAINT seriesprocedurefk FOREIGN KEY (procedureid) REFERENCES procedure_t(procedureid);


--
-- Name: seriesquanparamvalueunitfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY numericseriesparamvalue
    ADD CONSTRAINT seriesquanparamvalueunitfk FOREIGN KEY (unitid) REFERENCES unit_t(unitid);


--
-- Name: seriesunitfk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY series_t
    ADD CONSTRAINT seriesunitfk FOREIGN KEY (unitid) REFERENCES unit_t(unitid);


--
-- Name: typeoffk; Type: FK CONSTRAINT; Schema: sos_rawdb_2807; Owner: observations_p
--

ALTER TABLE ONLY procedure_t
    ADD CONSTRAINT typeoffk FOREIGN KEY (typeof) REFERENCES procedure_t(procedureid);


--
-- Name: categoryparametervalue; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW categoryparametervalue;


--
-- Name: featureofinterest; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW featureofinterest;


--
-- Name: series; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW series;


--
-- Name: categoryseriesparamvalue; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW categoryseriesparamvalue;


--
-- Name: codespace; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW codespace;


--
-- Name: featureofinteresttype; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW featureofinteresttype;


--
-- Name: featurerelation; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW featurerelation;


--
-- Name: numericvalue; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW numericvalue;


--
-- Name: observableproperty; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW observableproperty;


--
-- Name: observation; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW observation;


--
-- Name: procedure; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW procedure;


--
-- Name: observationconstellation; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW observationconstellation;


--
-- Name: observationhasoffering; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW observationhasoffering;


--
-- Name: observationtype; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW observationtype;


--
-- Name: offering; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW offering;


--
-- Name: offeringallowedobservationtype; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW offeringallowedobservationtype;


--
-- Name: parameter; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW parameter;


--
-- Name: proceduredescriptionformat; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW proceduredescriptionformat;


--
-- Name: seriesparameter; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW seriesparameter;


--
-- Name: unit; Type: MATERIALIZED VIEW DATA; Schema: sos_rawdb_2807; Owner: observations_p
--

REFRESH MATERIALIZED VIEW unit;


--
-- PostgreSQL database dump complete
--

