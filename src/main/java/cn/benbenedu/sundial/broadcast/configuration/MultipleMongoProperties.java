package cn.benbenedu.sundial.broadcast.configuration;

import lombok.Data;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mongodb")
public class MultipleMongoProperties {

    private MongoProperties examStation;
    private MongoProperties examResult;
    private MongoProperties accountCenter;
}
