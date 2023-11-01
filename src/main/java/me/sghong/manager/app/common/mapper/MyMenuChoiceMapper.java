package me.sghong.manager.app.common.mapper;

import me.sghong.manager.app.common.dto.MyMenuChoiceDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyMenuChoiceMapper {
    List<MyMenuChoiceDto> select_by_adminId(String adminId);
    void delete_by_menuCode(String menuCode);
    int select_by_adminId_menuCode_for_Count(MyMenuChoiceDto myMenuChoiceDto);
    int select_by_adminId_for_dispNum_Max(MyMenuChoiceDto myMenuChoiceDto);
    void insert_by_myMenu(MyMenuChoiceDto myMenuChoiceDto);
    void delete_by_menucode_adminid(MyMenuChoiceDto myMenuChoiceDto);
    MyMenuChoiceDto select_by_adminid_menucode(MyMenuChoiceDto myMenuChoiceDto);
    MyMenuChoiceDto select_by_adminid_for_dispnum_Up(MyMenuChoiceDto myMenuChoiceDto);
    MyMenuChoiceDto select_by_adminid_for_dispnum_Down(MyMenuChoiceDto myMenuChoiceDto);
    void update_by_myMenu_for_dispnum_Chg(MyMenuChoiceDto myMenuChoiceDto);
    void delete_by_myMenu_for_dispnum_Chg(MyMenuChoiceDto myMenuChoiceDto);
    void delete_by_myMenu(MyMenuChoiceDto myMenuChoiceDto);
}
