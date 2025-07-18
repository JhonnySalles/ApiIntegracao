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
  driver enum('MYSQL', 'HIBERNATE') DEFAULT 'MYSQL',
  tipo enum('TEXTO_JAPONES','MANGA_EXTRACTOR','TEXTO_INGLES','DECKSUBTITLE') DEFAULT NULL,
  PRIMARY KEY (id)
);

INSERT INTO tabelas (url, porta, username, PASSWORD, base, driver, tipo) VALUES
('localhost', '3306', 'admin', 'admin', 'manga_extractor', 'MYSQL', 'MANGA_EXTRACTOR'),
('localhost', '3306', 'admin', 'admin', 'decksubtitle', 'MYSQL', 'DECKSUBTITLE'),
('localhost', '3306', 'admin', 'admin', 'texto_japones', 'HIBERNATE', 'TEXTO_JAPONES'),
('localhost', '3306', 'admin', 'admin', 'texto_ingles', 'HIBERNATE', 'TEXTO_INGLES');
