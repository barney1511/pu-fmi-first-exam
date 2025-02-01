CREATE TABLE messages
(
    id           UUID                        NOT NULL,
    deleted      BOOLEAN                     NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by   VARCHAR(255),
    modified_by  VARCHAR(255),
    version      BIGINT,
    sender_id    UUID                        NOT NULL,
    recipient_id UUID,
    channel_id   UUID                        NOT NULL,
    content      TEXT                        NOT NULL,
    type         VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_CHANNEL FOREIGN KEY (channel_id) REFERENCES channels (id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_RECIPIENT FOREIGN KEY (recipient_id) REFERENCES users (id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_SENDER FOREIGN KEY (sender_id) REFERENCES users (id);