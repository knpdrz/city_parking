package city.parking;

import city.parking.entities.Money;
import city.parking.entities.ParkingProcess;
import city.parking.entities.ParkingProcessPartialUpdateRequest;
import city.parking.repositories.ParkingProcessRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static city.parking.TestUtils.asJsonString;
import static city.parking.TestUtils.prepareDummyParkingProcess;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ParkingProcessControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ParkingProcessRepository parkingProcessRepository;

    @Test
    public void whenStartingRegularParkingProcess_thenParkingProcessCreated() throws Exception {
        ParkingProcess process = new ParkingProcess();
        process.setMeterId(1);

        String processJson = asJsonString(process);

        mockMvc.perform(post("/parking-processes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(processJson))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("ONGOING")));
    }

    @Test
    public void givenOneOngoingParkingProcess_whenGetOngoingParkingProcesses_thenStatus200ParkingProcessReturned() throws Exception {
        ParkingProcess process = prepareDummyParkingProcess(LocalDateTime.now(), null, ParkingProcess.Stage.ONGOING, false);
        parkingProcessRepository.save(process);

        mockMvc.perform(get("/parking-processes")
                .param("stage", ParkingProcess.Stage.ONGOING.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].meterId", is(process.getMeterId())));
    }

    @Test
    public void givenOneOngoingParkingProcessForDisabled_whenGetStopParkingAndGetParkingCost_thenStatus200CostReturned() throws Exception {
        Integer hoursDiff = 3;
        Double parkingCostInPLN = 7.28;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime parkingStartTime = now.minusHours(hoursDiff);

        //given
        //a parking process started hoursDiff+1 hours ago
        ParkingProcess process = prepareDummyParkingProcess(parkingStartTime, null, ParkingProcess.Stage.ONGOING, true);
        process = parkingProcessRepository.save(process);

        //when
        //stop that parking process
        ParkingProcessPartialUpdateRequest updateRequest = new ParkingProcessPartialUpdateRequest();
        updateRequest.setParkingMeterToBeStopped(true);
        String updateRequestJson = asJsonString(updateRequest);

        mockMvc.perform(patch("/parking-processes/{processId}", process.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequestJson))
                .andExpect(status().isSeeOther());

        //get parking cost
        //then
        //parking cost is as calculated before
        mockMvc.perform(get("/parking-processes/{processId}/costs", process.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount", is(parkingCostInPLN)));
    }
}

