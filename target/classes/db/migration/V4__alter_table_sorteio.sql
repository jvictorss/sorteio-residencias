-- Renomear a coluna "id" para "uuid"
ALTER TABLE sorteio
    RENAME COLUMN id TO uuid;

-- Adicionar a nova coluna "id" como BIGINT e chave primária
ALTER TABLE sorteio
    ADD COLUMN id BIGSERIAL;

-- Garantir que a coluna "uuid" seja única e não nula
ALTER TABLE sorteio
    ALTER COLUMN uuid SET NOT NULL,
    ADD CONSTRAINT sorteio_uuid_unique UNIQUE (uuid);

-- Adicionar os campos da BaseEntity na tabela sorteio
 ALTER TABLE sorteio
     ADD COLUMN ativo BOOLEAN DEFAULT NULL,
     ADD COLUMN criado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     ADD COLUMN atualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     ADD COLUMN id_user BIGINT,
     ADD COLUMN id_user_atualizou BIGINT,
     ADD COLUMN hash VARCHAR(255) UNIQUE;