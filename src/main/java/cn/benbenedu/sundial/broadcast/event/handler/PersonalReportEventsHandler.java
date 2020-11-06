package cn.benbenedu.sundial.broadcast.event.handler;

import cn.benbenedu.sundial.broadcast.event.PersonalReportChannels;
import cn.benbenedu.sundial.broadcast.event.model.PersonalReportGeneratedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(PersonalReportChannels.class)
@Slf4j
public class PersonalReportEventsHandler {

    /**
     * 接收准考证Id和pdf路径
     *
     * @param personalReportGeneratedEvent
     */
    @StreamListener("inboundPersonalReportGenerated")
    public void personalReportGenerated(
            PersonalReportGeneratedEvent personalReportGeneratedEvent) {

        log.info(
                "Receive a PersonalReport-Generated event: {}",
                personalReportGeneratedEvent);
        // TODO
    }
}
