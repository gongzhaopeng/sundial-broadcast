//package cn.benbenedu.sundial.broadcast.event.handler;
//
//import cn.benbenedu.sundial.broadcast.event.PersonalReportChannels;
//import cn.benbenedu.sundial.broadcast.event.model.PersonalReportGeneratedEvent;
//import cn.benbenedu.sundial.broadcast.repository.examstation.ExamAticketRepository;
//import cn.benbenedu.sundial.broadcast.service.CreditEaseService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.annotation.StreamListener;
//
//@EnableBinding(PersonalReportChannels.class)
//@Slf4j
//public class PersonalReportEventsHandler {
//
//    private final ExamAticketRepository examAticketRepository;
//
//    private final CreditEaseService creditEaseService;
//
//    public PersonalReportEventsHandler(
//            final ExamAticketRepository examAticketRepository,
//            final CreditEaseService creditEaseService) {
//
//        this.examAticketRepository = examAticketRepository;
//
//        this.creditEaseService = creditEaseService;
//    }
//
//    /**
//     * 接收准考证Id和pdf路径
//     *
//     * @param personalReportGeneratedEvent
//     */
//    @StreamListener("inboundPersonalReportGenerated")
//    public void personalReportGenerated(
//            PersonalReportGeneratedEvent personalReportGeneratedEvent) {
//
//        log.info(
//                "Receive a PersonalReport-Generated event: {}",
//                personalReportGeneratedEvent);
//
//        try {
//
//            final var examAticket =
//                    examAticketRepository.findById(personalReportGeneratedEvent.getId()).orElseThrow();
//
//            creditEaseService.notifyExamReportGenerated(personalReportGeneratedEvent, examAticket);
//        } catch (Exception e) {
//            log.error(String.format(
//                    "Unpredicted error occurs while processing PersonalReport-Generated event: %s",
//                    personalReportGeneratedEvent.toString()), e);
//        }
//    }
//}
