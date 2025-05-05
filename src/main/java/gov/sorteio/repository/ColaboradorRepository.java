package gov.sorteio.repository;

import gov.sorteio.entity.ColaboradorEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ColaboradorRepository extends CrudRepository<ColaboradorEntity, UUID> {
    Optional<ColaboradorEntity> findByEmail(String email);
}
