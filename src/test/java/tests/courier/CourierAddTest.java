package tests.courier;

import api.dto.CourierReq;
import api.dto.LoginReq;
import api.utils.TestBase;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Создание курьера")
public class CourierAddTest extends TestBase {
    private static final String CREATE_PATH = "/api/v1/courier/";
    private static final String LOGIN_PATH = "/api/v1/courier/login/";
    private static final String DELETE_PATH = "/api/v1/courier/";

    private CourierReq newCourier;
    private String userId;

    @BeforeEach
    public void init() {
        newCourier = CourierReq.getRandom();
    }

    @Step("Создать курьера")
    private ValidatableResponse create(CourierReq data) {
        return given().log().all()
                .spec(getReqSpec())
                .body(data)
                .when()
                .post(CREATE_PATH)
                .then();
    }

    @Step("Войти в систему")
    private ValidatableResponse login(LoginReq data) {
        return given().log().all()
                .spec(getReqSpec())
                .body(data)
                .when()
                .post(LOGIN_PATH)
                .then().log().all();
    }

    @Step("Удалить курьера")
    private ValidatableResponse delete(String id) {
        return given()
                .spec(getReqSpec())
                .when()
                .delete(DELETE_PATH + id)
                .then().log().all();
    }

    @Test
    @DisplayName("Создать полные данные")
    public void createFullData() {
        ValidatableResponse resp = create(newCourier);
        resp.assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));

        ValidatableResponse loginResp = login(LoginReq.fromCourier(newCourier));
        userId = loginResp.extract().path("id").toString();
    }

    @Test
    @DisplayName("Создать без имени")
    public void createNoName() {
        newCourier.setName("");
        ValidatableResponse resp = create(newCourier);
        resp.assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));

        ValidatableResponse loginResp = login(LoginReq.fromCourier(newCourier));
        userId = loginResp.extract().path("id").toString();
    }

    @Test
    @DisplayName("Создать дубль")
    public void createDuplicate() {
        create(newCourier);
        ValidatableResponse resp = create(newCourier);
        resp.assertThat()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));

        ValidatableResponse loginResp = login(LoginReq.fromCourier(newCourier));
        userId = loginResp.extract().path("id").toString();
    }

    @Test
    @DisplayName("Создать без пароля")
    public void createNoPass() {
        newCourier.setPassword("");
        ValidatableResponse resp = create(newCourier);
        resp.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создать без логина")
    public void createNoLogin() {
        newCourier.setLogin("");
        ValidatableResponse resp = create(newCourier);
        resp.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создать пустые поля")
    public void createEmpty() {
        newCourier.setLogin("");
        newCourier.setPassword("");
        newCourier.setName("");
        ValidatableResponse resp = create(newCourier);
        resp.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @AfterEach
    public void cleanup() {
        if (userId != null) {
            delete(userId);
        }
    }
}