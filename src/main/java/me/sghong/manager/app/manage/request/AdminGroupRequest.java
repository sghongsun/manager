package me.sghong.manager.app.manage.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminGroupRequest {
    @NotBlank(message = "그룹명을 입력하여 주세요.")
    private String groupname;
    @NotBlank(message = "그룹설명을 입력하여 주세요.")
    private String groupdesc;
    private String[] main_write;
    private String[] main_read;
    private String[] sub_write;
    private String[] sub_read;
}
