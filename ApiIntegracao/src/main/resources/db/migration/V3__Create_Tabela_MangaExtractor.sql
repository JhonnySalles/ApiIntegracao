CREATE TABLE manga_extractor (
  id VARCHAR (36) NOT NULL,
  tabela VARCHAR (900),
  UltimaSincronizacao DATETIME DEFAULT NOW(),
   isVocabulario tinyint(1) DEFAULT '0',
  PRIMARY KEY (id)
);

INSERT INTO manga_extractor (id, tabela, isVocabulario)
SELECT UUID(), Table_Name, 1 FROM information_schema.tables
WHERE table_schema = "manga_extractor" AND Table_Name = "_vocabulario" GROUP BY Tabela
UNION ALL
SELECT UUID(), Table_Name, 0 FROM information_schema.tables
WHERE table_schema = "manga_extractor" AND Table_Name <> "_vocabulario" GROUP BY Tabela