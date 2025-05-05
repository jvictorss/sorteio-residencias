CREATE SEQUENCE public.sq_user_id
    INCREMENT 1
    START 2
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.sq_user_id
    OWNER TO postgres;

-- Table: public.tb_usuario
-- DROP TABLE public.tb_usuario;
CREATE TABLE IF NOT EXISTS public.usuario
(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    nome text COLLATE pg_catalog."default" NOT NULL,
    cpf text COLLATE pg_catalog."default" NOT NULL,
    sexo text COLLATE pg_catalog."default" NOT NULL,
    email text COLLATE pg_catalog."default" NOT NULL,
    senha text COLLATE pg_catalog."default" NOT NULL,
    tipo text COLLATE pg_catalog."default" NOT NULL,
    observacao text COLLATE pg_catalog."default",
    ativo text COLLATE pg_catalog."default" NOT NULL,
    criado timestamp without time zone,
    atualizado timestamp without time zone,
    id_user bigint NOT NULL,
    id_user_atualizou bigint NOT NULL,
    hash text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT tb_usuario_pkey PRIMARY KEY (id),
    CONSTRAINT tb_usuario_email_key UNIQUE (email)
    )

    TABLESPACE pg_default;

ALTER TABLE public.tb_usuario
    OWNER to postgres;


-- FUNCTION: public.atualizar_alteracao_usuario()
-- DROP FUNCTION public.atualizar_alteracao_usuario();

CREATE OR REPLACE FUNCTION public.atualizar_alteracao_usuario()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
update tb_usuario
set atualizado= current_timestamp
where id= old.id;
return null;
END;
$BODY$;

ALTER FUNCTION public.atualizar_alteracao_usuario()
    OWNER TO postgres;


-- Trigger: atualizar_data_usuario
-- DROP TRIGGER atualizar_data ON public.tb_usuario;

CREATE TRIGGER atualizar_data_usuario
    AFTER UPDATE OF nome, email, senha, tipo, ativo, sexo, cpf, hash, observacao
    ON public.tb_usuario
    FOR EACH ROW
    EXECUTE FUNCTION public.atualizar_alteracao_usuario();

INSERT INTO public.tb_usuario (id, nome, cpf, sexo, email, senha, tipo, hash, ativo, criado, atualizado, id_user, id_user_atualizou) VALUES (
'1'::bigint, 'Jackson Douglas de Souza'::text, '067.558.784-08'::text, 'Masculino'::text, 'jdsmax@gmail.com'::text, '$2a$10$gBz8EBjEORV7x1zgDJCVj.UW.GPC3jIpPGgjb0Tg3.bIWn8Ue.Mu6'::text, 'ROLE_ADMINISTRADOR'::text,'ttt'::text, true, '2022-12-28 23:30:00'::timestamp without time zone, '2022-12-28 23:30:00'::timestamp without time zone, '1'::bigint, '1'::bigint);
'1'::bigint, 'João Victor da Silva Santos'::text, '014.894.044-73'::text, 'Masculino'::text, 'joaovss1992@gmail.com'::text, '$2a$10$gBz8EBjEORV7x1zgDJCVj.UW.GPC3jIpPGgjb0Tg3.bIWn8Ue.Mu6'::text, 'ROLE_ADMINISTRADOR'::text,'ttt'::text, true, '2022-12-28 23:30:00'::timestamp without time zone, '2022-12-28 23:30:00'::timestamp without time zone, '1'::bigint, '1'::bigint);