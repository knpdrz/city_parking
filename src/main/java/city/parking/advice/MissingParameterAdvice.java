package city.parking.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class MissingParameterAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        StringBuilder missingParameterErrorMessage = new StringBuilder();
        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        for(ObjectError error: errorList){
            missingParameterErrorMessage.append(error.getDefaultMessage());
            missingParameterErrorMessage.append("\n");
        }
        return ResponseEntity.badRequest().body(missingParameterErrorMessage);
    }

}
