CREATE TABLE "user"
(
    id            VARCHAR(255) NOT NULL,
    email         VARCHAR(255),
    username      VARCHAR(255),
    password_hash VARCHAR(255),
    created       TIMESTAMP WITHOUT TIME ZONE,
    changed       TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_user PRIMARY KEY (id)
);