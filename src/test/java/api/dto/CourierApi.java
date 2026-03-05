package api.dto;

import api.utils.TestBase;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierApi extends TestBase {
    private static final String CREATE_PATH = "/api/v1/courier/";
    private static final String LOGIN_PATH = "/api/v1/courier/login/";
    private static final String DELETE_PATH = "/api/v1/courier/";

    @Step("Создать курьера")
    public ValidatableResponse create(CourierReq data) {
        return given().log().all()
                .spec(getReqSpec())
                .body(data)
                .when()
                .post(CREATE_PATH)
                .then();
    }
    @Step("Войти в систему")
    public ValidatableResponse login(LoginReq data) {
        return given().log().all()
                .spec(getReqSpec())
                .body(data)
                .when()
                .post(LOGIN_PATH)
                .then().log().all();
    }
    @Step("Удалить курьера")
    public ValidatableResponse delete(String id) {
        return given()
                .spec(getReqSpec())
                .when()
                .delete(DELETE_PATH + id)
                .then().log().all();
    }
}
