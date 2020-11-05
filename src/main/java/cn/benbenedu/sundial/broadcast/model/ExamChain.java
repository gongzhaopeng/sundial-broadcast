package cn.benbenedu.sundial.broadcast.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("examChain")
@Data
public class ExamChain {

    @Id
    private String id;

    private String title;

    private List<ExamBrief> exams;
}
