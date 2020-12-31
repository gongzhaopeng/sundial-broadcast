package cn.benbenedu.sundial.broadcast.service;

import cn.benbenedu.sundial.broadcast.model.Account;
import cn.benbenedu.sundial.broadcast.model.ExamPersonalReport;
import cn.benbenedu.sundial.broadcast.model.erdaoqu.ReportLookupReq;
import cn.benbenedu.sundial.broadcast.model.erdaoqu.ReportLookupResp;
import cn.benbenedu.sundial.broadcast.repository.examresult.ExamPersonalReportRepository;
import cn.benbenedu.sundial.broadcast.repository.examstation.EchainAticketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ErDaoQuReportService {

    final private MongoTemplate accountCenterMongoTemplate;
    final private EchainAticketRepository echainAticketRepository;
    final private ExamPersonalReportRepository examPersonalReportRepository;

    public ErDaoQuReportService(
            @Qualifier("accountCenterMongoTemplate") final MongoTemplate accountCenterMongoTemplate,
            final EchainAticketRepository echainAticketRepository,
            final ExamPersonalReportRepository examPersonalReportRepository) {

        this.accountCenterMongoTemplate = accountCenterMongoTemplate;
        this.echainAticketRepository = echainAticketRepository;
        this.examPersonalReportRepository = examPersonalReportRepository;
    }

    public ReportLookupResp lookupReport(final ReportLookupReq reportLookupReq) {

        final var account = lookupAccount(reportLookupReq);
        if (account == null) {
            return null;
        }

        final var optEchainAticket = echainAticketRepository.findByOwner(account.getId());
        if (optEchainAticket.isEmpty()) {
            return null;
        }

        final var reportLookupResp = new ReportLookupResp();

        final var echainAticket = optEchainAticket.get();
        final var examTicketsMat = echainAticket.getExamTickets();
        if (examTicketsMat != null) {

            if (!examTicketsMat.isEmpty()) {
                reportLookupResp.setReportA(lookupReportByExamAtickets(examTicketsMat.get(0)));
            }

            if (reportLookupReq.getGrade().contains("J")
                    && examTicketsMat.size() > 1) {
                reportLookupResp.setReportB(lookupReportByExamAtickets(examTicketsMat.get(1)));
            }
        }

        return reportLookupResp;
    }

    private Account lookupAccount(final ReportLookupReq reportLookupReq) {

        final var query = new Query();
        query.addCriteria(Criteria.where("name").is(reportLookupReq.getName())
                .and("address.county").is("二道区")
                .and("studyingStatus.grade").is(reportLookupReq.getGrade())
                .and("studyingStatus.school").is(reportLookupReq.getSchool())
                .and("studyingStatus.executiveClass").is(reportLookupReq.getExecutiveClass()));

        return accountCenterMongoTemplate.findOne(query, Account.class);
    }

    private ExamPersonalReport lookupReportByExamAtickets(final List<String> examAtickets) {

        if (examAtickets == null) {
            return null;
        }

        if (examAtickets.isEmpty()) {
            return null;
        }

        final String examAticket = examAtickets.size() > 1 ? examAtickets.get(1) : examAtickets.get(0);
        return examPersonalReportRepository.findById(examAticket).orElse(null);
    }
}
