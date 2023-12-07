package me.sghong.manager.exception;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.util.CommonUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@RequiredArgsConstructor
@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public void handler(final CustomException ex) throws IOException {
        CommonUtil.AlertMessage(ex.getMessage(), ex.getLocation());
    }
}
