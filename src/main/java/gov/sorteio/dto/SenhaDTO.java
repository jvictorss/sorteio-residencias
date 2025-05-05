package gov.sorteio.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SenhaDTO {
    @NotBlank
    private String atual;

    @NotBlank
    private String nova;
}
