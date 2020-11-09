package cn.benbenedu.sundial.broadcast.repository.examstation;

import cn.benbenedu.sundial.broadcast.model.ExamAticket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExamAticketRepository extends MongoRepository<ExamAticket, String> {
}
