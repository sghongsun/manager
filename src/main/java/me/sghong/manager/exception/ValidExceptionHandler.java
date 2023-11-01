package me.sghong.manager.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.security.AuthPermission;
import me.sghong.manager.util.CommonUtil;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@RequiredArgsConstructor
@ControllerAdvice
public class ValidExceptionHandler {
    private String fieldName;
    private String errorMessage;
    private final AuthPermission authPermission;
    private final HttpServletRequest request;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void validationMsg(final MethodArgumentNotValidException e) throws IOException {
       e.getBindingResult().getAllErrors().forEach((error)-> {
            fieldName = ((FieldError) error).getField();
            errorMessage = error.getDefaultMessage();
       });

       String location = authPermission.getPageType(request.getRequestURI()).equals("ajax") ? "ajax" : "history.back();";
       CommonUtil.AlertMessage(errorMessage, location);
    }
}
