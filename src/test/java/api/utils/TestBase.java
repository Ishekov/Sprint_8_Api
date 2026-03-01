package api.utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class TestBase {
    protected static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    protected static final String FIRST_NAME = "Илья";
    protected static final String LAST_NAME = "Ишеков";
    protected static final String ADDRESS = "г. Москва, ул. Ленина, д.11";
    protected static final String METRO = "Черкизовская";
    protected static final String PHONE = "89385567832";
    protected static final String DELIVERY = "2026-01-07T00:00:00.000Z";
    protected static final String NOTE = "Позвонить!";
    protected static final String BLACK = "black";
    protected static final String GREY = "grey";
    protected static final int RENT_DAYS = 1;

    protected RequestSpecification getReqSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }
}