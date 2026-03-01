package tests.order;

import api.dto.OrderReq;
import api.utils.TestBase;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Создание заказа")
public class OrderCreateTest extends TestBase {
    private static final String ORDER_PATH = "/api/v1/orders";
    private static final String CANCEL_PATH = "/api/v1/orders/cancel";
    private static final String TRACK_PATH = "/api/v1/orders/track";

    private String orderTrack;

    @Step("Создать заказ")
    private ValidatableResponse createOrder(OrderReq data) {
        return given().log().all()
                .spec(getReqSpec())
                .body(data)
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    @Step("Отменить заказ")
    private ValidatableResponse cancelOrder(String track) {
        return given().log().all()
                .spec(getReqSpec())
                .body(track)
                .when()
                .put(CANCEL_PATH)
                .then().log().all();
    }

    @Step("Получить заказ")
    private ValidatableResponse getOrder(String track) {
        return given().log().all()
                .spec(getReqSpec())
                .queryParam("t", track)
                .get(TRACK_PATH)
                .then().log().all();
    }

    private static Stream<Arguments> colorData() {
        return Stream.of(
                Arguments.of(FIRST_NAME, LAST_NAME, ADDRESS, METRO, PHONE, RENT_DAYS, DELIVERY, NOTE, List.of(BLACK)),
                Arguments.of(FIRST_NAME, LAST_NAME, ADDRESS, METRO, PHONE, RENT_DAYS, DELIVERY, NOTE, List.of(GREY)),
                Arguments.of(FIRST_NAME, LAST_NAME, ADDRESS, METRO, PHONE, RENT_DAYS, DELIVERY, NOTE, List.of(BLACK, GREY)),
                Arguments.of(FIRST_NAME, LAST_NAME, ADDRESS, METRO, PHONE, RENT_DAYS, DELIVERY, NOTE, List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("colorData")
    @DisplayName("Создать с разными цветами")
    public void createWithColors(String fName, String lName, String addr,
                                 String metro, String phone, int days,
                                 String date, String note, List<String> colors) {
        OrderReq order = new OrderReq(fName, lName, addr, metro, phone, days, date, note, colors);
        ValidatableResponse resp = createOrder(order);
        resp.assertThat()
                .statusCode(201)
                .body("track", notNullValue());

        orderTrack = resp.extract().path("track").toString();
        ValidatableResponse trackResp = getOrder(orderTrack);
        trackResp.assertThat()
                .statusCode(200)
                .body("order.color", equalTo(colors));
    }

    @AfterEach
    public void cleanup() {
        if (orderTrack != null) {
            cancelOrder(orderTrack);
        }
    }
}