package cn.benbenedu.sundial.broadcast.service;

import cn.benbenedu.sundial.broadcast.configuration.WenXuanConfiguration;
import cn.benbenedu.sundial.broadcast.model.Account;
import cn.benbenedu.sundial.broadcast.repository.accountcenter.AccountRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WenXuanService implements InitializingBean {

    private final WenXuanConfiguration wenXuanConfiguration;

    private final AccountRepository accountRepository;

    @Getter
    private Account account;

    public WenXuanService(
            final WenXuanConfiguration wenXuanConfiguration,
            final AccountRepository accountRepository) {

        this.wenXuanConfiguration = wenXuanConfiguration;
        this.accountRepository = accountRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        account = accountRepository.findByEmail(wenXuanConfiguration.getAccountName()).orElseThrow();
    }
}
