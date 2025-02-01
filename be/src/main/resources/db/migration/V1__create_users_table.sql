CREATE TABLE users
(
    id          UUID                        NOT NULL,
    deleted     BOOLEAN                     NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by  VARCHAR(255),
    modified_by VARCHAR(255),
    version     BIGINT,
    username    VARCHAR(255)                NOT NULL,
    password    VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);