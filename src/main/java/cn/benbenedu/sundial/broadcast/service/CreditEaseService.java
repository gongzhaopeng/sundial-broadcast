package cn.benbenedu.sundial.broadcast.service;

import cn.benbenedu.sundial.broadcast.configuration.CreditEaseConfiguration;
import cn.benbenedu.sundial.broadcast.event.model.PersonalReportGeneratedEvent;
import cn.benbenedu.sundial.broadcast.model.Account;
import cn.benbenedu.sundial.broadcast.model.AssessTokenTargetType;
import cn.benbenedu.sundial.broadcast.model.ExamAticket;
import cn.benbenedu.sundial.broadcast.model.creditease.CreditEaseProduct;
import cn.benbenedu.sundial.broadcast.model.creditease.CreditEaseProductCode;
import cn.benbenedu.sundial.broadcast.repository.accountcenter.AccountRepository;
import cn.benbenedu.sundial.broadcast.repository.examstation.AuxiliaryTokenRepository;
import cn.benbenedu.sundial.broadcast.repository.examstation.ExamChainRepository;
import cn.benbenedu.sundial.broadcast.repository.examstation.ExamRepository;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CreditEaseService implements InitializingBean {

    private final CreditEaseConfiguration creditEaseConfiguration;

    private final AccountRepository accountRepository;
    private final ExamRepository examRepository;
    private final ExamChainRepository examChainRepository;
    private final AuxiliaryTokenRepository auxiliaryTokenRepository;

    private Map<CreditEaseProductCode, CreditEaseProduct> codeToProduct;
    private Map<String, CreditEaseProduct> examIdToProduct;

    @Getter
    private Account account;

    public CreditEaseService(
            final CreditEaseConfiguration creditEaseConfiguration,
            final AccountRepository accountRepository,
            final ExamRepository examRepository,
            final ExamChainRepository examChainRepository,
            final AuxiliaryTokenRepository auxiliaryTokenRepository) {

        this.creditEaseConfiguration = creditEaseConfiguration;
        this.accountRepository = accountRepository;
        this.examRepository = examRepository;
        this.examChainRepository = examChainRepository;
        this.auxiliaryTokenRepository = auxiliaryTokenRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        account = accountRepository.findByEmail(creditEaseConfiguration.getAccountName()).orElseThrow();

        codeToProduct = new HashMap<>();
        examIdToProduct = new HashMap<>();

        this.creditEaseConfiguration.getProducts().stream().collect(Collectors.toMap(
                CreditEaseConfiguration.Product::getCode, Function.identity()))
                .forEach((code, productConfig) -> {

                    final var product = new CreditEaseProduct();
                    product.setCode(code);

                    Optional.ofNullable(productConfig.getEchainTitle()).ifPresentOrElse(
                            echainTitle -> {
                                final var examChain =
                                        examChainRepository.findByTitle(echainTitle).orElseThrow();
                                product.setEchainId(examChain.getId());
                                product.setEchainTitle(echainTitle);
                                codeToProduct.put(code, product);
                                examChain.getExams().forEach(examBrief ->
                                        examIdToProduct.put(examBrief.getId(), product));
                            },
                            () -> {
                                final var exam =
                                        examRepository.findByTitle(productConfig.getExamTitle()).orElseThrow();
                                product.setExamId(exam.getId());
                                product.setExamTitle(exam.getTitle());
                                codeToProduct.put(code, product);
                                examIdToProduct.put(exam.getId(), product);
                            }
                    );
                });
    }

    public Boolean verifyAssessCode(
            final CreditEaseProductCode productId,
            final String assessCode) {

        final var product = codeToProduct.get(productId);
        if (product.getEchainId() != null) {
            return auxiliaryTokenRepository.existsByCodeAndTargetTypeAndTargetId(
                    assessCode, AssessTokenTargetType.ExamChain.toString(), product.getEchainId());
        } else {
            return auxiliaryTokenRepository.existsByCodeAndTargetTypeAndTargetId(
                    assessCode, AssessTokenTargetType.Exam.toString(), product.getExamId());
        }
    }

    public boolean verifySign(final Map<String, Object> params,
                              final long timestamp,
                              final String sign) {

        final var actualSign = sign(params, timestamp);

        return actualSign.equals(sign);
    }

    public String sign(final Map<String, Object> params,
                       final long timestamp) {

        SortedMap<String, Object> sortedMap = new TreeMap<>(params);
        Set<Map.Entry<String, Object>> items = sortedMap.entrySet();
        StringBuilder sb = new StringBuilder();
        sb.append(this.creditEaseConfiguration.getSignKey()).append(",").append(timestamp);
        for (Map.Entry<String, Object> item : items) {
            //跳过为null的参数
            if (null != item.getValue()) {
                sb.append(",").append(item.getValue());
            }
        }
        String ret = DigestUtils.sha256Hex(sb.toString()).substring(40);
        return ret;
    }

    public void notifyExamReportGenerated(
            final PersonalReportGeneratedEvent personalReportGeneratedEvent,
            final ExamAticket examAticket) {

        final var examId = examAticket.getExam().getId();
        Optional.ofNullable(examIdToProduct.get(examId)).ifPresent(product -> {
            if (product.getCode() == CreditEaseProductCode.QSNHXSZ) {

                // TODO
            } else if (product.getCode() == CreditEaseProductCode.XueKeXQ) {

                // TODO
            }
        });
    }
}
