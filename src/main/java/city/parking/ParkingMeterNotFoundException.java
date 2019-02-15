package city.parking;

public class ParkingMeterNotFoundException extends RuntimeException {
    public ParkingMeterNotFoundException(Integer spotId){
        super("Could not find parking meter for spot " + spotId);
    }
}
