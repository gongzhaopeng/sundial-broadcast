package cn.benbenedu.sundial.broadcast.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("exam")
@Data
public class Exam {

    @Id
    private String id;

    private String title;
}
