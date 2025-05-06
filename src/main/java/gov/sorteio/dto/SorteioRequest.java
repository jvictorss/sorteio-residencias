package gov.sorteio.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class SorteioRequest {
    @NotBlank(message = "O título do sorteio não pode ser vazio")
    private String sorteio;

    @NotNull(message = "A quantidade de sorteados deve ser informada")
    private Integer quantidadeSorteio;

    @NotNull(message = "A lista de participantes não pode estar vazia")
    private String participantes;
}