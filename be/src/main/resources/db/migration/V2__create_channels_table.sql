CREATE TABLE channels
(
    id          UUID                        NOT NULL,
    deleted     BOOLEAN                     NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by  VARCHAR(255),
    modified_by VARCHAR(255),
    version     BIGINT,
    name        VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_channels PRIMARY KEY (id)
);