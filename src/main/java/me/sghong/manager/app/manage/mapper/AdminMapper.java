package me.sghong.manager.app.manage.mapper;

import me.sghong.manager.app.manage.dto.AdminDto;
import me.sghong.manager.app.manage.dto.AdminLoginDto;
import me.sghong.manager.app.manage.dto.AdminSearchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    AdminDto select_by_adminId_for_group(String adminId);
    AdminDto select_by_adminId(String adminId);
    void update_for_login_fail(String adminId, String ip);
    void update_for_login_success(String adminId, String ip);
    void login_insert(String adminId, String ip);
    List<AdminDto> select_by_groupcode(int groupCode);
    int select_by_list_for_totalCount(AdminSearchDto adminSearchDto);
    List<AdminDto> select_by_list(AdminSearchDto adminSearchDto);
    int select_by_adminId_For_Count(String adminId);
    void insert_admin(AdminDto adminDto);
    void update_admin_for_groupcode(AdminDto adminDto);
    void update_adminpwd(AdminDto adminDto);
    void update_adminhp(AdminDto adminDto);
    void update_admininfo(AdminDto adminDto);
    void delete_admin(AdminDto adminDto);
    int select_by_admin_login_for_totalCount(AdminSearchDto adminSearchDto);
    List<AdminLoginDto> select_by_admin_login_list(AdminSearchDto adminSearchDto);
    List<AdminDto> select_by_list_for_group_search(String mCode2, String authType);

}
