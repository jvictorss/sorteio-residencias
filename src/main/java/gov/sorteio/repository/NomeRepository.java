package gov.sorteio.repository;

import gov.sorteio.entity.Nome;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface NomeRepository extends CrudRepository<Nome, UUID> {
}
