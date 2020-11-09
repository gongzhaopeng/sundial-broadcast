package cn.benbenedu.sundial.broadcast.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("answerSheet")
public class AnswerSheet {
    @Id
    private String aticket;

    private Long startTime;

    private Long endTime;
}
