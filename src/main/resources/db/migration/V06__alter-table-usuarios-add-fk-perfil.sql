ALTER TABLE usuarios ADD COLUMN perfil_id BIGINT;

ALTER TABLE usuarios ADD CONSTRAINT fk_perfil_usuario FOREIGN KEY (perfil_id) REFERENCES perfis(id);

UPDATE usuarios SET perfil_id = (SELECT id FROM perfis WHERE nome = 'ROLE_USER') WHERE perfil_id IS NULL;

ALTER TABLE usuarios MODIFY COLUMN perfil_id BIGINT NOT NULL;