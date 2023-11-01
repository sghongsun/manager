package me.sghong.manager.app.common.service;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.common.dto.MyMenuChoiceDto;
import me.sghong.manager.app.common.mapper.MyMenuChoiceMapper;
import me.sghong.manager.app.common.request.MyMenuChoiceAddDeleteRequest;
import me.sghong.manager.app.common.request.MyMenuChoiceDispNumUpdateRequest;
import me.sghong.manager.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MyMenuChoiceService {
    private final MyMenuChoiceMapper myMenuChoiceMapper;

    public List<MyMenuChoiceDto> getList(String adminId) {
        return myMenuChoiceMapper.select_by_adminId(adminId);
    }

    public void delete_menuChoice_By_menuCode(String menuCode) {
        myMenuChoiceMapper.delete_by_menuCode(menuCode);
    }

    @Transactional
    public String insertMyMenu(MyMenuChoiceAddDeleteRequest myMenuChoiceAddDeleteRequest) {
        MyMenuChoiceDto myMenuChoiceDto = MyMenuChoiceDto.builder()
                .adminid(CommonUtil.getSession("adminid"))
                .menucode(myMenuChoiceAddDeleteRequest.getMenucode())
                .build();
        if (myMenuChoiceMapper.select_by_adminId_menuCode_for_Count(myMenuChoiceDto) > 0) {
            return "이미 등록된 메뉴 입니다.";
        }

        myMenuChoiceDto.setDispnum(myMenuChoiceMapper.select_by_adminId_for_dispNum_Max(myMenuChoiceDto));
        myMenuChoiceMapper.insert_by_myMenu(myMenuChoiceDto);

        String MenuChoice = "";
        List<MyMenuChoiceDto> myMenuChoiceDtoList = myMenuChoiceMapper.select_by_adminId(myMenuChoiceDto.getAdminid());
        if (!myMenuChoiceDtoList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for(MyMenuChoiceDto key : myMenuChoiceDtoList) {
                sb.append(key.getMenucode()).append(",");
            }
            MenuChoice = sb.deleteCharAt(sb.length() - 1).toString();
        }

        CommonUtil.setSession("menuchoice", MenuChoice);

        return "OK|||||";
    }

    @Transactional
    public String updateMyMenuDipsNum(MyMenuChoiceDispNumUpdateRequest myMenuChoiceDispNumUpdateRequest) {
        MyMenuChoiceDto myMenuChoiceRequest = MyMenuChoiceDto.builder()
                .adminid(CommonUtil.getSession("adminid"))
                .menucode(myMenuChoiceDispNumUpdateRequest.getMenucode())
                .build();

        MyMenuChoiceDto myMenuChoiceDto = myMenuChoiceMapper.select_by_adminid_menucode(myMenuChoiceRequest);

        if (myMenuChoiceDto == null) {
            return "FAIL|||||없는 메뉴 입니다.";
        } else {
            int nowDispNum = myMenuChoiceDto.getDispnum();
            MyMenuChoiceDto otherDto = null;
            switch (myMenuChoiceDispNumUpdateRequest.getUdType()) {
                case "U" -> otherDto = myMenuChoiceMapper.select_by_adminid_for_dispnum_Up(myMenuChoiceDto);
                case "D" -> otherDto = myMenuChoiceMapper.select_by_adminid_for_dispnum_Down(myMenuChoiceDto);
            }

            if (otherDto != null) {
                myMenuChoiceDto.setDispnum(otherDto.getDispnum());
                otherDto.setDispnum(nowDispNum);
                myMenuChoiceMapper.update_by_myMenu_for_dispnum_Chg(myMenuChoiceDto);
                myMenuChoiceMapper.update_by_myMenu_for_dispnum_Chg(otherDto);
            }
            return "OK|||||";
        }
    }

    @Transactional
    public String deleteMyMenu(MyMenuChoiceAddDeleteRequest myMenuChoiceAddDeleteRequest) {
        MyMenuChoiceDto myMenuChoiceRequest = MyMenuChoiceDto.builder()
                .adminid(CommonUtil.getSession("adminid"))
                .menucode(myMenuChoiceAddDeleteRequest.getMenucode())
                .build();

        MyMenuChoiceDto myMenuChoiceDto = myMenuChoiceMapper.select_by_adminid_menucode(myMenuChoiceRequest);

        myMenuChoiceMapper.delete_by_myMenu_for_dispnum_Chg(myMenuChoiceDto);
        myMenuChoiceMapper.delete_by_menucode_adminid(myMenuChoiceDto);

        String MenuChoice = "";
        List<MyMenuChoiceDto> myMenuChoiceDtoList = myMenuChoiceMapper.select_by_adminId(myMenuChoiceDto.getAdminid());
        if (!myMenuChoiceDtoList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for(MyMenuChoiceDto key : myMenuChoiceDtoList) {
                sb.append(key.getMenucode()).append(",");
            }
            MenuChoice = sb.deleteCharAt(sb.length() - 1).toString();
        }

        CommonUtil.setSession("menuchoice", MenuChoice);

        return "OK|||||";
    }
}
