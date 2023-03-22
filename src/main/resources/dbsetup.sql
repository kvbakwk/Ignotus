CREATE TABLE IF NOT EXISTS ignotus_players
(
    uuid        CHAR(36)        NOT NULL,
    nick        VARCHAR(100)    NOT NULL,
    instagram   VARCHAR(100)    DEFAULT '' NOT NULL,
    youtube     VARCHAR(100)    DEFAULT '' NOT NULL,
    twitch      VARCHAR(100)    DEFAULT '' NOT NULL,
    snapchat    VARCHAR(100)    DEFAULT '' NOT NULL,
    discord     VARCHAR(100)    DEFAULT '' NOT NULL,
    status      VARCHAR(100)    DEFAULT 'off' NOT NULL,
    PRIMARY KEY (uuid)
);