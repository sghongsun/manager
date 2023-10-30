package me.sghong.manager.app.manage.service;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.dto.AdminGroupDto;
import me.sghong.manager.app.manage.mapper.AdminGroupMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminGroupService {
    private final AdminGroupMapper adminGroupMapper;

    public AdminGroupDto getInfoByGroupCode(String groupCode) {
        return adminGroupMapper.select_by_groupCode(groupCode);
    }
}
