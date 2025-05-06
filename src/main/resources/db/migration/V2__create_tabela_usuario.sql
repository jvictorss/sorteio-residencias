CREATE SEQUENCE public.sq_user_id
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.usuario (
                                              id BIGINT DEFAULT nextval('public.sq_user_id') PRIMARY KEY,
                                              nome TEXT NOT NULL,
                                              cpf TEXT NOT NULL,
                                              sexo TEXT NOT NULL,
                                              email TEXT NOT NULL UNIQUE,
                                              senha TEXT NOT NULL,
                                              tipo TEXT NOT NULL,
                                              observacao TEXT,
                                              ativo BOOLEAN NOT NULL DEFAULT TRUE,
                                              criado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                              atualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                              id_user BIGINT NOT NULL,
                                              id_user_atualizou BIGINT NOT NULL,
                                              hash TEXT NOT NULL
);

CREATE OR REPLACE FUNCTION public.atualizar_alteracao_usuario()
    RETURNS trigger
    LANGUAGE plpgsql AS $$
BEGIN
    UPDATE public.usuario SET atualizado = CURRENT_TIMESTAMP WHERE id = OLD.id;
    RETURN NEW;
END;
$$;

CREATE TRIGGER atualizar_data_usuario
    BEFORE UPDATE OF nome, email, senha, tipo, ativo, sexo, cpf, hash, observacao
    ON public.usuario
    FOR EACH ROW
EXECUTE FUNCTION public.atualizar_alteracao_usuario();

INSERT INTO public.usuario (id, nome, cpf, sexo, email, senha, tipo, hash, ativo, criado, atualizado, id_user, id_user_atualizou) VALUES (
'1'::bigint, 'João Victor da Silva Santos'::text, '01489404473'::text, 'Masculino'::text, 'joaovss1992@gmail.com'::text, '$2a$10$gBz8EBjEORV7x1zgDJCVj.UW.GPC3jIpPGgjb0Tg3.bIWn8Ue.Mu6'::text, 'ROLE_ADMINISTRADOR'::text,'ttt'::text, true, '2022-12-28 23:30:00'::timestamp without time zone, '2022-12-28 23:30:00'::timestamp without time zone, '1'::bigint, '1'::bigint);