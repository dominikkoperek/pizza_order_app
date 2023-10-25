package PizzaApp.app;

import PizzaApp.app.LoginApp.AccountControler;

public class PizzaAppMain {
    public static void main(String[] args) {
        AccountControler accBuilder = new AccountControler();
        accBuilder.loop();
    }
}
