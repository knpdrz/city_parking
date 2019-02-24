package city.parking;

import com.fasterxml.jackson.databind.ObjectMapper;

class TestUtils {
    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
