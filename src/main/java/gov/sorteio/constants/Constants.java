package gov.sorteio.constants;

import gov.sorteio.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Constants {
    public static final String ROTA_EXCEPTION = "?exception=true";
    public static final String ERROR = "error";
    public static final String ZONE_ID = "America/Sao_Paulo";
    public static final String MSG_ERROR_FIELD = "Campo não deve estar em branco, pois é obrigatório.\n";
    public static final String MSG_GENERIC = "Desculpe! Não foi possível efetuar a operação.";
    public static final String TITLE_GENERIC = "Falha ao efetuar a operação";
    public static final String MSG_OBJ_DUPLICADO = "Desculpe! Não foi possível efetuar a operação, informações já cadastradas no Banco de Dados.";
    public static final Map<String, Object> entidades = new HashMap<>();
    public static final Map<String, Object> listEntidades = new HashMap<>();
    public static final Map<String, String> listModels = new HashMap<>();
    public static final Map<String, Integer> pathPai = new HashMap<>();
    public static final Map<String, Integer> SERVICO_CODIGOS = new HashMap<>();

    static {
        SERVICO_CODIGOS.put("cidadao", 1);
        SERVICO_CODIGOS.put("cidadaodetalhe", 2);
        SERVICO_CODIGOS.put("endereco", 3);
        SERVICO_CODIGOS.put("composicaofamiliar", 4);
        SERVICO_CODIGOS.put("resumo", 5);

        entidades.put("usuario", new UsuarioEntity());

        listModels.put("cidadao","cidadaos");

        pathPai.put("cidadao", 2);
        pathPai.put("usuario", 3);
    }
}
