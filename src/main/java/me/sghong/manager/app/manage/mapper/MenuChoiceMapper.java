package me.sghong.manager.app.manage.mapper;

import me.sghong.manager.app.manage.dto.MenuChoiceDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuChoiceMapper {
    List<MenuChoiceDto> select_by_adminId(String adminId);
    void delete_by_menuCode(String menuCode);
}
