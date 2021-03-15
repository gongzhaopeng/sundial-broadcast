package cn.benbenedu.sundial.broadcast.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GeneralLoginResp {

    @NotNull
    @JsonProperty("access_token")
    private String accessToken;
}
