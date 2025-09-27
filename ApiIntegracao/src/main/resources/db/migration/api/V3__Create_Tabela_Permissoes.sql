CREATE TABLE IF NOT EXISTS permissoes (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS usuarios_permissoes (
    id_usuario BIGINT(20) NOT NULL,
    id_permissao BIGINT(20) NOT NULL,
    PRIMARY KEY ( id_usuario, id_permissao ),
    KEY fk_usuario_permissao_permissao ( id_permissao ),
    CONSTRAINT fk_usuario_permissao FOREIGN KEY ( id_usuario ) REFERENCES usuarios ( id ),
    CONSTRAINT fk_permissao_permissao FOREIGN KEY ( id_permissao ) REFERENCES permissoes ( id )
);

INSERT INTO permissoes (descricao)
VALUES ('ADMIN'),
       ('MANAGER'),
       ('COMMON_USER');