package cn.benbenedu.sundial.broadcast.repository.examstation;

import cn.benbenedu.sundial.broadcast.model.ExamChain;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ExamChainRepository extends MongoRepository<ExamChain, String> {

    Optional<ExamChain> findByTitle(String title);
}
