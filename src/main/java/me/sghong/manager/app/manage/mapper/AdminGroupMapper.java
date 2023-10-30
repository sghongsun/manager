package me.sghong.manager.app.manage.mapper;

import me.sghong.manager.app.manage.dto.AdminGroupDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminGroupMapper {
    AdminGroupDto select_by_groupCode(String groupCode);
}
