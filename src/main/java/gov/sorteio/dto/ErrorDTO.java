package gov.sorteio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorDTO {
    private String codigo;
    private String titulo;
    private String mensagem;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String exception;
}
