package gov.sorteio.bo;

import gov.sorteio.dto.ResultadoResponse;
import gov.sorteio.dto.SorteioFormDto;
import gov.sorteio.entity.LotteryEntity;
import gov.sorteio.entity.ParticipanteEntity;
import gov.sorteio.entity.SorteadoEntity;
import gov.sorteio.entity.Sorteio;
import gov.sorteio.entity.UsuarioEntity;
import gov.sorteio.mapper.SorteioMapper;
import gov.sorteio.repository.BaseRepository;
import gov.sorteio.repository.LotteryRepository;
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
import java.util.UUID;

@Service
public class LotteryBO extends BaseBO<LotteryEntity, Long>{


    public LotteryBO(BaseRepository<LotteryEntity, Long> baseRepository) {
        super(baseRepository);
    }

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private LotteryRepository lotteryRepository;

    @Autowired
    private SorteadoRepository sorteadoRepository;


    @Transactional
    public ResultadoResponse realizarSorteio(SorteioFormDto sorteioFormDto) {
        var data = LocalDateTime.now();
        var nomeSorteio = sorteioFormDto.getSorteio();
        var quantidadeSorteados = sorteioFormDto.getQuantidadeSorteio();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsuarioEntity user = (UsuarioEntity) auth.getPrincipal();

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

        List<SorteadoEntity> resultado = saveSorteio(sorteioFormDto, data, nomeSorteio, user, quantidadeSorteados, sorteadosEntities);

        return SorteioMapper.toResponse(resultado);
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
   public List<SorteadoEntity> saveSorteio(SorteioFormDto sorteioFormDto, LocalDateTime data, String nomeSorteio, UsuarioEntity user, Integer quantidadeSorteados, List<SorteadoEntity> sorteados) {
       var sorteio = new LotteryEntity();
       var resultado = sorteados;
       sorteio.setDataSorteio(data);

       StringBuilder sorteadosStr = new StringBuilder();
       sorteados.forEach(sorteado -> {
           if (sorteadosStr.length() > 0) {
               sorteadosStr.append(", ");
           }
           sorteadosStr.append(sorteado.getNome()).append(" - ").append(sorteado.getCpf());
       });
       sorteio.setSorteados(sorteadosStr.toString());

       sorteio.setNomeSorteio(nomeSorteio);
       sorteio.setUuid(UUID.randomUUID());
       sorteio.setIdUser(user.getId());
       sorteio.setIdUserAtualizou(user.getId());
       sorteio.setNomeUsuario(user.getNome());
       sorteio.setQuantidadeParticipantes(sorteioFormDto.getParticipantes().size());
       sorteio.setQuantidadeSorteada(quantidadeSorteados);
       sorteio.setAtivo(true);
       sorteio.setCriado(data);
       sorteio.setAtualizado(data);
       lotteryRepository.save(sorteio);

       return resultado;
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
