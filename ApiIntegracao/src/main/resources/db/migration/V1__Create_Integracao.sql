CREATE TABLE IF NOT EXISTS consultas (
    id VARCHAR (36) NOT NULL,
    computador VARCHAR (250),
    ip VARCHAR (250),
    consulta DATETIME,
    PRIMARY KEY (id)
);