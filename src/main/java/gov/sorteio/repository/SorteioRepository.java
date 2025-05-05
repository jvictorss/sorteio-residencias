package gov.sorteio.repository;

import gov.sorteio.entity.Sorteio;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SorteioRepository extends CrudRepository<Sorteio, UUID> {
}
