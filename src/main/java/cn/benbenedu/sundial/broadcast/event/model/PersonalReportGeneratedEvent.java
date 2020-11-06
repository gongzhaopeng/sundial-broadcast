package cn.benbenedu.sundial.broadcast.event.model;

import lombok.Data;

import java.util.Map;

@Data
public class PersonalReportGeneratedEvent {

    private String id;      //准考证id
    private Map<String, String> urls;     //pdf路径
}
