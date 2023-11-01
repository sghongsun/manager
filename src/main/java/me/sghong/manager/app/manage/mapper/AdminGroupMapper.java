package me.sghong.manager.app.manage.mapper;

import me.sghong.manager.app.manage.dto.AdminGroupDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminGroupMapper {
    AdminGroupDto select_by_groupCode(int groupCode);
    List<AdminGroupDto> select_by_list();
    int select_by_menu_include_for_count();
    int select_by_groupcode_max();
    void insert_admingroup(AdminGroupDto adminGroupDto);
    void update_admingroup(AdminGroupDto adminGroupDto);
    void delete_admingroup(int groupCode);

}
