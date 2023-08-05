package br.com.fenix.apiIntegracao.database.dao.implement

import br.com.fenix.apiIntegracao.database.dao.MangaExtractorDao
import br.com.fenix.apiIntegracao.database.mysql.DB.closeResultSet
import br.com.fenix.apiIntegracao.database.mysql.DB.closeStatement
import br.com.fenix.apiIntegracao.exceptions.ExceptionDb
import br.com.fenix.apiIntegracao.messages.Mensagens
import br.com.fenix.apiIntegracao.model.mangaextractor.*
import java.sql.*
import java.util.*

class MangaDaoJDBC(private val conn: Connection) : MangaExtractorDao {

    companion object {
        private const val UPDATE_VOLUMES =
            "UPDATE %s_volumes SET manga = ?, volume = ?, linguagem = ?, arquivo = ?, is_processado = ? WHERE id = ?"
        private const val UPDATE_CAPITULOS =
            "UPDATE %s_capitulos SET manga = ?, volume = ?, capitulo = ?, linguagem = ?, is_extra = ?, scan = ?, is_processado = ? WHERE id = ?"
        private const val UPDATE_PAGINAS =
            "UPDATE %s_paginas SET nome = ?, numero = ?, hash_pagina = ?, is_processado = ? WHERE id = ?"
        private const val UPDATE_TEXTO =
            "UPDATE %s_textos SET sequencia = ?, texto = ?, posicao_x1 = ?, posicao_y1 = ?, posicao_x2 = ?, posicao_y2 = ? WHERE id = ?"


        private const val INSERT_VOLUMES =
            "INSERT INTO %s_volumes (manga, volume, linguagem, arquivo, is_processado) VALUES (?,?,?,?,?)"
        private const val INSERT_CAPITULOS =
            "INSERT INTO %s_capitulos (id_volume, manga, volume, capitulo, linguagem, scan, is_extra, is_raw, is_processado) VALUES (?,?,?,?,?,?,?,?,?)"
        private const val INSERT_PAGINAS =
            "INSERT INTO %s_paginas (id_capitulo, nome, numero, hash_pagina, is_processado) VALUES (?,?,?,?,?)"
        private const val INSERT_TEXTO =
            "INSERT INTO %s_textos (id_pagina, sequencia, texto, posicao_x1, posicao_y1, posicao_x2, posicao_y2) VALUES (?,?,?,?,?,?,?)"


        private const val DELETE_VOLUMES = "DELETE v FROM %s_volumes AS v %s"
        private const val DELETE_CAPITULOS =
            "DELETE c FROM %s_capitulos AS c INNER JOIN %s_volumes AS v ON v.id = c.id_volume %s"
        private const val DELETE_PAGINAS = ("DELETE p FROM %s_paginas p "
                + "INNER JOIN %s_capitulos AS c ON c.id = p.id_capitulo INNER JOIN %s_volumes AS v ON v.id = c.id_volume %s")
        private const val DELETE_TEXTOS =
            ("DELETE t FROM %s_textos AS t INNER JOIN %s_paginas AS p ON p.id = t.id_pagina "
                    + "INNER JOIN %s_capitulos AS c ON c.id = p.id_capitulo INNER JOIN %s_volumes AS v ON v.id = c.id_volume %s")


        private const val SELECT_VOLUMES =
            "SELECT VOL.id, VOL.manga, VOL.volume, VOL.linguagem, VOL.arquivo, VOL.is_Processado FROM %s_volumes VOL %s WHERE %s GROUP BY VOL.id ORDER BY VOL.manga, VOL.linguagem, VOL.volume"
        private const val SELECT_CAPITULOS =
            ("SELECT CAP.id, CAP.manga, CAP.volume, CAP.capitulo, CAP.linguagem, CAP.scan, CAP.is_extra, CAP.is_raw, CAP.is_processado "
                    + "FROM %s_capitulos CAP %s WHERE id_volume = ? AND %s GROUP BY CAP.id ORDER BY CAP.linguagem, CAP.volume, CAP.is_extra, CAP.capitulo")
        private const val SELECT_PAGINAS =
            "SELECT id, nome, numero, hash_pagina, is_processado FROM %s_paginas WHERE id_capitulo = ? AND %s "
        private const val SELECT_TEXTOS =
            "SELECT id, sequencia, texto, posicao_x1, posicao_y1, posicao_x2, posicao_y2 FROM %s_textos WHERE id_pagina = ? "
        private const val FIND_VOLUME =
            "SELECT VOL.id, VOL.manga, VOL.volume, VOL.linguagem, VOL.arquivo, VOL.is_Processado FROM %s_volumes VOL WHERE manga = ? AND volume = ? AND linguagem = ? LIMIT 1"
        private const val SELECT_VOLUME =
            "SELECT VOL.id, VOL.manga, VOL.volume, VOL.linguagem, VOL.arquivo, VOL.is_Processado FROM %s_volumes VOL WHERE id = ?"
        private const val SELECT_CAPITULO =
            ("SELECT CAP.id, CAP.manga, CAP.volume, CAP.capitulo, CAP.linguagem, CAP.scan, CAP.is_extra, CAP.is_raw, CAP.is_processado "
                    + "FROM %s_capitulos CAP WHERE id = ?")
        private const val SELECT_PAGINA =
            "SELECT id, nome, numero, hash_pagina, is_processado FROM %s_paginas WHERE id = ?"
        private const val INNER_CAPITULOS =
            "INNER JOIN %s_paginas PAG ON CAP.id = PAG.id_capitulo AND PAG.is_processado = 0"
        private const val INNER_VOLUMES = ("INNER JOIN %s_capitulos CAP ON VOL.id = CAP.id_volume "
                + "INNER JOIN %s_paginas PAG ON CAP.id = PAG.id_capitulo AND PAG.is_processado = 0")


        private const val DELETE_VOCABULARIO = "DELETE FROM %s_vocabulario WHERE %s;"
        private const val INSERT_VOCABULARIO =
            ("INSERT INTO %s_vocabulario (%s, palavra, portugues, ingles, leitura, revisado) "
                    + " VALUES (?,?,?,?,?,?);")
        private const val SELECT_VOCABUALARIO =
            "SELECT id, palavra, portugues, ingles, leitura, revisado FROM %s_vocabulario WHERE %s "


        private const val CREATE_VOLUMES =
            ("CREATE TABLE %s_volumes (id int(11) NOT NULL AUTO_INCREMENT, manga varchar(250) DEFAULT NULL, "
                    + "  volume int(4) DEFAULT NULL, linguagem varchar(4) DEFAULT NULL, arquivo varchar(250) DEFAULT NULL, vocabulario longtext, "
                    + "  is_processado tinyint(1) DEFAULT '0', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8")
        private const val CREATE_CAPITULOS =
            ("CREATE TABLE %s_capitulos (id INT(11) NOT NULL AUTO_INCREMENT, id_volume INT(11) DEFAULT NULL, "
                    + "  manga LONGTEXT COLLATE utf8mb4_unicode_ci NOT NULL, volume INT(4) NOT NULL, "
                    + "  capitulo DOUBLE NOT NULL, linguagem VARCHAR(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL, scan VARCHAR(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL, "
                    + "  is_extra TINYINT(1) DEFAULT NULL, is_raw TINYINT(1) DEFAULT NULL, is_processado TINYINT(1) DEFAULT '0', vocabulario LONGTEXT COLLATE utf8mb4_unicode_ci, "
                    + "  PRIMARY KEY (id), KEY %s_volumes_fk (id_volume), "
                    + "  CONSTRAINT %s_volumes_capitulos_fk FOREIGN KEY (id_volume) REFERENCES %s_volumes (id) ON DELETE CASCADE ON UPDATE CASCADE "
                    + ") ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci")
        private const val CREATE_PAGINAS =
            ("CREATE TABLE %s_paginas (id INT(11) NOT NULL AUTO_INCREMENT, id_capitulo INT(11) NOT NULL, "
                    + "  nome VARCHAR(250) DEFAULT NULL, numero INT(11) DEFAULT NULL, hash_pagina VARCHAR(250) DEFAULT NULL, is_processado TINYINT(1) DEFAULT '0', "
                    + "  vocabulario LONGTEXT, PRIMARY KEY (id), KEY %s_capitulos_fk (id_capitulo), "
                    + "  CONSTRAINT %s_capitulos_paginas_fk FOREIGN KEY (id_capitulo) REFERENCES %s_capitulos (id) ON DELETE CASCADE ON UPDATE CASCADE "
                    + ") ENGINE=INNODB DEFAULT CHARSET=utf8")
        private const val CREATE_TEXTO =
            ("CREATE TABLE %s_textos (id INT(11) NOT NULL AUTO_INCREMENT, id_pagina INT(11) NOT NULL, "
                    + "  sequencia INT(4) DEFAULT NULL, texto LONGTEXT COLLATE utf8mb4_unicode_ci, posicao_x1 DOUBLE DEFAULT NULL, "
                    + "  posicao_y1 DOUBLE DEFAULT NULL, posicao_x2 DOUBLE DEFAULT NULL, posicao_y2 DOUBLE DEFAULT NULL, versaoApp INT(11) DEFAULT '0', PRIMARY KEY (id), "
                    + "  KEY %s_paginas_fk (id_pagina), "
                    + "  CONSTRAINT %s_paginas_textos_fk FOREIGN KEY (id_pagina) REFERENCES %s_paginas (id) ON DELETE CASCADE ON UPDATE CASCADE "
                    + ") ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci")
        private const val CREATE_VOCABULARIO =
            ("CREATE TABLE %s_vocabulario (" + "  id INT(11) NOT NULL AUTO_INCREMENT,"
                    + "  id_volume INT(11) DEFAULT NULL," + "  id_capitulo INT(11) DEFAULT NULL,"
                    + "  id_pagina INT(11) DEFAULT NULL," + "  palavra VARCHAR(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,"
                    + "  portugues LONGTEXT COLLATE utf8mb4_unicode_ci," + "  ingles LONGTEXT COLLATE utf8mb4_unicode_ci,"
                    + "  leitura LONGTEXT COLLATE utf8mb4_unicode_ci," + "  revisado tinyint(1) DEFAULT 1," + "  PRIMARY KEY (id),"
                    + "  KEY %s_vocab_volume_fk (id_volume)," + "  KEY %s_vocab_capitulo_fk (id_capitulo)," + "  KEY %s_vocab_pagina_fk (id_pagina),"
                    + "  CONSTRAINT %s_vocab_capitulo_fk FOREIGN KEY (id_capitulo) REFERENCES %s_capitulos (id),"
                    + "  CONSTRAINT %s_vocab_pagina_fk FOREIGN KEY (id_pagina) REFERENCES %s_paginas (id),"
                    + "  CONSTRAINT %s_vocab_volume_fk FOREIGN KEY (id_volume) REFERENCES %s_volumes (id)"
                    + ") ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci")

        private const val EXIST_TABELA_VOCABULARIO = ("SELECT Table_Name AS Tabela "
                + " FROM information_schema.tables WHERE table_schema = 'manga_extractor' "
                + " AND Table_Name LIKE '%%_vocabulario%%' AND Table_Name LIKE '%%%s%%' GROUP BY Tabela ")

        private const val SELECT_LISTA_TABELAS = ("SELECT REPLACE(Table_Name, '_volumes', '') AS Tabela "
                + " FROM information_schema.tables WHERE table_schema = 'manga_extractor' "
                + " AND Table_Name LIKE '%%_volumes%%' GROUP BY Tabela ")
    }

