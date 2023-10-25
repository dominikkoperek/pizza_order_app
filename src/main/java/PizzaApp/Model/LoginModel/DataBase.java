package PizzaApp.Model.LoginModel;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataBase implements Serializable {

    private Map<String,Account> accounts = new LinkedHashMap<>();
    public void add (Account acc) {
        accounts.put(acc.getLogin(),acc);
    }

    public void setAccounts(Map<String,Account> accounts) {
        this.accounts = accounts;
    }

    public Map<String,Account> getAccounts (){
        return accounts;
    }
}
