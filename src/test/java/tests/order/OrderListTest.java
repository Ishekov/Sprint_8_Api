package tests.order;

import api.utils.TestBase;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Список заказов")
public class OrderListTest extends TestBase {
    private static final String LIST_PATH = "/api/v1/orders";

    @Step("Получить список")
    private ValidatableResponse getList() {
        return given().log().all()
                .spec(getReqSpec())
                .when()
                .get(LIST_PATH)
                .then().log().all();
    }

    @Test
    @DisplayName("Получить все заказы")
    public void getAllOrders() {
        ValidatableResponse resp = getList();
        resp.assertThat()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}