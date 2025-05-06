package gov.sorteio.repository;

import gov.sorteio.entity.SorteadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SorteadoRepository extends JpaRepository<SorteadoEntity, Long> {
}