package me.sghong.manager.app.manage.service;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.common.dto.Pagination;
import me.sghong.manager.app.common.dto.PagingDto;
import me.sghong.manager.app.manage.dto.AdminDto;
import me.sghong.manager.app.manage.dto.AdminSearchDto;
import me.sghong.manager.app.manage.mapper.AdminMapper;
import me.sghong.manager.app.manage.request.AdminAddRequest;
import me.sghong.manager.util.CommonUtil;
import me.sghong.manager.util.SHA512;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final AdminMapper adminMapper;

    public AdminDto getAdminInfoForGroup(String adminId) {
        return adminMapper.select_by_adminId_for_group(adminId);
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

    public PagingDto<AdminDto> getList(AdminSearchDto adminSearchDto) {
        int count = adminMapper.select_by_list_for_totalCount(adminSearchDto);

        if (count < 1) {
            return new PagingDto<>(Collections.emptyList(), null);
        }

        Pagination pagination = new Pagination(count, adminSearchDto);
        adminSearchDto.setPagination(pagination);

        List<AdminDto> adminDtoList = adminMapper.select_by_list(adminSearchDto);
        return new PagingDto<>(adminDtoList, pagination);
    }

    public String getAdminIdDuplicate(String adminId) {
        if (adminMapper.select_by_adminId_For_Count(adminId) == 0) {
            return "OK";
        } else {
            return "EXISTS";
        }
    }

    @Transactional
    public void insertAdmin(AdminAddRequest adminAddRequest) {
        SHA512 sha512 = new SHA512();

        AdminDto adminDto = AdminDto.builder()
                .adminid(adminAddRequest.getAdminid())
                .groupcode(adminAddRequest.getGroupcode())
                .adminname(adminAddRequest.getName())
                .adminpwd(sha512.encode(adminAddRequest.getPwd()))
                .hp(adminAddRequest.getHp1()+"-"+adminAddRequest.getHp2()+"-"+adminAddRequest.getHp3())
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();

        adminMapper.insert_admin(adminDto);
    }

    public AdminDto getAdminInfo(String adminId) {
        return adminMapper.select_by_adminId(adminId);
    }
}
