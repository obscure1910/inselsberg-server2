CREATE TABLE IBERG_IMAGE (
  id           BIGSERIAL,
  cam_type     INTEGER,
  size         BIGINT,
  width        INTEGER,
  height       INTEGER,
  image        BYTEA,
  statistic_id BIGINT,

  PRIMARY KEY (id),
  CONSTRAINT FK_Statistic FOREIGN KEY (statistic_id) REFERENCES IBERG_STATISTIC (id) ON DELETE CASCADE
);