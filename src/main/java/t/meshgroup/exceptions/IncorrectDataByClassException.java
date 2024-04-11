package t.meshgroup.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpResponseException;
import org.springframework.http.HttpStatus;

@Slf4j
public class IncorrectDataByClassException extends HttpResponseException {
    public IncorrectDataByClassException(Class clazz, String errorMessage, HttpStatus statusCode) {
        super(statusCode.value(), clazz.getName() + " - " + errorMessage);
    }

    public IncorrectDataByClassException(Class clazz, String errorMessage) {
        super(500, clazz.getName() + " - " + errorMessage);
    }

    public IncorrectDataByClassException(Class clazz, String errorMessage, HttpStatus statusCode, ExceptionLevel exceptionLevel) {
        super(statusCode.value(), clazz.getName() + " - " + errorMessage);
        if (exceptionLevel == ExceptionLevel.CRITICAL)
            log.error(String.format("Class: %s; Error - %s; HttpStatus - %d", clazz, errorMessage, statusCode.value()));
    }
}
