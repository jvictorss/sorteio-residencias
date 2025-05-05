package gov.sorteio.exceptions;

import gov.sorteio.dto.ErrorDTO;
import gov.sorteio.enums.ErrorCodesEnum;
import org.springframework.stereotype.Component;

import static gov.sorteio.constants.Constants.MSG_GENERIC;
import static gov.sorteio.constants.Constants.TITLE_GENERIC;

@Component
public class MensagemError {
    public static ErrorDTO gerarErrorDTO(String cod, String title, String msg){
        return ErrorDTO.builder()
                .codigo(cod)
                .titulo(title)
                .mensagem(msg)
                .build();
    }

    public static ErrorDTO gerarExceptionGenerica(Throwable e){
        return ErrorDTO.builder()
                .codigo(ErrorCodesEnum.COD_GENERICO.getCodigo())
                .titulo(TITLE_GENERIC)
                .mensagem(MSG_GENERIC)
                .exception(e.getClass().getSimpleName())
                .build();
    }
}
