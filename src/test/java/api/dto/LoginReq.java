package api.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LoginReq {
    private String login;
    private String password;

    public LoginReq() {
    }

    public static LoginReq fromCourier(CourierReq courier) {
        return new LoginReq(courier.getLogin(), courier.getPassword());
    }
}