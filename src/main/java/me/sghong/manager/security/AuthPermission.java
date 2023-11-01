package me.sghong.manager.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.dto.AdminGroupDto;
import me.sghong.manager.app.manage.dto.MenuDto;
import me.sghong.manager.app.manage.service.AdminGroupService;
import me.sghong.manager.app.manage.service.MenuService;
import me.sghong.manager.security.dto.PermissionDto;
import me.sghong.manager.util.CommonUtil;
import me.sghong.manager.util.Constants;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class AuthPermission {
    private final HttpServletRequest request;
    private final MenuService menuService;
    private final AdminGroupService adminGroupService;

    public PermissionDto getPermission() throws IOException {
        String pageType = getPageType(request.getRequestURI());
        String returnPageType = getReturnPageType(request.getRequestURI());

        String adminGroup = CommonUtil.getSession("admingroup");

        if (adminGroup == null || adminGroup.isEmpty()) {
            CommonUtil.AlertMessage("유효하지 않은 경로 입니다.", returnPageType);
            return null;
        }

        AdminGroupDto adminGroupDto = adminGroupService.getInfoByGroupCode(Integer.parseInt(adminGroup));
        if (adminGroupDto == null) {
            CommonUtil.AlertMessage("유효하지 않은 경로 입니다.", returnPageType);
            return null;
        }

        /* 메인페이지인 경우 */
        if (request.getRequestURI().equals("/") && request.getMethod().equals("GET")) {
            return PermissionDto.builder()
                    .MenuCode1("0000")
                    .MenuCode2("0000")
                    .isWrite(false)
                    .MenuChoice("N")
                    .TopMenuList(getTopMenu(adminGroupDto.getGroupread()))
                    .build();
        }
        /* 메인페이지인 경우 */

        /* URL 정보 */
        String thisPage = "";
        String[] thisUri = request.getRequestURI().split("/");
        if (thisUri.length > 1) {
            thisPage = "/" + thisUri[1] + "/" + thisUri[2];
        } else {
            thisPage = request.getRequestURI();
        }

        MenuDto menuDto = menuService.getMenuInfoByMenuUrl(thisPage);
        if (menuDto == null) {
            CommonUtil.AlertMessage("유효하지 않은 경로 입니다.", returnPageType);
            return null;
        }
        /* URL 정보 */

        /* 일반 정보 */
        if (pageType.equals("ajax")) {
            if (request.getHeader("referer") == null) {
                CommonUtil.AlertMessage("유효하지 않은 경로 입니다.", returnPageType);
                return null;
            } else {
                if (!request.getHeader("referer").contains(Constants.domain)) {
                    CommonUtil.AlertMessage("유효하지 않은 경로 입니다.", returnPageType);
                    return null;
                }
            }
        }
        if (!adminGroupDto.getGroupread().contains(menuDto.getMenucode())) {
            CommonUtil.AlertMessage("권한이 없습니다.", returnPageType);
            return null;
        }

        boolean isWrite = adminGroupDto.getGroupwrite().contains(menuDto.getMenucode());
        if (pageType.equals("general")) {
            List<MenuDto> topMenuList = getTopMenu(adminGroupDto.getGroupread());
            List<MenuDto> leftMenuList = getLeftMenu(menuDto.getMenupcode());

            return PermissionDto.builder()
                    .MenuCode1(menuDto.getMenupcode())
                    .MenuCode2(menuDto.getMenucode())
                    .isWrite(isWrite)
                    .MenuChoice(menuDto.getMenuchoice())
                    .TopMenuList(topMenuList)
                    .LeftMenuList(leftMenuList)
                    .TopMenuName(getMenuName(menuDto.getMenupcode(), topMenuList))
                    .LeftMenuName(getMenuName(menuDto.getMenucode(), leftMenuList))
                    .build();
        } else {
            return PermissionDto.builder()
                    .MenuCode1(menuDto.getMenupcode())
                    .MenuCode2(menuDto.getMenucode())
                    .isWrite(isWrite)
                    .MenuChoice(menuDto.getMenuchoice())
                    .build();
        }
        /* 일반 정보 */
   }

   public boolean AuthPostCheck() throws IOException {
       String returnPageType = getReturnPageType(request.getRequestURI());

       String adminGroup = CommonUtil.getSession("admingroup");

       if (adminGroup == null || adminGroup.isEmpty()) {
           CommonUtil.AlertMessage("유효하지 않은 경로 입니다.", returnPageType);
           return false;
       }

       AdminGroupDto adminGroupDto = adminGroupService.getInfoByGroupCode(Integer.parseInt(adminGroup));
       if (adminGroupDto == null) {
           CommonUtil.AlertMessage("유효하지 않은 경로 입니다.", returnPageType);
           return false;
       }

       if (request.getHeader("referer") == null) {
           CommonUtil.AlertMessage("유효하지 않은 경로 입니다.", returnPageType);
           return false;
       } else {
           if (!request.getHeader("referer").contains(Constants.domain)) {
               CommonUtil.AlertMessage("유효하지 않은 경로 입니다.", returnPageType);
               return false;
           }
       }

       /* URL 정보 */
       String thisPage = "";
       String[] thisUri = request.getRequestURI().split("/");
       if (thisUri.length > 1) {
           thisPage = "/" + thisUri[1] + "/" + thisUri[2];
       } else {
           thisPage = request.getRequestURI();
       }

       if (!thisPage.contains("/common")) {
           MenuDto menuDto = menuService.getMenuInfoByMenuUrl(thisPage);
           if (menuDto == null) {
               CommonUtil.AlertMessage("유효하지 않은 경로 입니다.", returnPageType);
               return false;
           }
           /* URL 정보 */

           if (!adminGroupDto.getGroupwrite().contains(menuDto.getMenucode())) {
               CommonUtil.AlertMessage("권한이 없습니다.", returnPageType);
               return false;
           }
       }
       return true;
   }

    public List<MenuDto> getTopMenu(String groupRead) {
        List<MenuDto> menuDtoList = new ArrayList<MenuDto>();
        for (MenuDto key : menuService.getMenuDepth1ForUse()) {
            if (groupRead.contains(key.getMenucode()) || groupRead.equals("0")) {
                key.setMenuurl(getChildMenuUrl(groupRead, key.getMenucode()));
                menuDtoList.add(key);
            }
        }
        return menuDtoList;
    }

    public List<MenuDto> getLeftMenu(String PCode) {
        return menuService.getMenuDepth2ForUse(PCode);
    }

    public String getChildMenuUrl(String groupRead, String PCode) {
        String retUrl = "#";

        for (MenuDto key : menuService.getMenuDepth2ForUse(PCode)) {
            if (key.getMenupcode().equals(PCode)) {
                if (groupRead.equals("0")) {
                    retUrl = key.getMenuurl();
                    break;
                } else {
                    if (groupRead.contains(key.getMenucode())) {
                        retUrl = key.getMenuurl();
                        break;
                    }
                }
            }
        }
        return retUrl;
    }

    public String getPageType(String thisPage) {
        if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equals("XMLHttpRequest")) {
            return "ajax";
        } else {
            if (thisPage.contains("/popup")) {
                return "popup";
            } else {
                return "general";
            }
        }
    }

    public String getReturnPageType(String thisPage) {
        if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equals("XMLHttpRequest")) {
            return "ajax";
        } else {
            if (thisPage.contains("/popup")) {
                return "window.close();";
            } else {
                return "history.back();";
            }
        }
    }

    public String getMenuName(String MenuCode, List<MenuDto> MenuList) {
        String retVal = "";

        for (MenuDto key : MenuList) {
            if (MenuCode.equals(key.getMenucode())) {
                retVal = key.getMenuname();
                break;
            }
        }
        return retVal;
    }
}
