-- Habilitar extensões necessárias
 CREATE EXTENSION IF NOT EXISTS pgcrypto;
 -- Ou, se preferir:
 -- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

 -- Tabela sorteio ajustada
CREATE TABLE sorteio (
     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
     quantidade_sorteada INTEGER NOT NULL,
     quantidade_participantes INTEGER NOT NULL,
     nome_sorteio VARCHAR(255) NOT NULL,
     data_sorteio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     usuario_id BIGINT NOT NULL,
     nome_usuario VARCHAR(255) NOT NULL,
     CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuario(id)
 );

 CREATE OR REPLACE FUNCTION atualizar_data_hora()
 RETURNS TRIGGER AS $$
 BEGIN
     NEW.data_sorteio = CURRENT_TIMESTAMP;
     RETURN NEW;
 END;
 $$ LANGUAGE plpgsql;

 CREATE TRIGGER trigger_atualizar_data_hora
 BEFORE INSERT OR UPDATE ON sorteio
 FOR EACH ROW
 EXECUTE FUNCTION atualizar_data_hora();

 -- Tabela participante ajustada
CREATE TABLE participante (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      nome VARCHAR(255) NOT NULL,
      cpf VARCHAR(11) NOT NULL,
      sorteio VARCHAR(255) NOT NULL,
      hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      usuario_id BIGINT NOT NULL,
      nome_usuario VARCHAR(255) NOT NULL,
      CONSTRAINT participante_unique_cpf_sorteio UNIQUE (cpf, sorteio)
 );

CREATE TABLE sorteado (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      nome VARCHAR(255) NOT NULL,
      cpf VARCHAR(11) NOT NULL,
      sorteio VARCHAR(255) NOT NULL,
      hora_sorteio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      usuario_id BIGINT NOT NULL,
      nome_usuario VARCHAR(255) NOT NULL,
      CONSTRAINT sorteado_unique_cpf_sorteio UNIQUE (cpf, sorteio)
);