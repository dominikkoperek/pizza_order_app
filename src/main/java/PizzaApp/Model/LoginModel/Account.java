package PizzaApp.Model.LoginModel;

import java.io.Serializable;
import java.util.Objects;

public class Account implements Serializable {
    public static final String DELIMITER =";" ;
    private String login;
    private String password;
    private String key;

    public Account(String login, String password, String key) {
        this.login = login;
        this.password = password;
        this.key = key;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return login+ DELIMITER +password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(login, account.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
