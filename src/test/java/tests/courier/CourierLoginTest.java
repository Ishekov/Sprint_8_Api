package tests.courier;

import static org.apache.http.HttpStatus.*;
import api.dto.CourierApi;
import api.dto.CourierReq;
import api.dto.LoginReq;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Авторизация курьера")
public class CourierLoginTest {
    private final CourierApi courierApi = new CourierApi();
    private CourierReq existCourier;
    private String userId;

    @BeforeEach
    public void init() {
        existCourier = CourierReq.getRandom();
        courierApi.create(existCourier);
    }

    @Test
    @DisplayName("Войти с полными данными")
    public void loginFull() {
        ValidatableResponse resp = courierApi.login(LoginReq.fromCourier(existCourier));
        userId = resp.extract().path("id").toString();
        resp.assertThat()
                .statusCode(SC_OK) //SC_OK = 200
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Войти без логина")
    public void loginNoLogin() {
        existCourier.setLogin("");
        ValidatableResponse resp = courierApi.login(LoginReq.fromCourier(existCourier));
        resp.assertThat()
                .statusCode(SC_BAD_REQUEST) //SC_BAD_REQUEST = 400
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Войти без пароля")
    public void loginNoPass() {
        existCourier.setPassword("");
        ValidatableResponse resp = courierApi.login(LoginReq.fromCourier(existCourier));
        resp.assertThat()
                .statusCode(SC_BAD_REQUEST)//SC_BAD_REQUEST = 400
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Войти без логина и пароля")
    public void loginNoData() {
        existCourier.setLogin("");
        existCourier.setPassword("");
        ValidatableResponse resp = courierApi.login(LoginReq.fromCourier(existCourier));
        resp.assertThat()
                .statusCode(SC_BAD_REQUEST) //SC_BAD_REQUEST = 400
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Войти с неверным логином")
    public void loginWrongLogin() {
        existCourier.setLogin("null");
        ValidatableResponse resp = courierApi.login(LoginReq.fromCourier(existCourier));
        resp.assertThat()
                .statusCode(SC_NOT_FOUND) //SC_NOT_FOUND = 404
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Войти с неверным паролем")
    public void loginWrongPassword() {
        String correctPassword = existCourier.getPassword();
        existCourier.setPassword(correctPassword + "wrong");
        ValidatableResponse resp = courierApi.login(LoginReq.fromCourier(existCourier));
        resp.assertThat()
                .statusCode(SC_NOT_FOUND) //SC_NOT_FOUND = 404
                .body("message", equalTo("Учетная запись не найдена"));
        existCourier.setPassword(correctPassword);
        ValidatableResponse loginResp = courierApi.login(LoginReq.fromCourier(existCourier));
        userId = loginResp.extract().path("id").toString();
    }

    @AfterEach
    public void cleanup() {
        if (userId != null) {
            courierApi.delete(userId);
        }
    }
}