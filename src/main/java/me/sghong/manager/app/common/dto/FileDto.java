package me.sghong.manager.app.common.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FileDto {
    private String originFileName;
    private String saveFileName;
    private long fileSize;
}
