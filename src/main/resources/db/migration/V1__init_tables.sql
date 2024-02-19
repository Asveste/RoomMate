CREATE TABLE workspace
(
    id   SERIAL PRIMARY KEY,
    room UUID NOT NULL
);

CREATE TABLE trait
(
    trait         VARCHAR(300) NOT NULL,
    workspace     INTEGER      NOT NULL,
    workspace_key INTEGER      NOT NULL,
    FOREIGN KEY (workspace) REFERENCES workspace (id)
);

CREATE TABLE timespan
(
    date          DATE    NOT NULL,
    start_time    TIME    NOT NULL,
    end_time      TIME    NOT NULL,
    workspace     INTEGER NOT NULL,
    workspace_key INTEGER NOT NULL,
    FOREIGN KEY (workspace) REFERENCES workspace (id)
);
