package gov.sorteio.enums;

import lombok.Getter;

@Getter
public enum ErrorCodesEnum {
    COD_GENERICO("GFT-0000"),
    COD_PERSISTIR("GFT-0001"),
    COD_USUARIO("GFT-0002"),
    COD_EMAIL("GFT-0003"),
    COD_CONSULTAR("GFT-0004");

    public final String codigo;

    private static final String TITULO = "Error ao efetuar operação";
    private static final String MENSAGEM = "Desculpe! Não foi possível efetuar a operação. Tente novamente em instantes.";

    ErrorCodesEnum(String codigo) {
        this.codigo = codigo;
    }
}
