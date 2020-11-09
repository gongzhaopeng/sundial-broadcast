package cn.benbenedu.sundial.broadcast.repository.examstation;

import cn.benbenedu.sundial.broadcast.model.EchainAticket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EchainAticketRepository extends MongoRepository<EchainAticket, String> {
}
