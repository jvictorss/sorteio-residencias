package gov.sorteio.dto;
import java.time.LocalDateTime;

public class SorteioDto {
    private String nomeSorteado;
    private LocalDateTime dataHora;

    public SorteioDto(String nomeSorteado, LocalDateTime dataHora) {
        this.nomeSorteado = nomeSorteado;
        this.dataHora = dataHora;
    }

    public String getNomeSorteado() {
        return nomeSorteado;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}