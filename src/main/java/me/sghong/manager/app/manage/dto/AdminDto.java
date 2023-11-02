package me.sghong.manager.app.manage.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Alias("adminDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AdminDto {
    private String adminid;
    private String adminpwd;
    private String adminname;
    private int groupcode;
    private String hp;
    private String authflag;
    private int pwderrcnt;
    private String groupname;
    private String groupwrite;
    private String groupread;
    private String createid;
    private String createip;
    private LocalDateTime createdt;
    private String updateid;
    private String updateip;
    private LocalDateTime updatedt;
}
