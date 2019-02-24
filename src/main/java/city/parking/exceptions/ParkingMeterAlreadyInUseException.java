package city.parking.exceptions;

public class ParkingMeterAlreadyInUseException extends RuntimeException {
    public ParkingMeterAlreadyInUseException(Integer meterId){
        super("Parking meter with id " + meterId + " cannot be used- it is already started or unpaid" );
    }
}