    // -------------------------------------------------------------------------------------------------------------  //

    override fun updateVolume(base: String, obj: Volumes) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(
                String.format(UPDATE_VOLUMES, base),
                Statement.RETURN_GENERATED_KEYS
            )
            st.setString(1, obj.manga)
            st.setInt(2, obj.volume)
            st.setString(3, obj.linguagem!!.sigla)
            st.setString(4, obj.arquivo)
            st.setBoolean(5, obj.isProcessado)
            st.setLong(6, obj.getId())
            insertVocabulario(base, obj.getId(), null, null, obj.vocabulario)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                println("Nenhum registro atualizado.")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updateCapitulo(base: String, obj: Capitulos) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(
                String.format(UPDATE_CAPITULOS, base),
                Statement.RETURN_GENERATED_KEYS
            )
            st.setString(1, obj.manga)
            st.setInt(2, obj.volume)
            st.setDouble(3, obj.capitulo)
            st.setString(4, obj.linguagem!!.sigla)
            st.setBoolean(5, obj.isExtra)
            st.setString(6, obj.scan)
            st.setBoolean(7, obj.isProcessado)
            st.setLong(8, obj.getId())
            insertVocabulario(base, null, obj.getId(), null, obj.vocabulario)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                println("Nenhum registro atualizado.")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updatePagina(base: String, obj: Paginas) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(
                String.format(UPDATE_PAGINAS, base),
                Statement.RETURN_GENERATED_KEYS
            )
            st.setString(1, obj.nomePagina)
            st.setInt(2, obj.numero)
            st.setString(3, obj.hashPagina)
            st.setBoolean(4, obj.isProcessado)
            st.setLong(5, obj.getId())
            insertVocabulario(base, null, null, obj.getId(), obj.vocabulario)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updateTexto(base: String, obj: Textos) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(UPDATE_TEXTO, base), Statement.RETURN_GENERATED_KEYS)
            st.setInt(1, obj.sequencia)
            st.setString(2, obj.texto)
            st.setInt(3, obj.posicao_x1)
            st.setInt(4, obj.posicao_y1)
            st.setInt(5, obj.posicao_x2)
            st.setInt(6, obj.posicao_y2)
            st.setLong(7, obj.getId())
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_UPDATE)
        } finally {
            closeStatement(st)
        }
    }

    override fun updateVocabulario(base: String, obj: Vocabulario) {
        TODO("Not yet implemented")
    }

    // -------------------------------------------------------------------------------------------------------------  //

    override fun insertVolume(base: String, obj: Volumes): Long {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(
                String.format(INSERT_VOLUMES, base),
                Statement.RETURN_GENERATED_KEYS
            )
            st.setString(1, obj.manga)
            st.setInt(2, obj.volume)
            st.setString(3, obj.linguagem!!.sigla)
            st.setString(4, obj.arquivo)
            st.setBoolean(5, obj.isProcessado)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
            } else {
                val rs = st.generatedKeys
                var id: Long = 0
                if (rs.next()) id = rs.getLong(1)
                insertVocabulario(base, id, null, null, obj.vocabulario)
                id!!
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
    }

    override fun insertCapitulo(base: String, idVolume: Long, obj: Capitulos): Long {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(
                String.format(INSERT_CAPITULOS, base),
                Statement.RETURN_GENERATED_KEYS
            )
            st.setLong(1, idVolume)
            st.setString(2, obj.manga)
            st.setInt(3, obj.volume)
            st.setFloat(4, obj.capitulo.toFloat())
            st.setString(5, obj.linguagem!!.sigla)
            st.setString(6, obj.scan)
            st.setBoolean(7, obj.isExtra)
            st.setBoolean(8, obj.isRaw)
            st.setBoolean(9, obj.isProcessado)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
            } else {
                val rs = st.generatedKeys
                var id: Long = 0
                if (rs.next()) id = rs.getLong(1)
                insertVocabulario(base, null, id, null, obj.vocabulario)
                id
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
    }

    override fun insertPagina(base: String, idCapitulo: Long, obj: Paginas): Long {
        var st: PreparedStatement? = null
        return try {
            st = conn.prepareStatement(
                String.format(INSERT_PAGINAS, base),
                Statement.RETURN_GENERATED_KEYS
            )
            st.setLong(1, idCapitulo)
            st.setString(2, obj.nomePagina)
            st.setInt(3, obj.numero)
            st.setString(4, obj.hashPagina)
            st.setBoolean(5, obj.isProcessado)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
            } else {
                val rs = st.generatedKeys
                var id: Long = 0
                if (rs.next()) id = rs.getLong(1)
                insertVocabulario(base, null, null, id, obj.vocabulario)
                id
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
    }

    override fun insertTexto(base: String, idPagina: Long, obj: Textos): Long {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(INSERT_TEXTO, base), Statement.RETURN_GENERATED_KEYS)
            st.setLong(1, idPagina)
            st.setInt(2, obj.sequencia)
            st.setString(3, obj.texto)
            st.setInt(4, obj.posicao_x1)
            st.setInt(5, obj.posicao_y1)
            st.setInt(6, obj.posicao_x2)
            st.setInt(7, obj.posicao_y2)
            val rowsAffected = st.executeUpdate()
            if (rowsAffected < 1) {
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
            } else {
                val rs = st.generatedKeys
                if (rs.next()) return rs.getLong(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
        return 0
    }

    override fun insertVocabulario(
        base: String,
        idVolume: Long?,
        idCapitulo: Long?,
        idPagina: Long?,
        vocabulario: Set<Vocabulario>
    ) {
        var st: PreparedStatement? = null
        try {
            if (idVolume == null && idCapitulo == null && idPagina == null) return
            val where = if (idVolume != null) "id_volume = $idVolume"
            else if (idCapitulo != null) "id_capitulo = $idCapitulo"
            else "id_pagina = $idPagina"
            //clearVocabulario(base, where)
            val campo = if (idVolume != null) "id_volume" else if (idCapitulo != null) "id_capitulo" else "id_pagina"
            val id = idVolume ?: (idCapitulo ?: idPagina)!!
            for (vocab in vocabulario) {
                st = conn.prepareStatement(
                    String.format(INSERT_VOCABULARIO, base, campo),
                    Statement.RETURN_GENERATED_KEYS
                )
                st.setLong(1, id)
                st.setString(2, vocab.palavra)
                st.setString(3, vocab.portugues)
                st.setString(4, vocab.ingles)
                st.setString(5, vocab.leitura)
                st.setBoolean(6, vocab.isRevisado)
                st.executeUpdate()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_INSERT)
        } finally {
            closeStatement(st)
        }
    }

    // -------------------------------------------------------------------------------------------------------------  //

    override fun selectVolume(base: String, id: Long): Volumes {
        TODO("Not yet implemented")
    }

    override fun selectCapitulo(base: String, id: Long): Capitulos {
        TODO("Not yet implemented")
    }

    override fun selectPagina(base: String, id: Long): Paginas {
        TODO("Not yet implemented")
    }

    override fun selectTexto(base: String, id: Long): Textos {
        TODO("Not yet implemented")
    }

    override fun selectVocabuario(base: String, id: Long): Vocabulario {
        TODO("Not yet implemented")
    }

    override fun selectAllCapitulo(base: String, idVolume: Long): List<Capitulos> {
        TODO("Not yet implemented")
    }

    override fun selectAllPagina(base: String, idCapitulo: Long): List<Vocabulario> {
        TODO("Not yet implemented")
    }

    override fun selectAllText(base: String, idPagina: Long): List<Paginas> {
        TODO("Not yet implemented")
    }

    override fun selectAllVocabuario(
        base: String,
        idVolume: Long?,
        idCapitulo: Long?,
        idPagina: Long?
    ): Set<Vocabulario> {
        TODO("Not yet implemented")
    }

    // -------------------------------------------------------------------------------------------------------------  //

    override fun deleteVolume(base: String, obj: Volumes) {
        var stVolume: PreparedStatement? = null
        var stCapitulo: PreparedStatement? = null
        var stPagina: PreparedStatement? = null
        var stTexto: PreparedStatement? = null
        try {
            var where = "WHERE "
            if (obj.getId() != null)
                where += " v.id = " + obj.getId().toString()
            else
                where += " v.manga = '" + obj.manga + "' AND v.volume = " + obj.volume.toString() + " AND v.linguagem = '" + obj.linguagem!!.sigla + "'"
            stTexto = conn.prepareStatement(
                String.format(
                    DELETE_TEXTOS,
                    base,
                    base,
                    base,
                    base,
                    where
                )
            )
            stPagina = conn.prepareStatement(String.format(DELETE_PAGINAS, base, base, base, where))
            stCapitulo = conn.prepareStatement(String.format(DELETE_CAPITULOS, base, base, where))
            stVolume = conn.prepareStatement(String.format(DELETE_VOLUMES, base, where))
            conn.autoCommit = false
            conn.beginRequest()
            stTexto.executeUpdate()
            stPagina.executeUpdate()
            stCapitulo.executeUpdate()
            stVolume.executeUpdate()
            conn.commit()
        } catch (e: SQLException) {
            try {
                conn.rollback()
            } catch (e1: SQLException) {
                e1.printStackTrace()
            }
            println(stTexto.toString())
            println(stPagina.toString())
            println(stCapitulo.toString())
            println(stVolume.toString())
            e.printStackTrace()
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            try {
                conn.autoCommit = true
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            closeStatement(stTexto)
            closeStatement(stPagina)
            closeStatement(stCapitulo)
            closeStatement(stVolume)
        }
    }

    override fun deleteCapitulo(base: String, obj: Capitulos) {
        var stCapitulo: PreparedStatement? = null
        var stPagina: PreparedStatement? = null
        var stTexto: PreparedStatement? = null
        try {
            val where = "WHERE c.id = " + obj.getId().toString()
            stTexto = conn.prepareStatement(
                String.format(
                    DELETE_TEXTOS,
                    base,
                    base,
                    base,
                    base,
                    where
                )
            )
            stPagina = conn
                .prepareStatement(String.format(DELETE_PAGINAS, base, base, base, where))
            stCapitulo = conn.prepareStatement(String.format(DELETE_CAPITULOS, base, base, where))
            conn.autoCommit = false
            conn.beginRequest()
            stTexto.executeUpdate()
            stPagina.executeUpdate()
            stCapitulo.executeUpdate()
            conn.commit()
        } catch (e: SQLException) {
            try {
                conn.rollback()
            } catch (e1: SQLException) {
                e1.printStackTrace()
            }
            println(stTexto.toString())
            println(stPagina.toString())
            println(stCapitulo.toString())
            e.printStackTrace()
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            try {
                conn.autoCommit = true
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            closeStatement(stTexto)
            closeStatement(stPagina)
            closeStatement(stCapitulo)
        }
    }

    override fun deletePagina(base: String, obj: Paginas) {
        var stPagina: PreparedStatement? = null
        var stTexto: PreparedStatement? = null
        try {
            val where = "WHERE p.id = " + obj.getId().toString()
            stTexto = conn.prepareStatement(
                String.format(
                    DELETE_TEXTOS,
                    base,
                    base,
                    base,
                    base,
                    where
                )
            )
            stPagina = conn
                .prepareStatement(String.format(DELETE_PAGINAS, base, base, base, where))
            conn.autoCommit = false
            conn.beginRequest()
            stTexto.executeUpdate()
            stPagina.executeUpdate()
            conn.commit()
        } catch (e: SQLException) {
            try {
                conn.rollback()
            } catch (e1: SQLException) {
                e1.printStackTrace()
            }
            println(stTexto.toString())
            println(stPagina.toString())
            e.printStackTrace()
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            try {
                conn.autoCommit = true
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            closeStatement(stTexto)
            closeStatement(stPagina)
        }
    }

    override fun deleteTexto(base: String, obj: Textos) {
        var stTexto: PreparedStatement? = null
        try {
            val where = "WHERE t.id = " + obj.getId().toString()
            stTexto = conn.prepareStatement(
                String.format(
                    DELETE_TEXTOS,
                    base,
                    base,
                    base,
                    base,
                    where
                )
            )
            conn.autoCommit = false
            conn.beginRequest()
            stTexto.executeUpdate()
            conn.commit()
        } catch (e: SQLException) {
            try {
                conn.rollback()
            } catch (e1: SQLException) {
                e1.printStackTrace()
            }
            println(stTexto.toString())
            e.printStackTrace()
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            try {
                conn.autoCommit = true
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            closeStatement(stTexto)
        }
    }

    override fun deletarVocabulario(base: String, idVolume: Long?, idCapitulo: Long?, idPagina: Long?) {
        var stVocabulario: PreparedStatement? = null
        try {
            stVocabulario = conn.prepareStatement(String.format("DELETE FROM %s_vocabulario", base))
            conn.autoCommit = false
            conn.beginRequest()
            stVocabulario.executeUpdate()
            conn.commit()
        } catch (e: SQLException) {
            try {
                conn.rollback()
            } catch (e1: SQLException) {
                e1.printStackTrace()
            }
            println(stVocabulario.toString())
            e.printStackTrace()
            throw ExceptionDb(Mensagens.BD_ERRO_DELETE)
        } finally {
            try {
                conn.autoCommit = true
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            closeStatement(stVocabulario)
        }
    }

    // -------------------------------------------------------------------------------------------------------------  //

    override fun createDatabase(nome: String) {
        var st: PreparedStatement? = null
        try {
            st = conn.prepareStatement(String.format(CREATE_VOLUMES, nome))
            st.execute()
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
        try {
            st = conn.prepareStatement(String.format(CREATE_CAPITULOS, nome, nome, nome, nome))
            st.execute()
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
        try {
            st = conn.prepareStatement(String.format(CREATE_PAGINAS, nome, nome, nome, nome))
            st.execute()
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
        try {
            st = conn.prepareStatement(String.format(CREATE_TEXTO, nome, nome, nome, nome))
            st.execute()
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
        try {
            st = conn.prepareStatement(
                String.format(
                    CREATE_VOCABULARIO, nome, nome, nome, nome, nome,
                    nome, nome, nome, nome, nome
                )
            )
            st.execute()
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
    }

    override fun existDatabase(nome: String): Boolean {
        var st: PreparedStatement? = null
        val rs: ResultSet?
        try {
            st = conn.prepareStatement(
                String.format(
                    EXIST_TABELA_VOCABULARIO,
                    nome
                )
            )
            rs = st.executeQuery()
            return rs.next()
        } catch (e: SQLException) {
            e.printStackTrace()
            println(st.toString())
            throw ExceptionDb(Mensagens.BD_ERRO_CREATE_DATABASE)
        } finally {
            closeStatement(st)
        }
    }

    // -------------------------------------------------------------------------------------------------------------  //

    override fun selectAllTabela(): List<Tabelas> {
        TODO("Not yet implemented")
    }

    @get:Throws(ExceptionDb::class)
    override val tabelas: List<String>
        get() {
            var st: PreparedStatement? = null
            var rs: ResultSet? = null
            return try {
                st = conn.prepareStatement(
                    String.format(
                        SELECT_LISTA_TABELAS
                    )
                )
                rs = st.executeQuery()
                val list: MutableList<String> = ArrayList()
                while (rs.next()) list.add(rs.getString("Tabela"))
                list
            } catch (e: SQLException) {
                e.printStackTrace()
                println(st.toString())
                throw ExceptionDb(Mensagens.BD_ERRO_SELECT)
            } finally {
                closeStatement(st)
                closeResultSet(rs)
            }
        }
}