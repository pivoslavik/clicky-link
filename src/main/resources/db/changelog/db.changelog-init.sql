--liquibase formatted sql

--changeset d.lebedev:001-create-link-table
CREATE SEQUENCE links_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE links (
    id BIGINT PRIMARY KEY DEFAULT nextval('links_seq'),
    short_url VARCHAR(64) NOT NULL UNIQUE,
    original_url VARCHAR(2048) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX idx_links_short_url ON links (short_url);

--rollback DROP TABLE links;
