package cn.benbenedu.sundial.broadcast.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("错误响应")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class ErrorResponse {

    @ApiModelProperty("错误码")
    @NotNull
    private final ErrorResponseCode code;

    @ApiModelProperty("错误详情")
    private String detail;
}
