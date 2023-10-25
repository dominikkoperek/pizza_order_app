package PizzaApp.app.LoginApp;

import PizzaApp.Model.LoginModel.Account;
import PizzaApp.Model.LoginModel.DataBase;
import PizzaApp.app.PizzaOrderApp.OrderMainLoop;
import PizzaApp.exception.LoginOccupatiedException;
import PizzaApp.io.file.FileManager;
import PizzaApp.exception.ImportException;
import PizzaApp.io.DataReader;
import PizzaApp.io.MessagePrinter;
import java.util.*;

/**
 * This class is responsible for reading a file with accounts (accounts.obj).
 * If this file doesn't exist, the class creates a new one.
 * Here you will find all the logic behind the login/registration system, such as checking if a login is already occupied
 * or if the username is the same as in the saved file.
 */
public class AccountControler {
    private MessagePrinter printer = new MessagePrinter();
    private DataReader dataReader = new DataReader();
    private DataBase dataBase;
    private FileManager fileManager;

    /**
     * Initializes the AccountController. Checks if the database file exists. If the file does not exist, it creates a new one.
     * If the file exists, it reads its data.
     */
    public AccountControler() {

        try {
            fileManager = new FileManager();
            dataBase = fileManager.importData();
            printer.printLine("Data loaded");
        } catch (ImportException e) {
            dataBase = new DataBase();
            printer.printLine("File not found! Created new db");
        }
    }

    /**
     * Main loop of the login/register system. It converts enum values to options and then uses them in a while loop.
     * The "EXIT" option is responsible for saving the file. If the process is terminated prematurely, new accounts won't be saved.
     */
    public void loop() {
        Option option = null;
        while (option != Option.EXIT) {
            printOptions();
            option = getOption();
            switch (option) {
                case REGISTER -> {
                    try {
                        register();
                    } catch (LoginOccupatiedException e) {
                        printer.printLine(e.getMessage());
                        loop();
                    }
                }
                case LOGIN -> login();
                case EXIT -> {
                    printer.printLine("Saving...");
                    fileManager.exportData(dataBase);
                }
                default -> printer.printLine("Wrong option");
            }
        }
    }

    /**
     * This method retrieves a login and password from the user via the getAccountForLogin method.
     * It then imports database accounts and checks if the provided login exists in the database.
     * If the login exists, the method checks if the provided password matches the password connected to the login in the database.
     * If the data is correct, it initiates the order menu loop and provides the account details to it.
     */
    private void login() {
        Account account = getAccountForLogin();
        Map<String, Account> accounts = dataBase.getAccounts();
        Set<String> keySet = accounts.keySet();
        if (isKeySetContainKey(account, keySet)) {
            printer.printLine("Login not exists");
        } else {
             Account accountDb = accounts.get(account.getLogin());
             if(account.getPassword().equals(accountDb.getPassword())){
             printer.printLine("Sucesfull login");
                 OrderMainLoop orderMainLoop = new OrderMainLoop();
                 orderMainLoop.mainPizzaLoop(account);
             }else {
                 printer.printLine("Wrong password");
             }
         }
    }

    /**
     * This method prompts the user to select an option and then converts the user's input into an Option object.
     *
     * @return Returns an Option object representing the selected option with a numerical value.
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
     * Registers a new account by obtaining a login and password from the user,
     * then checks if the login is available in the database. If the login is occupied,
     * an exception is thrown, and the account is not created.
     */
    private void register() {
        Account account = getAccountForRegister();
        boolean isLoginFree;
        Map<String, Account> accounts = dataBase.getAccounts();
        Set<String> keySet = accounts.keySet();
        if (isKeySetContainKey(account, keySet)) {
            isLoginFree = true;
        } else {
            throw new LoginOccupatiedException("Login occupied!");
        }
        if (isLoginFree || (accounts.size() < 1)) {
            printer.printLine("Account created!");
           dataBase.add(account);
        }
    }

    private static boolean isKeySetContainKey(Account account, Set<String> keySet) {
        return !keySet.contains(account.getKey());
    }

    /**
     * Retrieves the user's login and password, ensuring their correctness, and then creates an Account object for registration.
     *
     * This method relies on the registerGetLogin and registerGetPassword methods to validate the input.
     *
     * @return An Account object containing the login, password, and a key that matches the login.
     */
    private Account getAccountForRegister() {
        String login = registerGetLogin();
        String password = registerGetPassword();
        String key = login;
        return new Account(login, password, key);
    }
    /**
     * Retrieves the login and password from the user and creates an Account object for login.
     *
     * @return An Account object containing login, password, and a key that matches the login.
     */
    private Account getAccountForLogin() {
        printer.printLine("Type login");
        String login = dataReader.getString().trim();
        printer.printLine("Type password");
        String password = dataReader.getString().trim();
        String key = login;
        return new Account(login, password, key);
    }

    /**
     * This method get a password from user and checks its correctness. The password must be a minimum of 5 characters
     * in length and must not contain any whitespace characters.
     *
     * @return A String representing a valid password for storage in the database file.
     */
    private String registerGetPassword() {
        boolean passwordLenghtOk = false;
        String password = null;
        while (!passwordLenghtOk) {
            printer.printLine("Type password");
            password = dataReader.getString().trim();
            if (password.matches(".*\\s.*")) {
                printer.printLine("Password cannot  contain spacebars!");
            } else if (password.length() > 4) {
                passwordLenghtOk = true;
            } else {
                printer.printLine("Login must be at least 5 chars!");
            }
        }
        return password;
    }
    /**
     * This method get a login from user and checks its correctness. The login must be a minimum of 4 characters
     * in length and must not contain any whitespace characters.
     *
     * @return A String representing a valid login for storage in the database file.
     */
    private String registerGetLogin() {

        boolean loginLenghtOk = false;
        String login = null;
        while (!loginLenghtOk) {
            printer.printLine("Type login");
            login = dataReader.getString().trim();
            if (login.matches(".*\\s.*")) {
                printer.printLine("Login cannot  contain spacebars!");
            } else if (login.length() > 3) {
                loginLenghtOk = true;
            } else {
                printer.printLine("Login must be at least 4 chars!");
            }
        }
        return login;
    }


    private void printOptions() {
        printer.printLine("Chose Option");
        for (Option value : Option.values()) {
            printer.printLine(value.toString());
        }
    }

    /**
     * Enumeration of available options in the register menu.
     */
    private enum Option {
        EXIT(0, "-> Exit"),
        LOGIN(1, "-> Login"),
        REGISTER(2, "-> Register");


        private final int value;
        private final String describtion;

        Option(int value, String describtion) {
            this.value = value;
            this.describtion = describtion;
        }

        @Override
        public String toString() {
            return value + " " + describtion;
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
