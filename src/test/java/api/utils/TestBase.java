package api.utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class TestBase {
    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    public static final String FIRST_NAME = "Илья";
    public static final String LAST_NAME = "Ишеков";
    public static final String ADDRESS = "г. Москва, ул. Ленина, д.11";
    public static final String METRO = "Черкизовская";
    public static final String PHONE = "89385567832";
    public static final String DELIVERY = "2026-01-07T00:00:00.000Z";
    public static final String NOTE = "Позвонить!";
    public static final String BLACK = "black";
    public static final String GREY = "grey";
    public static final int RENT_DAYS = 1;

    protected RequestSpecification getReqSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }
}