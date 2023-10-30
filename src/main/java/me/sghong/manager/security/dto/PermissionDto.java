package me.sghong.manager.security.dto;

import lombok.*;
import me.sghong.manager.app.manage.dto.MenuDto;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PermissionDto {
    private String MenuCode1;
    private String MenuCode2;
    private boolean isWrite;
    private String MenuChoice;
    private List<MenuDto> TopMenuList;
    private List<MenuDto> LeftMenuList;
    private String TopMenuName;
    private String LeftMenuName;

}
