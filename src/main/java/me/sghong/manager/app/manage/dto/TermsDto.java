package me.sghong.manager.app.manage.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Alias("termsDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TermsDto {
    private int idx;
    private String title;
    private String place;
    private String contents;
    private String createid;
    private String createip;
    private LocalDateTime createdt;

}
