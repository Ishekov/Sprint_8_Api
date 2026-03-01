package tests.courier;

import api.dto.CourierReq;
import api.dto.LoginReq;
import api.utils.TestBase;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Удаление курьера")
public class CourierDelTest extends TestBase {
    private static final String CREATE_PATH = "/api/v1/courier/";
    private static final String LOGIN_PATH = "/api/v1/courier/login/";
    private static final String DELETE_PATH = "/api/v1/courier/";

    private CourierReq testCourier;
    private String userId;

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
    @DisplayName("Удалить существующего")
    public void deleteExisting() {
        testCourier = CourierReq.getRandom();
        create(testCourier);

        ValidatableResponse loginResp = login(LoginReq.fromCourier(testCourier));
        userId = loginResp.extract().path("id").toString();
        loginResp.assertThat()
                .statusCode(200)
                .body("id", notNullValue());

        ValidatableResponse delResp = delete(userId);
        delResp.assertThat()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Удалить без id")
    public void deleteNoId() {
        ValidatableResponse resp = delete("");
        resp.assertThat()
                .statusCode(404)
                .body("message", equalTo("Not Found."));
    }

    @Test
    @DisplayName("Удалить несуществующего")
    public void deleteWrongId() {
        ValidatableResponse resp = delete("0");
        resp.assertThat()
                .statusCode(404)
                .body("message", equalTo("Курьера с таким id нет."));
    }
}