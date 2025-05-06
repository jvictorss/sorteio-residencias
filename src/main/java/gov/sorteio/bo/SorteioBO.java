package gov.sorteio.bo;

import gov.sorteio.dto.ResultadoResponse;
import gov.sorteio.dto.SorteioFormDto;
import gov.sorteio.entity.ParticipanteEntity;
import gov.sorteio.entity.SorteadoEntity;
import gov.sorteio.entity.Sorteio;
import gov.sorteio.entity.UsuarioEntity;
import gov.sorteio.mapper.SorteioMapper;
import gov.sorteio.repository.ParticipanteRepository;
import gov.sorteio.repository.SorteadoRepository;
import gov.sorteio.repository.SorteioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SorteioBO {
    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private SorteioRepository sorteioRepository;

    @Autowired
    private SorteadoRepository sorteadoRepository;


    @Transactional
    public ResultadoResponse realizarSorteio(SorteioFormDto sorteioFormDto) {
        var data = LocalDateTime.now();
        var nomeSorteio = sorteioFormDto.getSorteio();
        var quantidadeSorteados = sorteioFormDto.getQuantidadeSorteio();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsuarioEntity user = (UsuarioEntity) auth.getPrincipal();

        saveSorteio(sorteioFormDto, data, nomeSorteio, user, quantidadeSorteados);

        var participantes = saveParticipantes(sorteioFormDto, user);

        List<ParticipanteEntity> sorteados = sortearParticipantes(participantes, sorteioFormDto.getQuantidadeSorteio());

        List<SorteadoEntity> sorteadosEntities = new ArrayList<>();
        sorteados.forEach(sorteado -> {
            SorteadoEntity sorteadoEntity = new SorteadoEntity();
            sorteadoEntity.setNome(sorteado.getNome());
            sorteadoEntity.setCpf(sorteado.getCpf());
            sorteadoEntity.setSorteio(sorteado.getSorteio());
            sorteadoEntity.setHoraSorteio(data);
            sorteadoEntity.setUsuarioId(user.getId());
            sorteadoEntity.setNomeUsuario(user.getNome());
            sorteadoRepository.save(sorteadoEntity);
            sorteadosEntities.add(sorteadoEntity);
        });

        return SorteioMapper.toResponse(sorteadosEntities);
    }

    @Transactional
    public List<ParticipanteEntity> saveParticipantes(SorteioFormDto sorteioFormDto, UsuarioEntity user) {
        List<ParticipanteEntity> participantesSalvos = new ArrayList<>();
        sorteioFormDto.getParticipantes().forEach(participante -> {
            ParticipanteEntity participanteEntity = new ParticipanteEntity();
            participanteEntity.setNome(participante.getNome());
            participanteEntity.setCpf(participante.getCpf());
            participanteEntity.setSorteio(sorteioFormDto.getSorteio());
            participanteEntity.setHoraCriacao(LocalDateTime.now());
            participanteEntity.setNomeUsuario(user.getNome());
            participanteEntity.setUsuarioId(user.getId());
            ParticipanteEntity salvo = participanteRepository.save(participanteEntity);
            participantesSalvos.add(salvo);
        });
        return participantesSalvos;
    }

    @Transactional
    public void saveSorteio(SorteioFormDto sorteioFormDto, LocalDateTime data, String nomeSorteio, UsuarioEntity user, Integer quantidadeSorteados) {
        var sorteio = new Sorteio();
        sorteio.setDataSorteio(data);
        sorteio.setNomeSorteio(nomeSorteio);
        sorteio.setUsuarioId(user.getId());
        sorteio.setNomeUsuario(user.getNome());
        sorteio.setQuantidadeParticipantes(sorteioFormDto.getParticipantes().size());
        sorteio.setQuantidadeSorteada(quantidadeSorteados);
        sorteioRepository.save(sorteio);
    }

    private List<ParticipanteEntity> sortearParticipantes(List<ParticipanteEntity> participantes, int quantidade) {
        Random random = new Random();
        List<ParticipanteEntity> sorteados = new ArrayList<>();
        while (sorteados.size() < quantidade && !participantes.isEmpty()) {
            int indice = random.nextInt(participantes.size());
            ParticipanteEntity sorteado = participantes.remove(indice);
            sorteados.add(sorteado);
        }
        return sorteados;
    }
}
