package gov.sorteio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoResponse {
    private String sorteio;
    private String horaSorteio;
    private String nomeUsuario;
    private List<ParticipanteResponse> participantes;
}
