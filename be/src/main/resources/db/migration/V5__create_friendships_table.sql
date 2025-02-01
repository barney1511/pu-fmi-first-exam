CREATE TABLE friendships
(
    id          UUID                        NOT NULL,
    deleted     BOOLEAN                     NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by  VARCHAR(255),
    modified_by VARCHAR(255),
    version     BIGINT,
    user_id     UUID                        NOT NULL,
    friend_id   UUID                        NOT NULL,
    status      VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_friendships PRIMARY KEY (id)
);

ALTER TABLE friendships
    ADD CONSTRAINT uc_9867f8cec806400c9f23982aa UNIQUE (user_id, friend_id);

ALTER TABLE friendships
    ADD CONSTRAINT FK_FRIENDSHIPS_ON_FRIEND FOREIGN KEY (friend_id) REFERENCES users (id);

ALTER TABLE friendships
    ADD CONSTRAINT FK_FRIENDSHIPS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);