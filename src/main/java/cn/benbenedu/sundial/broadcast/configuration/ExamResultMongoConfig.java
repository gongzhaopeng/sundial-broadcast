package cn.benbenedu.sundial.broadcast.configuration;

import cn.benbenedu.sundial.broadcast.repository.examresult.ExamResultRepositoryLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackageClasses = ExamResultRepositoryLocator.class,
        mongoTemplateRef = "examResultMongoTemplate"
)
public class ExamResultMongoConfig {
}
