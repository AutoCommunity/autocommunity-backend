CREATE TABLE event
(
    id            UUID NOT NULL,
    marker_id     UUID,
    starting_date TIMESTAMP WITHOUT TIME ZONE,
    end_date      TIMESTAMP WITHOUT TIME ZONE,
    status        VARCHAR(255),
    privacy_type  VARCHAR(255),
    owner_id      UUID,
    CONSTRAINT pk_event PRIMARY KEY (id)
);

ALTER TABLE event
    ADD CONSTRAINT FK_EVENT_ON_MARKER FOREIGN KEY (marker_id) REFERENCES marker (id) ON DELETE CASCADE;

ALTER TABLE event
    ADD CONSTRAINT FK_EVENT_ON_OWNER FOREIGN KEY (owner_id) REFERENCES user_t (id) ON DELETE CASCADE;


CREATE TABLE event_visitor
(
    event_visitors_id UUID NOT NULL,
    events_id         UUID NOT NULL,
    CONSTRAINT pk_event_visitor PRIMARY KEY (event_visitors_id, events_id)
);

ALTER TABLE event_visitor
    ADD CONSTRAINT fk_evevis_on_event_entity FOREIGN KEY (events_id) REFERENCES event (id);

ALTER TABLE event_visitor
    ADD CONSTRAINT fk_evevis_on_user_entity FOREIGN KEY (event_visitors_id) REFERENCES user_t (id);