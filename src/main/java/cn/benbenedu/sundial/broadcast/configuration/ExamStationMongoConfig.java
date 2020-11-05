package cn.benbenedu.sundial.broadcast.configuration;

import cn.benbenedu.sundial.broadcast.repository.examstation.ExamStationRepositoryLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackageClasses = ExamStationRepositoryLocator.class,
        mongoTemplateRef = "examStationMongoTemplate"
)
public class ExamStationMongoConfig {
}
