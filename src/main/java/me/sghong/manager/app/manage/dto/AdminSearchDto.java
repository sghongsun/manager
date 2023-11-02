package me.sghong.manager.app.manage.dto;

import lombok.Getter;
import me.sghong.manager.app.common.dto.SearchDto;
import org.apache.ibatis.type.Alias;

@Alias("adminSearchDto")
@Getter
public class AdminSearchDto extends SearchDto {
    private String groupcode;
    public AdminSearchDto(String groupcode) {
        this.groupcode = groupcode;
        setSearchtype("adminname");
    }
}
