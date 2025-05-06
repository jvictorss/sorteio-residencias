package gov.sorteio.repository;

import gov.sorteio.entity.SorteadoEntity;
import gov.sorteio.entity.Sorteio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SorteioRepository extends JpaRepository<Sorteio, Long> {
}