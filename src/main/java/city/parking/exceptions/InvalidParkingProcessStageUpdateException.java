package city.parking.exceptions;

public class InvalidParkingProcessStageUpdateException extends RuntimeException {
    public InvalidParkingProcessStageUpdateException(String newParkingProcessStage){
        super("Parking process stage cannot be updated to " + newParkingProcessStage);
    }
}
