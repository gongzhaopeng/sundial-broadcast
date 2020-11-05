package cn.benbenedu.sundial.broadcast.repository.examstation;

import cn.benbenedu.sundial.broadcast.model.AuxiliaryToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuxiliaryTokenRepository extends MongoRepository<AuxiliaryToken, String> {

    Optional<AuxiliaryToken> findByCodeAndTargetTypeAndTargetId(
            String code, String targetType, String targetId);
}
