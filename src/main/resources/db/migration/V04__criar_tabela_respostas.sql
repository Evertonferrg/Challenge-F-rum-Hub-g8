CREATE TABLE respostas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensagem TEXT NOT NULL,
    data_criacao DATETIME NOT NULL,
    autor_id BIGINT NOT NULL,
    topico_id BIGINT NOT NULL,
    solucao BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_respostas_autor_id FOREIGN KEY (autor_id) REFERENCES usuarios(id),
    CONSTRAINT fk_respostas_topico_id FOREIGN KEY (topico_id) REFERENCES topicos(id)
);
