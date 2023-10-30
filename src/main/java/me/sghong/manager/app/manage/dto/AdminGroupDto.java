package me.sghong.manager.app.manage.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Alias("adminGroupDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminGroupDto {
    private int groupcode;
    private String groupname;
    private String groupdesc;
    private String groupwrite;
    private String groupread;
    private int admincnt;
    @Setter
    private String id;
    @Setter
    private String ip;
}
