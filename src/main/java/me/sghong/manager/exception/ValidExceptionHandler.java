package me.sghong.manager.exception;

import me.sghong.manager.util.CommonUtil;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class ValidExceptionHandler {
    private String fieldName;
    private String errorMessage;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void validationMsg(final MethodArgumentNotValidException e) throws IOException {
       e.getBindingResult().getAllErrors().forEach((error)-> {
            fieldName = ((FieldError) error).getField();
            errorMessage = error.getDefaultMessage();
        });
        CommonUtil.AlertMessage(errorMessage, "history.back();");
    }
}
