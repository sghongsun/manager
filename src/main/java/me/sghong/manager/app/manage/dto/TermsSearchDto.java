package me.sghong.manager.app.manage.dto;

import lombok.Getter;
import me.sghong.manager.app.common.dto.SearchDto;
import org.apache.ibatis.type.Alias;

@Alias("termsSearchDto")
@Getter
public class TermsSearchDto extends SearchDto {
    public TermsSearchDto() {
        setSearchtype("title");
    }
}
