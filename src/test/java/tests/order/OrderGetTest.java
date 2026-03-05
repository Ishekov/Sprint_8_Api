package tests.order;

import api.dto.OrderApi;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Получение заказа")
public class OrderGetTest {
    private final OrderApi orderApi = new OrderApi();

    @Test
    @DisplayName("Несуществующий номер")
    public void getWrongTrack() {
        String track = "7";
        ValidatableResponse resp = orderApi.getOrder(track);
        resp.assertThat()
                .statusCode(SC_NOT_FOUND) //SC_NOT_FOUND = 404
                .body("message", equalTo("Заказ не найден"));
    }

    @Test
    @DisplayName("Без номера")
    public void getNoTrack() {
        String track = "";
        ValidatableResponse resp = orderApi.getOrder(track);
        resp.assertThat()
                .statusCode(SC_BAD_REQUEST) //SC_BAD_REQUEST = 400
                .body("message", equalTo("Недостаточно данных для поиска"));
    }
}