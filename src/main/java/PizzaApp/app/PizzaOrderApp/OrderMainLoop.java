package PizzaApp.app.PizzaOrderApp;

import PizzaApp.Model.LoginModel.Account;
import PizzaApp.io.*;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 * This class represents the main order options loop. It is responsible for displaying available options,
 * retrieving a number from the user, mapping it to an enum Option, and tracking the user's choice.
 */
public class OrderMainLoop {

    private MessagePrinter printer = new MessagePrinter();
    private DataReader dataReader = new DataReader();
    private PizzaOrderController pizzaOrderController = new PizzaOrderController(printer, dataReader);

    /**
     * The main loop responsible for executing the correct option by obtaining a number from the user via the getOption() method.
     *
     * @param account An object representing the logged-in user.
     */
    public void mainPizzaLoop(Account account) {
        Option option = null;
        while (option != Option.LOG_OFF) {
            printOptions(account);
            option = getOption();

            switch (option) {
                case LOG_OFF -> printer.printLine("Log off");
                case ORDER -> pizzaOrderController.pizzaOrderStart();
                case CHECK_ORDER -> pizzaOrderController.printOrder();
                case EDIT_ORDER -> pizzaOrderController.editOrder();
                case ACCOUNT_INFO -> accountSettings(account);
                default -> printer.printLine(">No such option");
            }
        }
    }

    private void accountSettings(Account account) {
        printer.printLine("Login: "+ account.getLogin());
        printer.printLine("Password: "+ account.getPassword());
    }

    private void printOptions(Account account) {
        printer.printLine("Hello " + account.getLogin() + "!");
        Option[] values = Option.values();
        for (Option value : values) {
            printer.printLine(value);
        }
    }

    /**
     * Retrieves an enum Option based on a user-provided integer.
     * This method handles two exceptions, InputMismatchException and NoSuchElementException.
     *
     * @return The corresponding Option value.
     */
    public Option getOption() {
        boolean optionOk = false;
        Option option = null;
        while (!optionOk) {
            try {
                option = Option.createFromInt(dataReader.getInt());
                optionOk = true;
            } catch (InputMismatchException e) {
                printer.printLine("Wrong value");

            } catch (NoSuchElementException e) {
                printer.printLine(e.getMessage());
            }
        }
        return option;
    }

    /**
     * Available options stored as an enum with descriptions.
     */
    private enum Option {
        LOG_OFF(0, "-> Log off"),
        ORDER(1, "-> Order"),
        CHECK_ORDER(2, "-> Check order"),
        EDIT_ORDER(3, "-> Edit order"),
        ACCOUNT_INFO(4, "-> Account info");

        private final int value;
        private final String description;

        Option(int value, String description) {
            this.value = value;
            this.description = description;
        }

        @Override
        public String toString() {
            return value + " " + description;
        }

        /**
         * Creates an enum Option based on a user-provided integer.
         *
         * @param option The number provided by the user.
         * @return The corresponding Option value.
         * @throws NoSuchElementException If the user provides a number that does not correspond to a valid option,
         * an exception is thrown.
         */
        static Option createFromInt(int option) throws NoSuchElementException {
            try {
                return Option.values()[option];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchElementException("Wrong option " + option);
            }
        }

    }
}

