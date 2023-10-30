package me.sghong.manager.app.manage.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Alias("menuDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MenuDto {
    private String menucode;
    private String menupcode;
    private String menuname;
    @Setter
    private String menuurl;
    private int menudispnum;
    private String menuuseflag;
    private String menuchoice;
}
