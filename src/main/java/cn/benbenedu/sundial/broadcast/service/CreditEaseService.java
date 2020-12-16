package cn.benbenedu.sundial.broadcast.service;

import cn.benbenedu.sundial.broadcast.configuration.CreditEaseConfiguration;
import cn.benbenedu.sundial.broadcast.configuration.RestTemplateConfiguration;
import cn.benbenedu.sundial.broadcast.event.model.PersonalReportGeneratedEvent;
import cn.benbenedu.sundial.broadcast.model.Account;
import cn.benbenedu.sundial.broadcast.model.AssessTokenTargetType;
import cn.benbenedu.sundial.broadcast.model.EchainAticket;
import cn.benbenedu.sundial.broadcast.model.ExamAticket;
import cn.benbenedu.sundial.broadcast.model.creditease.*;
import cn.benbenedu.sundial.broadcast.repository.accountcenter.AccountRepository;
import cn.benbenedu.sundial.broadcast.repository.examresult.ExamPersonalReportRepository;
import cn.benbenedu.sundial.broadcast.repository.examstation.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CreditEaseService implements InitializingBean {

    private static final String ORDER_CODE_FIELD_IN_EC_TICKET = "orderCode";

    private final RestTemplate restTemplate;

    private final CreditEaseConfiguration creditEaseConfiguration;

    private final AccountRepository accountRepository;
    private final ExamRepository examRepository;
    private final ExamChainRepository examChainRepository;
    private final AuxiliaryTokenRepository auxiliaryTokenRepository;
    private final EchainAticketRepository echainAticketRepository;
    private final AnswerSheetRepository answerSheetRepository;
    private final ExamPersonalReportRepository examPersonalReportRepository;

    private Map<CreditEaseProductCode, CreditEaseProduct> codeToProduct;
    private Map<String, CreditEaseProduct> examIdToProduct;

    @Getter
    private Account account;

    public CreditEaseService(
            @RestTemplateConfiguration.Pure final RestTemplate restTemplate,
            final CreditEaseConfiguration creditEaseConfiguration,
            final AccountRepository accountRepository,
            final ExamRepository examRepository,
            final ExamChainRepository examChainRepository,
            final AuxiliaryTokenRepository auxiliaryTokenRepository,
            final EchainAticketRepository echainAticketRepository,
            final AnswerSheetRepository answerSheetRepository,
            final ExamPersonalReportRepository examPersonalReportRepository) {

        this.restTemplate = restTemplate;
        this.creditEaseConfiguration = creditEaseConfiguration;
        this.accountRepository = accountRepository;
        this.examRepository = examRepository;
        this.examChainRepository = examChainRepository;
        this.auxiliaryTokenRepository = auxiliaryTokenRepository;
        this.echainAticketRepository = echainAticketRepository;
        this.answerSheetRepository = answerSheetRepository;
        this.examPersonalReportRepository = examPersonalReportRepository;
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
                    product.setAssessCodeTag(productConfig.getAssessCodeTag());
                    product.setNotifyPhase(productConfig.getNotifyPhase());

                    Optional.ofNullable(productConfig.getEchainTitle()).ifPresentOrElse(
                            echainTitle -> examChainRepository.findByTitle(echainTitle).ifPresentOrElse(
                                    examChain -> {
                                        product.setEchainId(examChain.getId());
                                        product.setEchainTitle(echainTitle);
                                        codeToProduct.put(code, product);
                                        examChain.getExams().forEach(examBrief ->
                                                examIdToProduct.put(examBrief.getId(), product));
                                    },
                                    () -> log.warn("Exam-Chain:{} does not exist.", echainTitle)
                            ),
                            () -> examRepository.findByTitle(productConfig.getExamTitle()).ifPresentOrElse(
                                    exam -> {
                                        product.setExamId(exam.getId());
                                        product.setExamTitle(exam.getTitle());
                                        codeToProduct.put(code, product);
                                        examIdToProduct.put(exam.getId(), product);
                                    },
                                    () -> log.warn("Exam:{} does not exist.", productConfig.getExamTitle())
                            )
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
            if (product.getCode() != CreditEaseProductCode.QSNHXSZ &&
                    product.getCode() != CreditEaseProductCode.SYSY) {
                return;
            }

            final var echainAticket =
                    echainAticketRepository.findById(examAticket.getEchainAticket()).orElseThrow();
            if (echainAticket.getAssessToken() == null) {
                return;
            }

            final var answerSheet =
                    answerSheetRepository.findById(examAticket.getId()).orElseThrow();

            final var notification = new CreditEaseAssessResultNotification();
            notification.setProductId(product.getCode());
            notification.setAssessTime(answerSheet.getEndTime().toString());
            notification.setAssessCode(echainAticket.getAssessToken());
            notification.setAssessResultUrl(
                    personalReportGeneratedEvent.getUrls().values().stream().findFirst().get());
            getOrderCode(echainAticket).ifPresent(notification::setOrderCode);

            final var timestamp = System.currentTimeMillis();
            final var params = new HashMap<String, Object>();
            params.put("productId", notification.getProductId());
            params.put("assessTime", notification.getAssessTime());
            params.put("assessCode", notification.getAssessCode());
            params.put("assessResultUrl", notification.getAssessResultUrl());
            params.put(ORDER_CODE_FIELD_IN_EC_TICKET, notification.getOrderCode());

            final var token = sign(params, timestamp);
            notification.setTimestamp(String.valueOf(timestamp));
            notification.setToken(token);

            log.info("CreditEase notification prepared: {}", notification);

            Optional.ofNullable(creditEaseConfiguration.getAssessResultNotifyUrl()).ifPresent(url -> {
                final var resp = restTemplate.postForEntity(
                        creditEaseConfiguration.getAssessResultNotifyUrl(),
                        notification,
                        String.class
                );
                log.info("CreditEase resp:{} to notification:{}", resp, notification);
            });
        });
    }

    public CreditEaseAssessResult getAssessResult(
            final CreditEaseAssessResultReq assessResultReq) {

        final var assessResult = new CreditEaseAssessResult();
        assessResult.setRespCode("0003");
        assessResult.setRespMessage("No result");

        final var params = new HashMap<String, Object>();
        params.put("productId", assessResultReq.getProductId());
        params.put("assessCode", assessResultReq.getAssessCode());
        if (!verifySign(params, Long.parseLong(assessResultReq.getTimestamp()), assessResultReq.getToken())) {
            assessResult.setRespCode("0001");
            assessResult.setRespMessage("Invalid token");
            return assessResult;
        }

        if (assessResultReq.getProductId() != CreditEaseProductCode.QSNHXSZ &&
                assessResultReq.getProductId() != CreditEaseProductCode.SYSY) {
            assessResult.setRespCode("0002");
            assessResult.setRespMessage("Invalid productId");
            return assessResult;
        }

        final var product = codeToProduct.get(assessResultReq.getProductId());
        final var assessCode = assessResultReq.getAssessCode();
        final var optEchainAticket =
                echainAticketRepository.findByAssessTokenAndEchainId(assessCode, product.getEchainId());
        if (optEchainAticket.isEmpty()) {
            return assessResult;
        }

        final var echainAticket = optEchainAticket.get();
        String examAticketId = null;
        if (echainAticket.getExamTickets() != null &&
                !echainAticket.getExamTickets().isEmpty()) {
            if (echainAticket.getExamTickets().get(0) != null &&
                    !echainAticket.getExamTickets().get(0).isEmpty()) {
                final var examAtickets = echainAticket.getExamTickets().get(0);
                examAticketId = examAtickets.get(examAtickets.size() - 1);
            } else if (echainAticket.getExamTickets().size() > 1 &&
                    echainAticket.getExamTickets().get(1) != null &&
                    !echainAticket.getExamTickets().get(1).isEmpty()) {
                final var examAtickets = echainAticket.getExamTickets().get(1);
                examAticketId = examAtickets.get(examAtickets.size() - 1);
            }

        }
        if (examAticketId == null) {
            return assessResult;
        }

        final var optExamPersonalReport = examPersonalReportRepository.findById(examAticketId);
        if (optExamPersonalReport.isEmpty()) {
            return assessResult;
        }
        final var examPersonalReport = optExamPersonalReport.get();
        String reportUrl = null;
        if (examPersonalReport.getFiles() != null &&
                !examPersonalReport.getFiles().isEmpty()) {
            reportUrl = examPersonalReport.getFiles().get(0).getUrl();
        }
        if (reportUrl == null) {
            return assessResult;
        }

        final var optAnswerSheet = answerSheetRepository.findById(examAticketId);
        if (optAnswerSheet.isEmpty()) {
            return assessResult;
        }

        final var answerSheet = optAnswerSheet.get();
        assessResult.setRespCode("0000");
        assessResult.setRespMessage("Success");
        assessResult.setProductId(assessResultReq.getProductId());
        assessResult.setAssessTime(answerSheet.getEndTime().toString());
        assessResult.setAssessCode(assessCode);
        assessResult.setAssessResultUrl(reportUrl);
        getOrderCode(echainAticket).ifPresent(assessResult::setOrderCode);

        return assessResult;
    }

    private Optional<String> getOrderCode(final EchainAticket echainAticket) {

        return Optional.ofNullable(echainAticket.getExtInfo()).map(
                extInfo -> extInfo.get(ORDER_CODE_FIELD_IN_EC_TICKET));
    }
}
