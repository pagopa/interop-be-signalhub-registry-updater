CREATE TABLE IF NOT EXISTS ESERVICE (
    eservice_id     VARCHAR (50) UNIQUE NOT NULL,
    producer_id     VARCHAR (50) NOT NULL,
    event_id        BIGINT       NOT NULL,
    state           VARCHAR (50) NOT NULL,
    tmst_insert     TIMESTAMP    NOT NULL,
    tmst_last_edit  TIMESTAMP,
    UNIQUE (eservice_id, producer_id),
    PRIMARY KEY (eservice_id, producer_id)
);
/*
CREATE INDEX IF NOT EXISTS ESERVICE_INDEX_ID ON ESERVICE (eservice_id);
CREATE INDEX IF NOT EXISTS ESERVICE_INDEX_PRODUCER_ID ON ESERVICE (producer_id);
*/


CREATE TABLE IF NOT EXISTS CONSUMER_ESERVICE (
    eservice_id     VARCHAR (50) NOT NULL,
    consumer_id     VARCHAR (50) NOT NULL,
    event_id        BIGINT       NOT NULL,
    state           VARCHAR (50) NOT NULL,
    tmst_insert     TIMESTAMP    NOT NULL,
    tmst_last_edit  TIMESTAMP,
    UNIQUE (eservice_id, consumer_id),
    PRIMARY KEY (eservice_id, consumer_id)
);
/*
CREATE INDEX IF NOT EXISTS CONSUMER_ESERVICE_INDEX_ID ON CONSUMER_ESERVICE (eservice_id);
CREATE INDEX IF NOT EXISTS ESERVICE_INDEX_CONSUMER_ID ON CONSUMER_ESERVICE (consumer_id);
*/


CREATE TABLE IF NOT EXISTS SIGNAL (
    id SERIAL PRIMARY KEY,
    correlation_id VARCHAR(50) NOT NULL,
    signal_id      BIGINT        NOT NULL,
    object_id      VARCHAR (50)  NOT NULL,
    eservice_id    VARCHAR (50)  NOT NULL,
    object_type    VARCHAR (50)  NOT NULL,
    signal_type    VARCHAR (50)  NOT NULL,
    tmst_insert    TIMESTAMP     NOT NULL,
    UNIQUE (signal_id, eservice_id)
);
/* H2 not support USING constructor
CREATE INDEX IF NOT EXISTS SIGNAL_INDEX_SIGNAL_ID ON SIGNAL USING hash (signal_id);
CREATE INDEX IF NOT EXISTS SIGNAL_INDEX_ESERVICE_ID ON SIGNAL USING hash (eservice_id);
*/


CREATE TABLE IF NOT EXISTS DEAD_SIGNAL (
   id          SERIAL PRIMARY KEY,
   correlation_id VARCHAR(50)   NOT NULL,
   signal_id      BIGINT        NOT NULL,
   object_id      VARCHAR (50)  NOT NULL,
   eservice_id    VARCHAR (50)  NOT NULL,
   object_type    VARCHAR (50)  NOT NULL,
   signal_type    VARCHAR (50)  NOT NULL,
   tmst_insert    TIMESTAMP     NOT NULL,
   error_reason   VARCHAR(255)  NOT NULL
);


CREATE TABLE IF NOT EXISTS TRACING_BATCH (
    batch_id         SERIAL PRIMARY KEY,
    state            VARCHAR (50) NOT NULL,
    last_event_id    BIGINT,
    tmst_started     TIMESTAMP NOT NULL,
    tmst_ended       TIMESTAMP
);


CREATE TABLE IF NOT EXISTS DEAD_EVENT (
    event_tmp_id        SERIAL PRIMARY KEY,
    tmst_insert         TIMESTAMP,
    error_reason        VARCHAR(255) NOT NULL,
    event_id            BIGINT NOT NULL,
    event_type          VARCHAR (50) NOT NULL,
    object_type         VARCHAR (50) NOT NULL,
    eservice_id         VARCHAR (50),
    agreement_id        VARCHAR (50)
);