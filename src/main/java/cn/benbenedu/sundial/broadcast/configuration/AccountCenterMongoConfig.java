package cn.benbenedu.sundial.broadcast.configuration;

import cn.benbenedu.sundial.broadcast.repository.accountcenter.AccountCenterRepositoryLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackageClasses = AccountCenterRepositoryLocator.class,
        mongoTemplateRef = "accountCenterMongoTemplate"
)
public class AccountCenterMongoConfig {
}
