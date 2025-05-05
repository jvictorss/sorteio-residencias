package gov.sorteio.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UsuarioDTO {
    private Long id;

    @NotBlank(message = "Campo necessário")
    private String nome;

    @NotBlank(message = "Campo necessário")
    private String email;

    @NotBlank(message = "Campo necessário")
    private String tipo;

}
