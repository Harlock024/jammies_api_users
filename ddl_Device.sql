CREATE TABLE device
(
    id                UUID    NOT NULL,
    user_id           UUID,
    name              VARCHAR(255),
    active            BOOLEAN NOT NULL,
    last_connected_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_device PRIMARY KEY (id)
);

ALTER TABLE device
    ADD CONSTRAINT FK_DEVICE_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);