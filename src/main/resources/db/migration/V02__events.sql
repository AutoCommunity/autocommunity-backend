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
    id       UUID NOT NULL,
    user_id  UUID,
    event_id UUID,
    CONSTRAINT pk_event_visitor PRIMARY KEY (id)
);

ALTER TABLE event_visitor
    ADD CONSTRAINT FK_EVENT_VISITOR_ON_EVENT FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE;

ALTER TABLE event_visitor
    ADD CONSTRAINT FK_EVENT_VISITOR_ON_USER FOREIGN KEY (user_id) REFERENCES user_t (id) ON DELETE CASCADE;