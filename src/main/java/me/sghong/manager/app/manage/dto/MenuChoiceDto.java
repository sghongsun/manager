package me.sghong.manager.app.manage.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Alias("menuChoiceDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MenuChoiceDto {
    private String menucode;
    private String adminid;
    private int dispnum;
    private String menuname;
    private String menuurl;
}
