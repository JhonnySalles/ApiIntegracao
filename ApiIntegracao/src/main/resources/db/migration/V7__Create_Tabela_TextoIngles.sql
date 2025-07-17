CREATE TABLE IF NOT EXISTS texto_ingles (
  id VARCHAR (36) NOT NULL,
  tabela VARCHAR (900),
  sincronizacao DATETIME DEFAULT NOW(),
  PRIMARY KEY (id)
);

DELETE FROM texto_ingles;

INSERT INTO texto_ingles (id, tabela)
SELECT UUID(), Table_Name FROM information_schema.tables
WHERE table_schema = "texto_ingles" GROUP BY Table_Name