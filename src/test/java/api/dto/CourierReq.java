package api.dto;

import org.apache.commons.lang3.RandomStringUtils;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class CourierReq {
    private String login;
    private String password;
    private String name;

    public CourierReq() {
    }

    public static CourierReq getRandom() {
        return new CourierReq(
                RandomStringUtils.randomAlphabetic(12),
                RandomStringUtils.randomAlphabetic(6),
                RandomStringUtils.randomAlphabetic(10)
        );
    }
}