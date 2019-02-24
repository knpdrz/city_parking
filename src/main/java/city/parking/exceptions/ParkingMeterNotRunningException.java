package city.parking.exceptions;

public class ParkingMeterNotRunningException extends RuntimeException {
    public ParkingMeterNotRunningException(Integer processId){
        super("Parking process with id " + processId + " cannot be stopped, because it is not ongoing" );
    }
}
