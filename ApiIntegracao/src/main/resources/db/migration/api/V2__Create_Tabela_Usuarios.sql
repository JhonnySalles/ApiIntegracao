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
('admin', 'Admin', '$2a$10$BipJ1Wv3lo7QBybP/Lo6n.Fge32f17DEKjNvciEuOBJfSYvJrB2ze', b'1', b'1', b'1', b'1'),
('jhonny', 'Jhonny', '$2a$10$3GqPlc2fJ21KEOIWCA2csu54EhqMDVwtM0dyicLhwL9aF6CvtfloO', b'1', b'1', b'1', b'1');