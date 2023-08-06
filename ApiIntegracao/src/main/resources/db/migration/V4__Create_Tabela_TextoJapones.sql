CREATE TABLE texto_japones (
  id VARCHAR (36) NOT NULL,
  tabela VARCHAR (900),
  sincronizacao DATETIME DEFAULT NOW(),
  PRIMARY KEY (id)
);

INSERT INTO texto_japones (id, tabela)
SELECT UUID(), Table_Name FROM information_schema.tables
WHERE table_schema = "texto_japones" GROUP BY Tabela