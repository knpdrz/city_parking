package city.parking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ParkingMeterNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ParkingMeterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String parkingMeterNotFoundHandler(ParkingMeterNotFoundException e){
        return e.getMessage();
    }
}
