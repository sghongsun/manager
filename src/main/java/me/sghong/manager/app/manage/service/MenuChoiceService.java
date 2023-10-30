package me.sghong.manager.app.manage.service;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.dto.MenuChoiceDto;
import me.sghong.manager.app.manage.mapper.MenuChoiceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MenuChoiceService {
    private final MenuChoiceMapper menuChoiceMapper;

    public List<MenuChoiceDto> getList(String adminId) {
        return menuChoiceMapper.select_by_adminId(adminId);
    }
    public void delete_menuChoice_By_menuCode(String menuCode) {
        menuChoiceMapper.delete_by_menuCode(menuCode);
    }
}
