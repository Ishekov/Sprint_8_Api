package api.dto;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierReq {
    private String login;
    private String password;
    private String name;

    public CourierReq(String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }

    public CourierReq() {
    }

    public static CourierReq getRandom() {
        return new CourierReq(
                RandomStringUtils.randomAlphabetic(12),
                RandomStringUtils.randomAlphabetic(6),
                RandomStringUtils.randomAlphabetic(10)
        );
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}