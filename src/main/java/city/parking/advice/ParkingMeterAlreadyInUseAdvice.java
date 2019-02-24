package city.parking.advice;

import city.parking.exceptions.ParkingMeterAlreadyInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ParkingMeterAlreadyInUseAdvice {
    @ResponseBody
    @ExceptionHandler(ParkingMeterAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String parkingMeterAlreadyInUseHandler(ParkingMeterAlreadyInUseException e) {
        return e.getMessage();
    }
}
