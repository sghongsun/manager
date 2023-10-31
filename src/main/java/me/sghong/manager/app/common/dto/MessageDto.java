package me.sghong.manager.app.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class MessageDto {
    private String message;
    private String redirectUri;
    private RequestMethod method;
    private Map<String, Object> data;
}
