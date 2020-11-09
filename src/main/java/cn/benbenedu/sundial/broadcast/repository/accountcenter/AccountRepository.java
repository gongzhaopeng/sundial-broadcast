package cn.benbenedu.sundial.broadcast.repository.accountcenter;

import cn.benbenedu.sundial.broadcast.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {

    Optional<Account> findByEmail(String email);
}
