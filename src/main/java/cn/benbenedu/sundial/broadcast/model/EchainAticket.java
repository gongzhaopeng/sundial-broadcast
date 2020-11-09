package cn.benbenedu.sundial.broadcast.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("echainAticket")
public class EchainAticket {

    @Id
    private String id;

    private EchainBrief echain;

    private String assessToken;
}
