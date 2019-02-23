package city.parking.exceptions;

import city.parking.entities.ParkingProcessPartialUpdateRequest;
import city.parking.exceptions.InvalidParkingProcessStageUpdateException;
import city.parking.exceptions.ParkingProcessNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidParkingProcessStageUpdateAdvice {
    @ResponseBody
    @ExceptionHandler(InvalidParkingProcessStageUpdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidParkingProcessStageUpdateHandler(InvalidParkingProcessStageUpdateException e){
        return e.getMessage();
    }
}
