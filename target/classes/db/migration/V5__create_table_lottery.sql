CREATE TABLE lottery (
                         id BIGSERIAL PRIMARY KEY,
                         ativo BOOLEAN,
                         criado TIMESTAMP,
                         atualizado TIMESTAMP,
                         id_user BIGINT,
                         id_user_atualizou BIGINT,
                         hash VARCHAR(255) UNIQUE,

                         uuid UUID NOT NULL UNIQUE,
                         quantidade_sorteada INTEGER NOT NULL,
                         quantidade_participantes INTEGER NOT NULL,
                         nome_sorteio VARCHAR(255) NOT NULL,
                         data_sorteio TIMESTAMP NOT NULL,
                         nome_usuario VARCHAR(255) NOT NULL,
                         sorteados TEXT NOT NULL
);
