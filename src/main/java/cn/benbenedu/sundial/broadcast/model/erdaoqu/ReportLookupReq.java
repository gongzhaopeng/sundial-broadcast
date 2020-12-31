package cn.benbenedu.sundial.broadcast.model.erdaoqu;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReportLookupReq {

    @NotNull
    private String school;

    @NotNull
    private String grade;

    @NotNull
    private String executiveClass;

    @NotNull
    private String name;
}
