CREATE TABLE IBERG_STATISTIC (
  id                 BIGSERIAL,
  timestamp          TIMESTAMP WITH TIME ZONE,
  temperature        NUMERIC(4,1),
  humidity           NUMERIC(4,1),
  air_pressure       NUMERIC(5,1),
  air_pressure_trend TEXT,
  wind_velocity      NUMERIC(4,1),
  wind_gust          NUMERIC(4,1),
  rain               NUMERIC(4,1),
  rain_last_hours    NUMERIC(4,1),
  is_lift_open       BOOLEAN,

  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_timestamp ON IBERG_STATISTIC (timestamp DESC);