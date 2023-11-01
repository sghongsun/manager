package me.sghong.manager.app.manage.service;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.dto.AdminGroupDto;
import me.sghong.manager.app.manage.mapper.AdminGroupMapper;
import me.sghong.manager.app.manage.request.AdminGroupRequest;
import me.sghong.manager.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminGroupService {
    private final AdminGroupMapper adminGroupMapper;

    public AdminGroupDto getInfoByGroupCode(int groupCode) {
        return adminGroupMapper.select_by_groupCode(groupCode);
    }

    public List<AdminGroupDto> getList() {
        return adminGroupMapper.select_by_list();
    }

    @Transactional
    public void insertAdminGroup(AdminGroupRequest adminGroupRequest) {
        String mainWrite="";
        if (adminGroupRequest.getMain_write() != null) {
            mainWrite = CommonUtil.ArrarytoStringForComma(adminGroupRequest.getMain_write());
        }

        String mainRead="";
        if (adminGroupRequest.getMain_read() != null) {
            mainRead = CommonUtil.ArrarytoStringForComma(adminGroupRequest.getMain_read());
        }

        String subWrite="";
        if (adminGroupRequest.getSub_write() != null) {
            subWrite = CommonUtil.ArrarytoStringForComma(adminGroupRequest.getSub_write());
        }

        String subRead="";
        if (adminGroupRequest.getSub_read() != null) {
            subRead = CommonUtil.ArrarytoStringForComma(adminGroupRequest.getSub_read());
        }

        if (!mainWrite.isEmpty()) {
            mainWrite = mainWrite + "," + subWrite;
        }

        if (!mainRead.isEmpty()) {
            mainRead = mainRead + "," + subRead;
        }

        AdminGroupDto adminGroupDto = AdminGroupDto.builder()
                .groupcode(adminGroupMapper.select_by_groupcode_max())
                .groupname(adminGroupRequest.getGroupname())
                .groupdesc(adminGroupRequest.getGroupdesc())
                .groupwrite(mainWrite)
                .groupread(mainRead)
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();

        adminGroupMapper.insert_admingroup(adminGroupDto);
    }

    @Transactional
    public void updateAdminGroup(int groupCode, AdminGroupRequest adminGroupRequest) {
        String mainWrite="";
        if (adminGroupRequest.getMain_write() != null) {
            mainWrite = CommonUtil.ArrarytoStringForComma(adminGroupRequest.getMain_write());
        }

        String mainRead="";
        if (adminGroupRequest.getMain_read() != null) {
            mainRead = CommonUtil.ArrarytoStringForComma(adminGroupRequest.getMain_read());
        }

        String subWrite="";
        if (adminGroupRequest.getSub_write() != null) {
            subWrite = CommonUtil.ArrarytoStringForComma(adminGroupRequest.getSub_write());
        }

        String subRead="";
        if (adminGroupRequest.getSub_read() != null) {
            subRead = CommonUtil.ArrarytoStringForComma(adminGroupRequest.getSub_read());
        }

        if (!mainWrite.isEmpty()) {
            mainWrite = mainWrite + "," + subWrite;
        }

        if (!mainRead.isEmpty()) {
            mainRead = mainRead + "," + subRead;
        }

        AdminGroupDto adminGroupDto = AdminGroupDto.builder()
                .groupcode(groupCode)
                .groupname(adminGroupRequest.getGroupname())
                .groupdesc(adminGroupRequest.getGroupdesc())
                .groupwrite(mainWrite)
                .groupread(mainRead)
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();

        adminGroupMapper.update_admingroup(adminGroupDto);
    }

    @Transactional
    public void deleteAdminGroup(int groupCode) {
        adminGroupMapper.delete_admingroup(groupCode);
    }
}
