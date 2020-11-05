package cn.benbenedu.sundial.broadcast.repository.examstation;

import cn.benbenedu.sundial.broadcast.model.Exam;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ExamRepository extends MongoRepository<Exam, String> {

    Optional<Exam> findByTitle(String title);
}
