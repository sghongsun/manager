package me.sghong.manager.app.manage.service;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.common.service.MyMenuChoiceService;
import me.sghong.manager.app.manage.dto.MenuAuthDto;
import me.sghong.manager.app.manage.dto.MenuDto;
import me.sghong.manager.app.manage.mapper.MenuMapper;
import me.sghong.manager.app.manage.request.MenuAddRequest;
import me.sghong.manager.app.manage.request.MenuDeleteRequest;
import me.sghong.manager.app.manage.request.MenuDispNumModifyRequest;
import me.sghong.manager.app.manage.request.MenuModifyRequest;
import me.sghong.manager.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MenuService {
    private final MenuMapper menuMapper;
    private final MyMenuChoiceService myMenuChoiceService;

    public List<MenuDto> getMenuDepth1ForUse() {
        return menuMapper.select_for_Depth1_For_Use();
    }

    public MenuDto getMenuInfoByMenuUrl(
            String menuUrl
    ) {
        return menuMapper.select_by_menuUrl(menuUrl);
    }

    public List<MenuDto> getMenuDepth2ForUse(
            String PCode
    ) {
        return menuMapper.select_by_Depth2_For_Use(PCode);
    }

    public List<MenuDto> getMenuDepth1ForAll() {
        return menuMapper.select_by_Depth1_For_All();
    }

    public List<MenuDto> getMenuDepth2ForAll(
            String PCode
    ) {
        return menuMapper.select_by_Depth2_For_All(PCode);
    }

    public List<MenuDto> getMenuDepth2ForAll() {
        return menuMapper.select_by_Depth2_For_All_NoPCode();
    }

    @Transactional
    public String addMenu(
            MenuAddRequest menuAddRequest
    ) {
        if (!menuAddRequest.getMenuPCode().equals("0000")) {
            String result = MenuUrlChk("0000", menuAddRequest.getMenuUrl());
            if (!result.equals("OK")) {
                return result;
            }
        }

        int MaxCode = menuMapper.select_by_max_menucode(menuAddRequest.getMenuPCode());
        int MaxDisplayNum = menuMapper.select_by_max_menudispnum(menuAddRequest.getMenuPCode());

        if (MaxCode == 0) {
            MaxCode = Integer.parseInt(menuAddRequest.getMenuPCode());
        }

        if (menuAddRequest.getMenuPCode().equals("0000")) {
            MaxCode += 100;
        } else {
            MaxCode += 1;
        }

        String MenuCode = CommonUtil.MaketoZero(Integer.toString(MaxCode), 4);

        MenuDto menuDto = MenuDto.builder()
                .menucode(MenuCode)
                .menupcode(menuAddRequest.getMenuPCode())
                .menuname(menuAddRequest.getMenuName())
                .menuurl(menuAddRequest.getMenuUrl())
                .menudispnum(MaxDisplayNum)
                .menuuseflag("Y")
                .menuchoice("N")
                .build();

        menuMapper.insert_by_menu(menuDto);

        return "OK";
    }

    public MenuDto getMenuInfo(
            String menuPCode,
            String menuCode
    ) {
        return menuMapper.select_by_menuPCode_menuCode(menuPCode, menuCode);
    }

    @Transactional
    public String modifyMenu(
            MenuModifyRequest menuModifyRequest
    ) {
        if (!menuModifyRequest.getMenuPCode().equals("0000")) {
            String result = MenuUrlChk(menuModifyRequest.getMenuCode(), menuModifyRequest.getMenuUrl());
            if (!result.equals("OK")) {
                return result;
            }
        }

        MenuDto menuDto = MenuDto.builder()
                .menupcode(menuModifyRequest.getMenuPCode())
                .menucode(menuModifyRequest.getMenuCode())
                .menuname(menuModifyRequest.getMenuName())
                .menuurl(menuModifyRequest.getMenuUrl())
                .menuuseflag(menuModifyRequest.getMenuUseFlag())
                .menuchoice(menuModifyRequest.getMenuChoice())
                .build();

        menuMapper.modify_by_menu(menuDto);

        return "OK";
    }

    @Transactional
    public String deleteMenu(
            MenuDeleteRequest menuDeleteRequest
    ) {
        if (menuDeleteRequest.getMenuPCode().equals("0000")) {
            if (!menuMapper.select_by_Depth2_For_All(menuDeleteRequest.getMenuCode()).isEmpty()) {
                return "하위 메뉴가 있어 삭제 할 수 없습니다.";
            }
        }

        menuMapper.delete_by_menuPCode_menuCode(menuDeleteRequest.getMenuPCode(), menuDeleteRequest.getMenuCode());

        if (!menuDeleteRequest.getMenuPCode().equals("0000")) {
            myMenuChoiceService.delete_menuChoice_By_menuCode(menuDeleteRequest.getMenuCode());
        }

        return "OK";
    }

    @Transactional
    public String modifyDisplayNum(
            MenuDispNumModifyRequest menuDispNumModifyRequest
    ) {
        MenuDto menuDto = menuMapper.select_by_menuPCode_menuCode(menuDispNumModifyRequest.getMenuPCode(), menuDispNumModifyRequest.getMenuCode());
        if(menuDto != null) {
            MenuDto otherMenuDto = null;
            switch (menuDispNumModifyRequest.getUdType()) {
                case "U" -> otherMenuDto = menuMapper.select_by_menuPCode_menuCode_For_DisplayNum_Up(menuDto.getMenupcode(), menuDto.getMenudispnum());
                case "D" -> otherMenuDto = menuMapper.select_by_menuPCode_menuCode_For_DisplayNum_Down(menuDto.getMenupcode(), menuDto.getMenudispnum());
            }

            if (otherMenuDto != null) {
                menuMapper.modify_by_menuDisplayNum(menuDto.getMenupcode(), menuDto.getMenucode(), otherMenuDto.getMenudispnum());
                menuMapper.modify_by_menuDisplayNum(otherMenuDto.getMenupcode(), otherMenuDto.getMenucode(), menuDto.getMenudispnum());
            }
        } else {
            return "등록 되지 않은 메뉴 입니다.";
        }

        return "OK";
    }

    public String MenuUrlChk(
            String menuCode,
            String menuUrl
    ) {
        String retVal = "OK";

        String[] arr_menuUrl = menuUrl.split("/");
        if (arr_menuUrl.length < 3) {
            retVal = "메뉴 URL 경로를 /class/method/action 형식으로 작성하여 주세요.";
            return retVal;
        }

        if (menuMapper.select_by_menuUrl_Like_Count(menuCode, "/" + arr_menuUrl[1] + "/" + arr_menuUrl[2]) > 0) {
            retVal = "URL 경로가 이미 사용 중입니다. 확인 해 보시기 바랍니다.";
            return retVal;
        }

        return retVal;
    }

    public List<MenuAuthDto> getMenuAuthList() {
        return menuMapper.select_by_list_for_menuauth();
    }
}
