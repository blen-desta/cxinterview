DROP SCHEMA IF EXISTS INTERVIEW CASCADE;
CREATE SCHEMA INTERVIEW;

CREATE TABLE INTERVIEW.employee (
  id       SERIAL UNIQUE PRIMARY KEY,
  username VARCHAR(128) NOT NULL UNIQUE
);

CREATE TABLE INTERVIEW.category (
  id   SERIAL UNIQUE PRIMARY KEY,
  name VARCHAR(128) NOT NULL UNIQUE
);

INSERT INTO INTERVIEW.category (name) VALUES ('DEVOPS');
INSERT INTO INTERVIEW.category (name) VALUES ('FRONTEND');
INSERT INTO INTERVIEW.category (name) VALUES ('BACKEND');

CREATE TABLE INTERVIEW.level (
  id   SERIAL UNIQUE PRIMARY KEY,
  name VARCHAR(128) NOT NULL UNIQUE
);

INSERT INTO INTERVIEW.level (name) VALUES ('NEW_GRAD');
INSERT INTO INTERVIEW.level (name) VALUES ('JUNIOR');
INSERT INTO INTERVIEW.level (name) VALUES ('SENIOR');

CREATE TABLE INTERVIEW.question (
  id     SERIAL UNIQUE PRIMARY KEY,
  title  VARCHAR(128) NOT NULL,
  qtext  TEXT NOT NULL,
  qnotes TEXT
);

CREATE TABLE INTERVIEW.question_level (
  qid   INTEGER NOT NULL REFERENCES INTERVIEW.question (id),
  level INTEGER NOT NULL REFERENCES INTERVIEW.level (id),

  PRIMARY KEY (qid, level)
);

CREATE TABLE INTERVIEW.question_category (
  qid      INTEGER NOT NULL REFERENCES INTERVIEW.question (id),
  category INTEGER NOT NULL REFERENCES INTERVIEW.category (id) ON DELETE CASCADE,

  PRIMARY KEY (qid, category)
);

CREATE TABLE INTERVIEW.refertype (
  id   SERIAL UNIQUE PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO INTERVIEW.refertype (name) VALUES ('EMPLOYEE');
INSERT INTO INTERVIEW.refertype (name) VALUES ('RECRUITER');
INSERT INTO INTERVIEW.refertype (name) VALUES ('ASU_CAREER');
INSERT INTO INTERVIEW.refertype (name) VALUES ('UA_CAREER');

CREATE TABLE INTERVIEW.candidate (
  id         SERIAL UNIQUE PRIMARY KEY,
  email      VARCHAR(128) NOT NULL UNIQUE,
  phone      VARCHAR(64)  NOT NULL UNIQUE,
  name       VARCHAR(255) NOT NULL,
  experience INT          NOT NULL,
  resume     BYTEA
);

CREATE TABLE INTERVIEW.cand_notes (
  cand_id INTEGER NOT NULL REFERENCES INTERVIEW.candidate (id),
  emp_id  INTEGER NOT NULL REFERENCES INTERVIEW.employee (id),
  date    TIMESTAMP,
  notes   TEXT,

  PRIMARY KEY (cand_id, emp_id, date)
);

CREATE TABLE INTERVIEW.cand_school (
  cand_id   INTEGER      NOT NULL REFERENCES INTERVIEW.candidate (id),
  school    VARCHAR(255) NOT NULL,
  grad_date DATE,

  PRIMARY KEY (cand_id, school, grad_date)
);

CREATE TABLE INTERVIEW.cand_job (
  cand_id    INTEGER      NOT NULL REFERENCES INTERVIEW.candidate (id),
  employer   VARCHAR(255) NOT NULL,
  start_date DATE         NOT NULL,
  end_date   DATE         NOT NULL,

  PRIMARY KEY (cand_id, employer, start_date)
);

CREATE TABLE INTERVIEW.cand_refertype (
  cand_id  INTEGER NOT NULL REFERENCES INTERVIEW.candidate (id),
  refer_id INTEGER NOT NULL REFERENCES INTERVIEW.refertype (id),

  PRIMARY KEY (cand_id, refer_id)
);

--
-- It is possible for multiple employees to refer the same candidate
--
CREATE TABLE INTERVIEW.emp_referral (
  id      SERIAL UNIQUE PRIMARY KEY,
  emp_id  INTEGER NOT NULL REFERENCES INTERVIEW.employee (id),
  cand_id INTEGER NOT NULL REFERENCES INTERVIEW.candidate (id)
);

CREATE TABLE INTERVIEW.sessiontype (
  id   SERIAL UNIQUE PRIMARY KEY,
  name VARCHAR(128) NOT NULL UNIQUE
);

INSERT INTO INTERVIEW.sessiontype (name) VALUES ('PHONE');
INSERT INTO INTERVIEW.sessiontype (name) VALUES ('VIDEO');
INSERT INTO INTERVIEW.sessiontype (name) VALUES ('ONSITE');

CREATE TABLE INTERVIEW.session (
  id             SERIAL UNIQUE PRIMARY KEY,
  cand_id        INTEGER NOT NULL REFERENCES INTERVIEW.candidate (id),
  type           INTEGER NOT NULL REFERENCES INTERVIEW.sessiontype (id),
  date           TIMESTAMP,
  recommendation TEXT
);

CREATE TABLE INTERVIEW.session_question_notes (
  session_id INTEGER NOT NULL REFERENCES INTERVIEW.session (id),
  emp_id     INTEGER NOT NULL REFERENCES INTERVIEW.employee (id),
  qid        INTEGER NOT NULL REFERENCES INTERVIEW.question (id),
  notes      TEXT,

  PRIMARY KEY (session_id, emp_id, qid)
);

CREATE TABLE INTERVIEW.session_notes (
  session_id INTEGER NOT NULL REFERENCES INTERVIEW.session (id),
  emp_id     INTEGER NOT NULL REFERENCES INTERVIEW.employee (id),
  notes      TEXT,

  PRIMARY KEY (session_id, emp_id)
);

GRANT USAGE ON SCHEMA interview TO interview;
GRANT SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA interview TO interview;
GRANT ALL ON ALL TABLES IN SCHEMA interview TO interview;