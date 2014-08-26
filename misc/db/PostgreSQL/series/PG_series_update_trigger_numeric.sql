-- trigger functionality

CREATE OR REPLACE FUNCTION public.numeric_ins_upd_to_series()
	RETURNS trigger AS
$code$
DECLARE
	c1 CURSOR FOR
		SELECT 
			firsttimestamp, 
			firstnumericvalue,
			firstobservationid,
			lasttimestamp, 
			lastnumericvalue,
			lastobservationid
			unitid
		FROM public.series
		WHERE seriesid = (Select seriesid  FROM observation WHERE observationid = NEW.observationid);
	first_ts TIMESTAMP;
	first_val NUMERIC;
	first_obs BIGINT;
	last_ts TIMESTAMP;
	last_val NUMERIC;
	last_obs BIGINT;
	start_ts TIMESTAMP;
	end_ts TIMESTAMP;
	obs_unitid BIGINT;
	del CHARACTER(1);
BEGIN 
	Select o.phenomenontimestart, o.phenomenontimeend, unitid, o.deleted INTO start_ts, end_ts, obs_unitid, del FROM public.observation o WHERE o.observationid = NEW.observationid;
	IF del = 'F' THEN
		OPEN c1;
		FETCH c1 INTO first_ts, first_val, first_obs, last_ts, last_val, last_obs;
		CASE
			WHEN first_ts IS NULL THEN
				UPDATE public.series SET
					firsttimestamp = start_ts,
					firstnumericvalue = NEW.value, 
					firstobservationid = NEW.observationid,
					lasttimestamp = end_ts, 
					lastnumericvalue = NEW.value,
					lastobservationid = NEW.observationid,
					unitid = obs_unitid
				WHERE CURRENT OF c1;
			WHEN end_ts > last_ts THEN 
				UPDATE public.series SET
					lasttimestamp = end_ts, 
					lastnumericvalue = NEW.value,
					lastobservationid = NEW.observationid
				WHERE CURRENT OF c1;
			WHEN end_ts = last_ts AND NEW.value <> last_val THEN 
				UPDATE public.series SET
					lastnumericvalue = NEW.value,
					lastobservationid = NEW.observationid
				WHERE CURRENT OF c1;
			WHEN start_ts < first_ts THEN
				UPDATE public.series SET
					firsttimestamp = start_ts, 
					firstnumericvalue = NEW.value,
					firstobservationid = NEW.observationid
				WHERE CURRENT OF c1;
			WHEN start_ts = first_ts AND NEW.value <> first_val THEN
				UPDATE public.series SET
					firstnumericvalue = NEW.value,
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

-- drop trigger
DROP TRIGGER IF EXISTS numeric_ins_upd_to_series ON public.numericvalue;

-- create trigger on numericvalue table
CREATE TRIGGER numeric_ins_upd_to_series
	AFTER INSERT OR UPDATE ON public.numericvalue
	FOR EACH ROW EXECUTE PROCEDURE public.numeric_ins_upd_to_series();