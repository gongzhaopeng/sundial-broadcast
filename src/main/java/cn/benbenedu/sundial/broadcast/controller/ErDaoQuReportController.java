package cn.benbenedu.sundial.broadcast.controller;

import cn.benbenedu.sundial.broadcast.model.erdaoqu.ReportLookupReq;
import cn.benbenedu.sundial.broadcast.model.erdaoqu.ReportLookupResp;
import cn.benbenedu.sundial.broadcast.service.ErDaoQuReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/genesis/erdaoqu/report")
@Slf4j
public class ErDaoQuReportController {

    final private ErDaoQuReportService erDaoQuReportService;

    public ErDaoQuReportController(
            final ErDaoQuReportService erDaoQuReportService) {

        this.erDaoQuReportService = erDaoQuReportService;
    }

    @PostMapping("/lookup")
    public ReportLookupResp lookupReport(
            @RequestBody @Valid ReportLookupReq reportLookupReq) {

        return erDaoQuReportService.lookupReport(reportLookupReq);
    }
}
