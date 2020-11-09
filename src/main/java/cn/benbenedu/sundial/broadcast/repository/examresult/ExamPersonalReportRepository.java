package cn.benbenedu.sundial.broadcast.repository.examresult;

import cn.benbenedu.sundial.broadcast.model.ExamPersonalReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExamPersonalReportRepository extends MongoRepository<ExamPersonalReport, String> {
}
