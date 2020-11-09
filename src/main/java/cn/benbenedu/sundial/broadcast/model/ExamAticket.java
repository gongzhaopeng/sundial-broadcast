package cn.benbenedu.sundial.broadcast.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("examAticket")
public class ExamAticket {

    @Id
    private String id;

    private ExamBrief exam;

    private String assessToken;

    private String echainAticket;
}
