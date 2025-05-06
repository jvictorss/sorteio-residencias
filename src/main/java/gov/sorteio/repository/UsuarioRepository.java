package gov.sorteio.repository;

import gov.sorteio.entity.UsuarioEntity;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends BaseRepository<UsuarioEntity, Long> {
//    @Transactional
//    @Modifying
//    @Query("UPDATE UsuarioEntity set ativo=?2 WHERE id=?1")
//    void update(Long id, Boolean acao);

    Optional<UsuarioEntity> findByEmail(String email);

    DataTablesOutput<UsuarioEntity> findAll(DataTablesInput input);
}
