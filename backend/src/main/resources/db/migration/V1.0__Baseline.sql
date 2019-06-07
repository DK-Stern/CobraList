CREATE TABLE User
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    provider    VARCHAR(30)  NOT NULL,
    provider_id VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE KEY (email)
);

CREATE TABLE Guest
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Party
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    user_id       BIGINT       NOT NULL,
    name          VARCHAR(255) NOT NULL,
    password      VARCHAR(255),
    vote_down     BOOLEAN DEFAULT TRUE,
    creation_date DATE         NOT NULL,
    archived      BOOLEAN DEFAULT FALSE,
    description   VARCHAR(500),
    PRIMARY KEY (id),
    CONSTRAINT `fk_party_user_id`
        FOREIGN KEY (user_id) REFERENCES User (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT
);

CREATE TABLE Music_Request
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    interpreter VARCHAR(255) NOT NULL,
    title       VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Vote
(
    id               BIGINT NOT NULL AUTO_INCREMENT,
    music_request_id BIGINT NOT NULL,
    user_id          BIGINT,
    guest_id         BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT `fk_voted_music_request_id`
        FOREIGN KEY (music_request_id) REFERENCES Music_Request (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT,
    CONSTRAINT `fk_voted_user_id`
        FOREIGN KEY (user_id) REFERENCES User (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT,
    CONSTRAINT `fk_voted_guest_id`
        FOREIGN KEY (guest_id) REFERENCES Guest (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT
);
