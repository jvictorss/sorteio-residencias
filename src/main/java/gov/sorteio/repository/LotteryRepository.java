package gov.sorteio.repository;

import gov.sorteio.entity.LotteryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface LotteryRepository extends BaseRepository<LotteryEntity, Long> {
}
