package PizzaApp.exception;

public class NoSuchPizzaSize extends RuntimeException{
    public NoSuchPizzaSize(String message) {
        super(message);
    }
}
