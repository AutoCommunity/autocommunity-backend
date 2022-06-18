CREATE TABLE marker_rate
(
    id        UUID NOT NULL,
    marker_id UUID,
    user_id   UUID,
    rate      DOUBLE PRECISION CHECK (rate in (0, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5)),
    CONSTRAINT pk_marker_rate PRIMARY KEY (id)
);

ALTER TABLE marker_rate
    ADD CONSTRAINT FK_MARKER_RATE_ON_MARKER FOREIGN KEY (marker_id) REFERENCES marker (id) ON DELETE CASCADE;

ALTER TABLE marker_rate
    ADD CONSTRAINT FK_MARKER_RATE_ON_USER FOREIGN KEY (user_id) REFERENCES user_t (id) ON DELETE CASCADE;