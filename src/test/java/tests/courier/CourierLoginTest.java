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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Авторизация курьера")
public class CourierLoginTest extends TestBase {
    private static final String CREATE_PATH = "/api/v1/courier/";
    private static final String LOGIN_PATH = "/api/v1/courier/login/";
    private static final String DELETE_PATH = "/api/v1/courier/";

    private CourierReq existCourier;
    private String userId;

    @BeforeEach
    public void init() {
        existCourier = CourierReq.getRandom();
        create(existCourier);
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
    @DisplayName("Войти с полными данными")
    public void loginFull() {
        ValidatableResponse resp = login(LoginReq.fromCourier(existCourier));
        userId = resp.extract().path("id").toString();
        resp.assertThat()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Войти без логина")
    public void loginNoLogin() {
        existCourier.setLogin("");
        ValidatableResponse resp = login(LoginReq.fromCourier(existCourier));
        resp.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Войти без пароля")
    public void loginNoPass() {
        existCourier.setPassword("");
        ValidatableResponse resp = login(LoginReq.fromCourier(existCourier));
        resp.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Войти без логина и пароля")
    public void loginNoData() {
        existCourier.setLogin("");
        existCourier.setPassword("");
        ValidatableResponse resp = login(LoginReq.fromCourier(existCourier));
        resp.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Войти с неверным логином")
    public void loginWrongLogin() {
        existCourier.setLogin("null");
        ValidatableResponse resp = login(LoginReq.fromCourier(existCourier));
        resp.assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Войти с неверным паролем")
    public void loginWrongPassword() {
        String correctPassword = existCourier.getPassword();
        existCourier.setPassword(correctPassword + "wrong");
        ValidatableResponse resp = login(LoginReq.fromCourier(existCourier));
        resp.assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
        existCourier.setPassword(correctPassword);
        ValidatableResponse loginResp = login(LoginReq.fromCourier(existCourier));
        userId = loginResp.extract().path("id").toString();
    }

    @AfterEach
    public void cleanup() {
        if (userId != null) {
            delete(userId);
        }
    }
}