CREATE TABLE user
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    provider    VARCHAR(30)  NOT NULL,
    provider_id VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE KEY (email)
);

CREATE TABLE playlist
(
    id                       BIGINT       NOT NULL AUTO_INCREMENT,
    name                     VARCHAR(255) NOT NULL,
    id_on_streaming_platform VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE party
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    party_code    VARCHAR(6)   NOT NULL,
    creator_id    BIGINT       NOT NULL,
    playlist_id   BIGINT,
    name          VARCHAR(255) NOT NULL,
    password      VARCHAR(255),
    vote_down     BOOLEAN DEFAULT TRUE,
    creation_date DATE         NOT NULL,
    archived      BOOLEAN DEFAULT FALSE,
    description   VARCHAR(500),
    PRIMARY KEY (id),
    CONSTRAINT `fk_party_user_id`
        FOREIGN KEY (creator_id) REFERENCES user (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT,
    CONSTRAINT `fk_party_playlist_id`
        FOREIGN KEY (playlist_id) REFERENCES playlist (id)
            ON DELETE SET NULL
            ON UPDATE SET NULL
);

CREATE TABLE guest
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    party_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT `fk_guest_party_id`
        FOREIGN KEY (party_id) REFERENCES party (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT
);

CREATE TABLE user_joined_party
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    user_id  BIGINT NOT NULL,
    party_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT `fk_joined_user_id`
        FOREIGN KEY (user_id) REFERENCES user (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT,
    CONSTRAINT `fk_joined_party_id`
        FOREIGN KEY (party_id) REFERENCES party (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT
);

CREATE TABLE music_request
(
    id                       BIGINT       NOT NULL AUTO_INCREMENT,
    playlist_id              BIGINT       NOT NULL,
    title                    VARCHAR(255) NOT NULL,
    id_on_streaming_platform VARCHAR(255) NOT NULL,
    uri                      VARCHAR(255) NOT NULL,
    image_url                VARCHAR(255),
    image_width              INT,
    image_height             INT,
    PRIMARY KEY (id),
    CONSTRAINT `fk_music_request_playlist_id`
        FOREIGN KEY (playlist_id) REFERENCES playlist (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT
);

CREATE TABLE music_request_artist
(
    music_request_id BIGINT       NOT NULL,
    artist           VARCHAR(255) NOT NULL,
    PRIMARY KEY (music_request_id, artist),
    CONSTRAINT `fk_music_request_artist_id`
        FOREIGN KEY (music_request_id) REFERENCES music_request (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT
);

CREATE TABLE vote
(
    id               BIGINT NOT NULL AUTO_INCREMENT,
    music_request_id BIGINT NOT NULL,
    user_id          BIGINT,
    guest_id         BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT `fk_voted_music_request_id`
        FOREIGN KEY (music_request_id) REFERENCES music_request (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT,
    CONSTRAINT `fk_voted_user_id`
        FOREIGN KEY (user_id) REFERENCES user (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT,
    CONSTRAINT `fk_voted_guest_id`
        FOREIGN KEY (guest_id) REFERENCES guest (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT
);
