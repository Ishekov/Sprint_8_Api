package api.dto;

public class LoginReq {
    private String login;
    private String password;

    public LoginReq(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public LoginReq() {}

    public static LoginReq fromCourier(CourierReq courier) {
        return new LoginReq(courier.getLogin(), courier.getPassword());
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}