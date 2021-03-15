package cn.benbenedu.sundial.broadcast.service;

import cn.benbenedu.sundial.broadcast.configuration.BaidingSchoolConfiguration;
import cn.benbenedu.sundial.broadcast.model.Account;
import cn.benbenedu.sundial.broadcast.repository.accountcenter.AccountRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaidingSchoolService implements InitializingBean {

    private final BaidingSchoolConfiguration baidingSchoolConfiguration;

    private final AccountRepository accountRepository;

    @Getter
    private Account account;

    public BaidingSchoolService(
            final BaidingSchoolConfiguration baidingSchoolConfiguration,
            final AccountRepository accountRepository) {

        this.baidingSchoolConfiguration = baidingSchoolConfiguration;
        this.accountRepository = accountRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        account = accountRepository.findByEmail(baidingSchoolConfiguration.getAccountName()).orElseThrow();
    }
}
