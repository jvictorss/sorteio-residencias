package gov.sorteio.repository;

import gov.sorteio.entity.UsuarioEntity;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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
