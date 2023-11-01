package me.sghong.manager.app.common.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Alias("myMenuChoiceDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MyMenuChoiceDto {
    private String menucode;
    private String adminid;
    @Setter
    private int dispnum;
    private String menuname;
    private String menuurl;
}