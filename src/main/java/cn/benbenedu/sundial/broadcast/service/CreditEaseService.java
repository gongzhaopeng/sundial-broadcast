package cn.benbenedu.sundial.broadcast.service;

import cn.benbenedu.sundial.broadcast.configuration.CreditEaseConfiguration;
import cn.benbenedu.sundial.broadcast.model.creditease.CreditEaseProduct;
import cn.benbenedu.sundial.broadcast.model.creditease.CreditEaseProductCode;
import cn.benbenedu.sundial.broadcast.repository.examstation.ExamChainRepository;
import cn.benbenedu.sundial.broadcast.repository.examstation.ExamRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CreditEaseService implements InitializingBean {

    private final CreditEaseConfiguration creditEaseConfiguration;

    private final ExamRepository examRepository;
    private final ExamChainRepository examChainRepository;

    private Map<CreditEaseProductCode, CreditEaseProduct> codeToProduct;
    private Map<String, CreditEaseProduct> examIdToProduct;

    public CreditEaseService(
            final CreditEaseConfiguration creditEaseConfiguration,
            final ExamRepository examRepository,
            final ExamChainRepository examChainRepository) {

        this.creditEaseConfiguration = creditEaseConfiguration;
        this.examRepository = examRepository;
        this.examChainRepository = examChainRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

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
}
