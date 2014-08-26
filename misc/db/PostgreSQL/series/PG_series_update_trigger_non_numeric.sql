-- trigger functionality

CREATE OR REPLACE FUNCTION public.non_numeric_ins_upd_to_series()
	RETURNS trigger AS
$code$
DECLARE
	c1 CURSOR FOR
		SELECT 
			firsttimestamp, 
			firstobservationid, 
			lasttimestamp, 
			lastobservationid,
			unitid
		FROM public.series
		WHERE seriesid = (Select seriesid FROM observation WHERE observationid = NEW.observationid);
	first_ts TIMESTAMP;
	first_obs BIGINT;
	last_ts TIMESTAMP;
	last_obs BIGINT;
	start_ts TIMESTAMP;
	end_ts TIMESTAMP;
	obs_unitid BIGINT;
	del CHARACTER(1);
BEGIN 
	Select o.phenomenontimestart, o.phenomenontimeend, unitid, o.deleted INTO start_ts, end_ts, obs_unitid, del FROM public.observation o WHERE o.observationid = NEW.observationid;
	IF del = 'F' THEN
		OPEN c1;
		FETCH c1 INTO first_ts, first_obs, last_ts, last_obs;
		CASE
			WHEN first_ts IS NULL THEN
				UPDATE public.series SET
					firsttimestamp = start_ts,
					firstobservationid = NEW.observationid, 
					lasttimestamp = end_ts, 
					lastobservationid = NEW.observationid,
					unitid = obs_unitid
				WHERE CURRENT OF c1;
			WHEN end_ts > last_ts THEN 
				UPDATE public.series SET
					lasttimestamp = end_ts, 
					lastobservationid = NEW.observationid
				WHERE CURRENT OF c1;
			WHEN end_ts = last_ts AND NEW.observationid <> last_obs THEN 
				UPDATE public.series SET
					lastobservationid = NEW.observationid
				WHERE CURRENT OF c1;
			WHEN start_ts < first_ts THEN
				UPDATE public.series SET
					firsttimestamp = start_ts, 
					firstobservationid = NEW.observationid
				WHERE CURRENT OF c1;
			WHEN start_ts = first_ts AND NEW.observationid <> first_obs THEN
				UPDATE public.series SET
					firstobservationid = NEW.observationid
				WHERE CURRENT OF c1;
			ELSE NULL;
		END CASE;
		CLOSE c1;
	END IF;	 
	RETURN NEW;
END
$code$
LANGUAGE plpgsql 
SECURITY DEFINER;

-- drop triggers
DROP TRIGGER IF EXISTS boolean_ins_upd_to_series ON public.booleanvalue;
DROP TRIGGER IF EXISTS category_ins_upd_to_series ON public.categoryvalue;
DROP TRIGGER IF EXISTS count_ins_upd_to_series ON public.countvalue;
DROP TRIGGER IF EXISTS geometry_ins_upd_to_series ON public.geometryvalue;
DROP TRIGGER IF EXISTS swedataarray_ins_upd_to_series ON public.swedataarrayvalue;
DROP TRIGGER IF EXISTS text_ins_upd_to_series ON public.textvalue;

-- create triggers on ...value tables
CREATE TRIGGER boolean_ins_upd_to_series
	AFTER INSERT OR UPDATE ON public.booleanvalue
	FOR EACH ROW EXECUTE PROCEDURE public.non_numeric_ins_upd_to_series();
	
CREATE TRIGGER category_ins_upd_to_series
	AFTER INSERT OR UPDATE ON public.categoryvalue
	FOR EACH ROW EXECUTE PROCEDURE public.non_numeric_ins_upd_to_series();
	
CREATE TRIGGER count_ins_upd_to_series
	AFTER INSERT OR UPDATE ON public.countvalue
	FOR EACH ROW EXECUTE PROCEDURE public.non_numeric_ins_upd_to_series();

CREATE TRIGGER geometry_ins_upd_to_series
	AFTER INSERT OR UPDATE ON public.geometryvalue
	FOR EACH ROW EXECUTE PROCEDURE public.non_numeric_ins_upd_to_series();
	
CREATE TRIGGER swedataarray_ins_upd_to_series
	AFTER INSERT OR UPDATE ON public.swedataarrayvalue
	FOR EACH ROW EXECUTE PROCEDURE public.non_numeric_ins_upd_to_series();

CREATE TRIGGER text_ins_upd_to_series
	AFTER INSERT OR UPDATE ON public.textvalue
	FOR EACH ROW EXECUTE PROCEDURE public.non_numeric_ins_upd_to_series();