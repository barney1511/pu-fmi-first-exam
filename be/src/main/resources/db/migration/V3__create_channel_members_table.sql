CREATE TABLE channel_members
(
    id          UUID                        NOT NULL,
    deleted     BOOLEAN                     NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by  VARCHAR(255),
    modified_by VARCHAR(255),
    version     BIGINT,
    user_id     UUID                        NOT NULL,
    channel_id  UUID                        NOT NULL,
    role        VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_channel_members PRIMARY KEY (id)
);

ALTER TABLE channel_members
    ADD CONSTRAINT uc_4f0af67ef316f67e3b43f3f35 UNIQUE (user_id, channel_id);

ALTER TABLE channel_members
    ADD CONSTRAINT FK_CHANNEL_MEMBERS_ON_CHANNEL FOREIGN KEY (channel_id) REFERENCES channels (id);

ALTER TABLE channel_members
    ADD CONSTRAINT FK_CHANNEL_MEMBERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);