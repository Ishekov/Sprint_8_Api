package tests.order;

import api.dto.OrderApi;
import api.dto.OrderReq;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.apache.http.HttpStatus.*;
import static api.utils.TestBase.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class OrderCheckTest {
    private final OrderApi orderApi = new OrderApi();
    private String orderTrack;
    private static Stream<Arguments> orderData() {
        return Stream.of(
                Arguments.of(FIRST_NAME, LAST_NAME, ADDRESS, METRO, PHONE, RENT_DAYS, DELIVERY, NOTE, List.of(BLACK))
        );
    }

    @ParameterizedTest
    @MethodSource("orderData")
    @DisplayName("Проверка данных заказа")
    public void checkAllFields(String fName, String lName, String addr,
                               String metro, String phone, int days,
                               String date, String note, List<String> colors) {
        OrderReq order = new OrderReq(fName, lName, addr, metro, phone, days, date, note, colors);
        ValidatableResponse resp = orderApi.createOrder(order);
        resp.assertThat()
                .statusCode(SC_CREATED) //SC_CREATED = 201
                .body("track", notNullValue());

        orderTrack = resp.extract().path("track").toString();
        ValidatableResponse trackResp = orderApi.getOrder(orderTrack);
        trackResp.assertThat()
                .statusCode(SC_OK) //SC_OK = 200
                .body("order.firstName", equalTo(fName))
                .body("order.lastName", equalTo(lName))
                .body("order.address", equalTo(addr))
                .body("order.metroStation", equalTo(metro))
                .body("order.phone", equalTo(phone))
                .body("order.rentTime", equalTo(days))
                .body("order.deliveryDate", equalTo(date))
                .body("order.comment", equalTo(note))
                .body("order.color", equalTo(colors));
    }

    @AfterEach
    public void cleanup() {
        if (orderTrack != null) {
            orderApi.cancelOrder(orderTrack);
        }
    }
}