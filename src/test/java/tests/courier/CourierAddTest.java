package tests.courier;

import api.dto.CourierApi;
import api.dto.CourierReq;
import api.dto.LoginReq;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Создание курьера")
public class CourierAddTest {
    private final CourierApi courierApi = new CourierApi();
    private CourierReq newCourier;
    private String userId;

    @BeforeEach
    public void init() {
        newCourier = CourierReq.getRandom();
    }

    @Test
    @DisplayName("Создать полные данные")
    public void createFullData() {
        ValidatableResponse resp = courierApi.create(newCourier);
        resp.assertThat()
                .statusCode(SC_CREATED) //SC_CREATED = 201
                .body("ok", equalTo(true));

        ValidatableResponse loginResp = courierApi.login(LoginReq.fromCourier(newCourier));
        userId = loginResp.extract().path("id").toString();
    }

    @Test
    @DisplayName("Создать без имени")
    public void createNoName() {
        newCourier.setName("");
        ValidatableResponse resp = courierApi.create(newCourier);
        resp.assertThat()
                .statusCode(SC_CREATED) //SC_CREATED = 201
                .body("ok", equalTo(true));

        ValidatableResponse loginResp = courierApi.login(LoginReq.fromCourier(newCourier));
        userId = loginResp.extract().path("id").toString();
    }

    @Test
    @DisplayName("Создать дубль")
    public void createDuplicate() {
        courierApi.create(newCourier);
        ValidatableResponse resp = courierApi.create(newCourier);
        resp.assertThat()
                .statusCode(SC_CONFLICT) //SC_CONFLICT = 409
                .body("message", equalTo("Этот логин уже используется"));

        ValidatableResponse loginResp = courierApi.login(LoginReq.fromCourier(newCourier));
        userId = loginResp.extract().path("id").toString();
    }

    @Test
    @DisplayName("Создать без пароля")
    public void createNoPass() {
        newCourier.setPassword("");
        ValidatableResponse resp = courierApi.create(newCourier);
        resp.assertThat()
                .statusCode(SC_BAD_REQUEST) //SC_BAD_REQUEST = 400
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создать без логина")
    public void createNoLogin() {
        newCourier.setLogin("");
        ValidatableResponse resp = courierApi.create(newCourier);
        resp.assertThat()
                .statusCode(SC_BAD_REQUEST) //SC_BAD_REQUEST = 400
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создать пустые поля")
    public void createEmpty() {
        newCourier.setLogin("");
        newCourier.setPassword("");
        newCourier.setName("");
        ValidatableResponse resp = courierApi.create(newCourier);
        resp.assertThat()
                .statusCode(SC_BAD_REQUEST) //SC_BAD_REQUEST = 400
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @AfterEach
    public void cleanup() {
        if (userId != null) {
            courierApi.delete(userId);
        }
    }
}