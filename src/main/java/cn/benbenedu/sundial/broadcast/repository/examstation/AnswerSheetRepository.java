package cn.benbenedu.sundial.broadcast.repository.examstation;

import cn.benbenedu.sundial.broadcast.model.AnswerSheet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnswerSheetRepository extends MongoRepository<AnswerSheet, String> {
}
