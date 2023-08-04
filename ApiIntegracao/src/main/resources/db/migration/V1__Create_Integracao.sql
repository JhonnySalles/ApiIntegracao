CREATE TABLE Atualizacao (
  id VARCHAR (36) NOT NULL,
  Computador VARCHAR (250),
  Ip VARCHAR (250),
  UltimaConsulta DATETIME,
  PRIMARY KEY (id)
);

CREATE TABLE tabelas (
  tenant VARCHAR (100) NOT NULL,
  url VARCHAR (250),
  username VARCHAR (250),
  PASSWORD VARCHAR (250),
  driver VARCHAR (250),
  PRIMARY KEY (tenant)
);

