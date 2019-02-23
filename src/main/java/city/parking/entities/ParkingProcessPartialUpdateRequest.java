package city.parking.entities;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ParkingProcessPartialUpdateRequest {
    @NotNull(message = "New parking process stage must not be null")
    private ParkingProcess.Stage newParkingProcessStage;
}
