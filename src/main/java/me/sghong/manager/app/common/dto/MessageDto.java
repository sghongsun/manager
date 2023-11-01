package me.sghong.manager.app.common.dto;

import lombok.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MessageDto {
    private String message;
    private String redirectUri;
    private RequestMethod method;
    private Map<String, Object> data;
}
