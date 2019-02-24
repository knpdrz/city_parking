package city.parking.entities;

import lombok.Data;

@Data
public class ParkingProcessPartialUpdateRequest {
    private boolean parkingMeterToBeStopped;
}
