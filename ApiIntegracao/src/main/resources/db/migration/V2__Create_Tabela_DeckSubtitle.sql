CREATE TABLE decksubtitle (
  id VARCHAR (36) NOT NULL,
  tabela VARCHAR (900),
  UltimaSincronizacao DATETIME DEFAULT NOW(),
  isSqls tinyint(1) DEFAULT '0',
  PRIMARY KEY (id)
);

INSERT INTO decsubtitle (id, tabela, isSqls)
SELECT UUID(), Table_Name, 1 FROM information_schema.tables
WHERE table_schema = "decksubtitle" AND Table_Name = "_sql" GROUP BY Tabela
UNION ALL
SELECT UUID(), Table_Name, 0 FROM information_schema.tables
WHERE table_schema = "decksubtitle" AND Table_Name <> "_sql" GROUP BY Tabela