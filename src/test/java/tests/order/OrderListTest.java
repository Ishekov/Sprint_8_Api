package tests.order;

import api.dto.OrderApi;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.apache.http.HttpStatus.*;

import static org.hamcrest.CoreMatchers.notNullValue;


public class OrderListTest  {
    private final OrderApi orderApi = new OrderApi();
    @Test
    @DisplayName("Получить все заказы")
    @Description("Проверка ответа на возвращение списка заказов")
    public void getAllOrders() {
        ValidatableResponse resp = orderApi.getList();
        resp.assertThat().log().all()
                .statusCode(SC_OK) //SC_OK = 200
                .body("orders", notNullValue());
    }
}