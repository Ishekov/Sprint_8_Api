package tests.order;

import api.utils.TestBase;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Получение заказа")
public class OrderGetTest extends TestBase {
    private static final String TRACK_PATH = "/api/v1/orders/track";

    @Step("Получить заказ")
    private ValidatableResponse getOrder(String track) {
        return given().log().all()
                .spec(getReqSpec())
                .queryParam("t", track)
                .get(TRACK_PATH)
                .then().log().all();
    }

    @Test
    @DisplayName("Несуществующий номер")
    public void getWrongTrack() {
        String track = "7";
        ValidatableResponse resp = getOrder(track);
        resp.assertThat()
                .statusCode(404)
                .body("message", equalTo("Заказ не найден"));
    }

    @Test
    @DisplayName("Без номера")
    public void getNoTrack() {
        String track = "";
        ValidatableResponse resp = getOrder(track);
        resp.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для поиска"));
    }
}