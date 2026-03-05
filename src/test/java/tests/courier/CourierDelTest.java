package tests.courier;

import api.dto.CourierApi;
import api.dto.CourierReq;
import api.dto.LoginReq;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Удаление курьера")
public class CourierDelTest {
    private final CourierApi courierApi = new CourierApi();
    private CourierReq testCourier;
    private String userId;

    @Test
    @DisplayName("Удалить существующего")
    public void deleteExisting() {
        testCourier = CourierReq.getRandom();
        courierApi.create(testCourier);
                ValidatableResponse loginResp = courierApi.login(LoginReq.fromCourier(testCourier));
        userId = loginResp.extract().path("id").toString();
        loginResp.assertThat()
                .statusCode(SC_OK) //SC_OK = 200
                .body("id", notNullValue());

        ValidatableResponse delResp = courierApi.delete(userId);
        delResp.assertThat()
                .statusCode(SC_OK) //SC_OK = 200
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Удалить без id")
    public void deleteNoId() {
        ValidatableResponse resp = courierApi.delete("");
        resp.assertThat()
                .statusCode(SC_NOT_FOUND) //SC_NOT_FOUND = 404
                .body("message", equalTo("Not Found."));
    }

    @Test
    @DisplayName("Удалить несуществующего")
    public void deleteWrongId() {
        ValidatableResponse resp = courierApi.delete("0");
        resp.assertThat()
                .statusCode(SC_NOT_FOUND) //SC_NOT_FOUND = 404
                .body("message", equalTo("Курьера с таким id нет."));
    }
}