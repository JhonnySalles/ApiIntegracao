CREATE TABLE IF NOT EXISTS usuarios (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    nome varchar(255) DEFAULT NULL,
    username varchar(255) DEFAULT NULL,
    password varchar(255) DEFAULT NULL,
    conta_nao_expirada bit(1) DEFAULT NULL,
    conta_nao_travada bit(1) DEFAULT NULL,
    credencial_nao_expirado bit(1) DEFAULT NULL,
    ativo bit(1) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuario_username (username)
);

INSERT INTO usuarios (username, nome, password, conta_nao_expirada, conta_nao_travada, credencial_nao_expirado, ativo) VALUES
('admin', 'Admin', 'c579dd4bffe3efccd56540baf9b5743cd9ae61b9ee5a1f03eacfa49cbe26c65b3194eb32290d9ed5', b'1', b'1', b'1', b'1'),
('jhonny', 'Jhonny', '098344719160725f7c5e7448053cfe1b828b34be1942e6b97aa8f4e177912668df48c2df19681072', b'1', b'1', b'1', b'1');