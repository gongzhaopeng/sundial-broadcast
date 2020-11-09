package cn.benbenedu.sundial.broadcast.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("examPersonalReport")
public class ExamPersonalReport {

    @Id
    private String aticket;

    private List<ReportFile> files;
}
