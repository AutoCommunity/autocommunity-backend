CREATE TABLE session
(
    id              UUID NOT NULL,
    user_id         VARCHAR(255),
    creation_time   TIMESTAMP WITHOUT TIME ZONE,
    status          VARCHAR(255),
    expiration_time TIMESTAMP WITHOUT TIME ZONE,
    session         VARCHAR(255),
    CONSTRAINT pk_session PRIMARY KEY (id)
);