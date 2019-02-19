package city.parking;

import city.parking.entities.ParkingProcess;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ParkingProcessStageConverter implements Converter<String, ParkingProcess.Stage> {

    @Override
    public ParkingProcess.Stage convert(String source){
        try{
            source = source.toUpperCase();
            return ParkingProcess.Stage.valueOf(source);
        }catch (IllegalArgumentException e){
            return null;
        }
    }
}
