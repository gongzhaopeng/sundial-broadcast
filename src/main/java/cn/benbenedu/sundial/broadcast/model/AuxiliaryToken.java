package cn.benbenedu.sundial.broadcast.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("auxiliarytoken")
@Data
public class AuxiliaryToken {

    private String code;
    private AssessTokenTargetType targetType;
    private String targetId;
    private String tokenId;
}
