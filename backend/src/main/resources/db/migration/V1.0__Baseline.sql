CREATE TABLE Benutzer
(
    id         INT          NOT NULL AUTO_INCREMENT,
    namen      VARCHAR(100) NOT NULL,
    spotify_id VARCHAR(255) NOT NULL,
    datum      DATE         NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Gast
(
    id    INT          NOT NULL AUTO_INCREMENT,
    namen VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Party
(
    id           INT          NOT NULL AUTO_INCREMENT,
    benutzer_id  INT          NOT NULL,
    name         VARCHAR(255) NOT NULL,
    passwort     VARCHAR(255),
    vote_down    BOOLEAN DEFAULT TRUE,
    datum        DATE         NOT NULL,
    archiviert   BOOLEAN DEFAULT FALSE,
    beschreibung VARCHAR(500),
    PRIMARY KEY (id),
    CONSTRAINT `fk_party_benutzer_id`
        FOREIGN KEY (benutzer_id) REFERENCES Benutzer (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT
);

CREATE TABLE Musikwunsch
(
    id        INT          NOT NULL AUTO_INCREMENT,
    interpret VARCHAR(255) NOT NULL,
    titel     VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Abstimmung
(
    id             INT NOT NULL AUTO_INCREMENT,
    musikwunsch_id INT NOT NULL,
    benutzer_id    INT,
    gast_id        INT,
    PRIMARY KEY (id),
    CONSTRAINT `fk_abstimmung_musikwunsch_id`
        FOREIGN KEY (musikwunsch_id) REFERENCES Musikwunsch (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT,
    CONSTRAINT `fk_abstimmung_benutzer_id`
        FOREIGN KEY (benutzer_id) REFERENCES Benutzer (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT,
    CONSTRAINT `fk_abstimmung_gast_id`
        FOREIGN KEY (gast_id) REFERENCES Gast (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT
);
