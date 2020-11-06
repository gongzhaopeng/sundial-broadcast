package cn.benbenedu.sundial.broadcast.configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
@EnableConfigurationProperties(MultipleMongoProperties.class)
@RequiredArgsConstructor
public class MongoConfiguration {

    private final MultipleMongoProperties mongoProperties;

    @Primary
    @Bean(name = "examStationMongoTemplate")
    public MongoTemplate examStationMongoTemplate() throws Exception {

        return new MongoTemplate(examStationFactory(this.mongoProperties.getExamStation()));
    }

    @Bean(name = "examResultMongoTemplate")
    public MongoTemplate examResultMongoTemplate() throws Exception {

        return new MongoTemplate(examResultFactory(this.mongoProperties.getExamResult()));
    }

    @Bean
    @Primary
    public MongoDbFactory examStationFactory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(new MongoClientURI(mongo.getUri())), mongo.getDatabase());
    }

    @Bean
    public MongoDbFactory examResultFactory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(new MongoClientURI(mongo.getUri())), mongo.getDatabase());
    }
}
