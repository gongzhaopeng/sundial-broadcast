package cn.benbenedu.sundial.broadcast.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document("echainAticket")
public class EchainAticket {

    @Id
    private String id;

    private EchainBrief echain;

    private String assessToken;

    private List<List<String>> examTickets;

    private Map<String, String> extInfo;
}
