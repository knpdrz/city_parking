package city.parking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ParkingProcessNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ParkingProcessNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String parkingProcessNotFoundHandler(ParkingProcessNotFoundException e){
        return e.getMessage();
    }
}
