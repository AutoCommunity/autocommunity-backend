CREATE TABLE user_t
(
    id            UUID NOT NULL,
    username      VARCHAR(255),
    password_hash VARCHAR(255),
    created       TIMESTAMP WITHOUT TIME ZONE,
    changed       TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_user_t PRIMARY KEY (id)
);

ALTER TABLE user_t
    ADD CONSTRAINT uc_user_t_username UNIQUE (username);

CREATE TABLE session
(
    id              UUID NOT NULL,
    user_id         UUID,
    creation_time   TIMESTAMP WITHOUT TIME ZONE,
    status          VARCHAR(255),
    expiration_time TIMESTAMP WITHOUT TIME ZONE,
    session         VARCHAR(255),
    CONSTRAINT pk_session PRIMARY KEY (id)
);

ALTER TABLE session
    ADD CONSTRAINT FK_SESSION_ON_USER FOREIGN KEY (user_id) REFERENCES user_t (id) ON DELETE CASCADE;

CREATE TABLE marker
(
    id          UUID NOT NULL,
    marker_type VARCHAR(255),
    name        VARCHAR(255),
    lat         DOUBLE PRECISION,
    lng         DOUBLE PRECISION,
    CONSTRAINT pk_marker PRIMARY KEY (id)
);
