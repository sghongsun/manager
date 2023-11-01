package me.sghong.manager.app.manage.service;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.sghong.manager.app.manage.dto.AdminDto;
import me.sghong.manager.app.manage.mapper.AdminMapper;
import me.sghong.manager.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final AdminMapper adminMapper;

    public AdminDto AdminInfoForLogin(String adminId) {
        return adminMapper.select_by_adminId(adminId);
    }

    public void LoginForPwdErrUpdate(String adminId) {
        adminMapper.update_for_login_fail(adminId, CommonUtil.getRemoteIP());
    }

    @Transactional
    public void AdminLoginSuccess(String adminId) {
        adminMapper.update_for_login_success(adminId, CommonUtil.getRemoteIP());
        adminMapper.login_insert(adminId, CommonUtil.getRemoteIP());
    }

    public List<AdminDto> getAdminListByGroupCode(int groupCode) {
        return adminMapper.select_by_groupcode(groupCode);
    }
}
