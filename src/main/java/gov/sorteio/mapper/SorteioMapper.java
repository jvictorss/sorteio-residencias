package gov.sorteio.mapper;

import gov.sorteio.dto.ParticipanteResponse;
import gov.sorteio.dto.ResultadoResponse;
import gov.sorteio.entity.SorteadoEntity;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SorteioMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    public static ResultadoResponse toResponse(List<SorteadoEntity> sorteados) {
        if (sorteados.isEmpty()) {
            throw new IllegalArgumentException("A lista de sorteados está vazia.");
        }

        SorteadoEntity primeiroSorteado = sorteados.get(0);

        ResultadoResponse response = new ResultadoResponse();
        response.setSorteio(primeiroSorteado.getSorteio());
        response.setHoraSorteio(primeiroSorteado.getHoraSorteio().format(FORMATTER));
        response.setNomeUsuario(primeiroSorteado.getNomeUsuario());
        response.setParticipantes(sorteados.stream()
                .map(sorteado -> new ParticipanteResponse(sorteado.getNome(), sorteado.getCpf()))
                .collect(Collectors.toList()));

        return response;
    }
}