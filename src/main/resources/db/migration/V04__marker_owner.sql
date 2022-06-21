ALTER TABLE marker ADD COLUMN owner UUID;
ALTER TABLE marker ADD COLUMN status VARCHAR(255);

ALTER TABLE marker
    ADD CONSTRAINT FK_MARKER_ON_OWNER FOREIGN KEY (owner) REFERENCES user_t (id);


CREATE INDEX ON event (starting_date);
CREATE INDEX ON event (privacy_type);

CREATE INDEX ON marker_rate (marker_id, user_id);

CREATE INDEX ON marker (status);

CREATE INDEX ON session (session);
CREATE INDEX ON session (expiration_time, status);
CREATE INDEX ON session (user_id, status);

CREATE INDEX ON user_t (username)