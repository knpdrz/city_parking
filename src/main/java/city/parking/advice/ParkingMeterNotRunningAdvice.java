package city.parking.advice;

import city.parking.exceptions.ParkingMeterAlreadyInUseException;
import city.parking.exceptions.ParkingMeterNotRunningException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ParkingMeterNotRunningAdvice {
    @ResponseBody
    @ExceptionHandler(ParkingMeterNotRunningException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String parkingMeterNotRunningHandler(ParkingMeterNotRunningException e) {
        return e.getMessage();
    }
}
