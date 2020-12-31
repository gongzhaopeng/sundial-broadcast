package cn.benbenedu.sundial.broadcast.repository.examstation;

import cn.benbenedu.sundial.broadcast.model.EchainAticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EchainAticketRepository extends MongoRepository<EchainAticket, String> {

    Optional<EchainAticket> findByAssessTokenAndEchainId(String assessToken, String echainId);

    Optional<EchainAticket> findByOwner(String owner);
}
