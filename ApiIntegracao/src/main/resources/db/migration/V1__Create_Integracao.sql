CREATE TABLE IF NOT EXISTS atualizacoes (
  id int(11) NOT NULL AUTO_INCREMENT,
  base VARCHAR (100),
  tabela VARCHAR (250),
  atualizacao DATETIME,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS consultas (
    id VARCHAR (36) NOT NULL,
    computador VARCHAR (250),
    ip VARCHAR (250),
    consulta DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tabelas (
  id int(11) NOT NULL AUTO_INCREMENT,
  url VARCHAR (250),
  porta VARCHAR (10),
  username VARCHAR (250),
  password VARCHAR (250),
  base VARCHAR (100),
  driver enum('MYSQL') DEFAULT 'MYSQL',
  tipo enum('MANGA_EXTRACTOR','TEXTO_INGLES','DECKSUBTITLE') DEFAULT NULL,
  PRIMARY KEY (id)
);

