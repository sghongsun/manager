package me.sghong.manager.app.manage.mapper;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.dto.MenuDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<MenuDto> select_for_Depth1_For_Use();
    List<MenuDto> select_by_Depth1_For_All();
    List<MenuDto> select_by_Depth2_For_Use(String menuPCode);
    List<MenuDto> select_by_Depth2_For_All(String menuPCode);
    MenuDto select_by_menuUrl(String menuUrl);
    int select_by_menuUrl_Like_Count(String menuCode, String menuUrl);
    int select_by_max_menucode(String menuPCode);
    int select_by_max_menudispnum(String menuPCode);
    void insert_by_menu(MenuDto menuDto);
    MenuDto select_by_menuPCode_menuCode(String menuPCode, String menuCode);
    void modify_by_menu(MenuDto menuDto);
    void delete_by_menuPCode_menuCode(String menuPCode, String menuCode);
    MenuDto select_by_menuPCode_menuCode_For_DisplayNum_Up(String menuPCode, int displayNum);
    MenuDto select_by_menuPCode_menuCode_For_DisplayNum_Down(String menuPCode, int displayNum);
    void modify_by_menuDisplayNum(String menuPCode, String menuCode, int displayNum);
}
