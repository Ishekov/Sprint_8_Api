package api.dto;

import api.utils.TestBase;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderApi extends TestBase {
    private static final String CREATE_PATH = "/api/v1/orders";
    private static final String LIST_PATH = "/api/v1/orders";
    private static final String CANCEL_PATH = "/api/v1/orders/cancel";
    private static final String TRACK_PATH = "/api/v1/orders/track";

    @Step("Получить список")
    public ValidatableResponse getList() {
        return given().log().all()
                .spec(getReqSpec())
                .when()
                .get(LIST_PATH)
                .then().log().all();
    }

    @Step("Создать заказ")
    public ValidatableResponse createOrder(OrderReq data) {
        return given().log().all()
                .spec(getReqSpec())
                .body(data)
                .when()
                .post(CREATE_PATH)
                .then().log().all();
    }
    @Step("Отменить заказ")
    public ValidatableResponse cancelOrder(String track) {
        return given().log().all()
                .spec(getReqSpec())
                .queryParam("track", track)
                .when()
                .put(CANCEL_PATH)
                .then().log().all();
    }

    @Step("Получить заказ")
    public ValidatableResponse getOrder(String track) {
        return given().log().all()
                .spec(getReqSpec())
                .queryParam("t", track)
                .get(TRACK_PATH)
                .then().log().all();
    }
}
