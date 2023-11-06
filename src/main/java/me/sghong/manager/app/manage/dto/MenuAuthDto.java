package me.sghong.manager.app.manage.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Alias("menuAuthDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MenuAuthDto {
    private String menucode1;
    private String menuname1;
    private String menucode2;
    private String menuname2;
    private int readgroupcount;
    private int readusercount;
    private int writegroupcount;
    private int writeusercount;
}
