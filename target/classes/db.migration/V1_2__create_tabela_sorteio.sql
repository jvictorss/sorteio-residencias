CREATE TABLE sorteio (
                          id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                          nome_sorteado VARCHAR(255) NOT NULL,
                          data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          usuario_id INT NOT NULL
);

CREATE OR REPLACE FUNCTION atualizar_data_hora()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_hora = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_atualizar_data_hora
    BEFORE INSERT OR UPDATE ON sorteios
                         FOR EACH ROW
                         EXECUTE FUNCTION atualizar_data_hora();