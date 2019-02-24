package city.parking.exceptions;

public class ParkingProcessNotFoundException extends RuntimeException {
    public ParkingProcessNotFoundException(Integer processId){
        super("Could not find parking process with id " + processId);
    }
}
