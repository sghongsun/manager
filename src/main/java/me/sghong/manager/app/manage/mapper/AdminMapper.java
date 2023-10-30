package me.sghong.manager.app.manage.mapper;

import me.sghong.manager.app.manage.dto.AdminDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    AdminDto select_by_adminId(String adminId);
    void update_for_login_fail(String adminId, String ip);
    void update_for_login_success(String adminId, String ip);
    void login_insert(String adminId, String ip);
}
