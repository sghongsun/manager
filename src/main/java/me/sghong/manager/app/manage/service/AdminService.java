package me.sghong.manager.app.manage.service;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.common.dto.Pagination;
import me.sghong.manager.app.common.dto.PagingDto;
import me.sghong.manager.app.manage.dto.AdminDto;
import me.sghong.manager.app.manage.dto.AdminLoginDto;
import me.sghong.manager.app.manage.dto.AdminSearchDto;
import me.sghong.manager.app.manage.mapper.AdminMapper;
import me.sghong.manager.app.manage.request.*;
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

    @Transactional
    public String updateAdminGroup(AdminModifyGroupRequest adminModifyGroupRequest) {
        String[] AdminId = adminModifyGroupRequest.getAdminid().split(",");
        String[] GroupCode = adminModifyGroupRequest.getGroupcode().split(",");

        if (AdminId.length != GroupCode.length) {
            return "FAIL||||값이 상이 합니다.";
        }

        for (int i=0; i<AdminId.length; i++) {
            AdminDto adminDto = AdminDto.builder()
                    .adminid(AdminId[i])
                    .groupcode(Integer.parseInt(GroupCode[i]))
                    .createid(CommonUtil.getSession("adminid"))
                    .createip(CommonUtil.getSession("ip"))
                    .build();
            adminMapper.update_admin_for_groupcode(adminDto);
        }

        return "OK|||||";
    }

    @Transactional
    public String updateAdminPwd(AdminModifyPwdRequet adminModifyPwdRequet) {
        if (!adminModifyPwdRequet.getPwd().equals(adminModifyPwdRequet.getPwd1())) {
            return "FAIL|||||비밀번호가 상이 합니다.";
        }

        if (adminMapper.select_by_adminId(adminModifyPwdRequet.getAdminid()) == null) {
            return "FAIL|||||정보가 없습니다.";
        }

        SHA512 sha512 = new SHA512();
        AdminDto adminDto = AdminDto.builder()
                .adminid(adminModifyPwdRequet.getAdminid())
                .adminpwd(sha512.encode(adminModifyPwdRequet.getPwd()))
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        adminMapper.update_adminpwd(adminDto);

        return "OK|||||";
    }

    @Transactional
    public String updateAdminHp(AdminModifyHpRequest adminModifyHpRequest) {
        AdminDto chkDto = adminMapper.select_by_adminId(adminModifyHpRequest.getAdminid());

        if (chkDto == null) {
            return "정보가 없습니다.";
        }

        if (chkDto.getHp().equals(adminModifyHpRequest.getHp1()+"-"+adminModifyHpRequest.getHp2()+"-"+adminModifyHpRequest.getHp3())) {
            return "현재 핸드폰 번호와 동일 합니다.";
        }

        AdminDto adminDto = AdminDto.builder()
                .adminid(adminModifyHpRequest.getAdminid())
                .hp(adminModifyHpRequest.getHp1()+"-"+adminModifyHpRequest.getHp2()+"-"+adminModifyHpRequest.getHp3())
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        adminMapper.update_adminhp(adminDto);

        return "OK";
    }

    @Transactional
    public String updateAdminInfo(AdminModifyRequest adminModifyRequest) {
        AdminDto chkDto = adminMapper.select_by_adminId(adminModifyRequest.getAdminid());

        if (chkDto == null) {
            return "정보가 없습니다.";
        }

        AdminDto adminDto = AdminDto.builder()
                .adminid(adminModifyRequest.getAdminid())
                .adminname(adminModifyRequest.getName())
                .groupcode(adminModifyRequest.getGroupcode())
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        adminMapper.update_admininfo(adminDto);

        return "OK";
    }

    @Transactional
    public String deleteAdmin(AdminDeleteRequest adminDeleteRequest) {
        if (adminMapper.select_by_adminId(adminDeleteRequest.getAdminid()) == null) {
            return "정보가 없습니다.";
        }

        AdminDto adminDto = AdminDto.builder()
                .adminid(adminDeleteRequest.getAdminid())
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        adminMapper.delete_admin(adminDto);

        return "OK";
    }

    public PagingDto<AdminLoginDto> getAdminLoginList(AdminSearchDto adminSearchDto) {
        int count = adminMapper.select_by_admin_login_for_totalCount(adminSearchDto);

        if (count < 1) {
            return new PagingDto<>(Collections.emptyList(), null);
        }

        Pagination pagination = new Pagination(count, adminSearchDto);
        adminSearchDto.setPagination(pagination);

        List<AdminLoginDto> adminLoginDtoList = adminMapper.select_by_admin_login_list(adminSearchDto);
        return new PagingDto<>(adminLoginDtoList, pagination);
    }

    public List<AdminDto> getListForGroupSearch(String mCode2, String authType) {
        return adminMapper.select_by_list_for_group_search(mCode2, authType);
    }
}
