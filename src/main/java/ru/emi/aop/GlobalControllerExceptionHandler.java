package ru.emi.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.emi.util.Ajax;
import ru.emi.util.MyException;
import java.util.Map;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Throwable.class)
    public Map<String, Object> generalError(Exception ex) {
        log.error("We have got an exception when processing a request: ", ex);
        return Ajax.errorResponse("generalError: " + ex.getMessage() + ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MyException.class)
    public Map<String, Object> validationError(Exception ex) {
        log.error("We have got an exception when processing a request: ", ex);
        return Ajax.errorResponse(ex.getMessage());
    }
}