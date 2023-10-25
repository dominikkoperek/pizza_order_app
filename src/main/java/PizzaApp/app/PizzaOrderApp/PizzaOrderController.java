package PizzaApp.app.PizzaOrderApp;

import PizzaApp.Model.PizzaModel.MenuPizza;
import PizzaApp.Model.PizzaModel.Pizza;
import PizzaApp.Model.PizzaModel.PizzaIngredients;
import PizzaApp.Model.PizzaModel.PizzaSizes;
import PizzaApp.exception.NoSuchPizzaSize;
import PizzaApp.io.*;

import java.util.*;
import java.util.List;

/**
 * This class is responsible for managing the entire pizza order system, which includes adding pizzas to the cart,
 * customizing preset pizzas, creating custom pizzas, removing items from the cart, calculating the total price, and
 * providing comprehensive information to the user.
 */
public class PizzaOrderController {

    private List<Pizza> order = new ArrayList<>();
    private final MessagePrinter printer;
    private final DataReader dataReader;
    private static final MenuPizza[] PIZZA_NUMBERS = MenuPizza.values();
    private static final PizzaIngredients[] INGREDIENTS_NUMBERS = PizzaIngredients.values();
    private static final int CONFIRM_ORDER_CHOICE = 6;
    private static final int PRINT_ORDER_CHOICE = 7;
    private static final int WRONG_START_VALUE = -1;
    private static final int ACCEPT_INGREDIENTS_CHOICE = getIngredientsListWithOutDoughAndSouce();

    private static int getIngredientsListWithOutDoughAndSouce() {
        return PizzaIngredients.values().length - 1;
    }

    public PizzaOrderController(MessagePrinter printer, DataReader dataReader) {
        this.printer = printer;
        this.dataReader = dataReader;
    }

    /**
     * This method retrieves a user's choice, and based on the chosen number, it either creates a pizza
     * object or executes other options.
     */
    public void pizzaOrderStart() {
        int choice = WRONG_START_VALUE;
        while (choice != CONFIRM_ORDER_CHOICE) {
            printer.printLine("Hello chose your pizza!");
            printMenu();

            try {
                choice = dataReader.getInt();
            } catch (InputMismatchException e) {
                printer.printLine(">Wrong value");
                continue;
            }
            final int finalChoice = choice;
            final int[] pizzasNumber = getPizzasNumbers();

         /*
         check if pizza value from MenuPizza.java equals user choice
          if yes create  new Pizza object
         */
            if (Arrays.stream(pizzasNumber).anyMatch(c -> c == finalChoice)) {
                createPizzaFromInt(choice);
            } else {
                switch (choice) {
          /*
          else execute cases
          */
                    case CONFIRM_ORDER_CHOICE -> confirmOrder();
                    case PRINT_ORDER_CHOICE -> printOrder();
                    default -> printer.printLine(">No such option");
                }
            }
        }
    }

    /**
     * This method generates sequential numbers for the basic pizzas in the menu.
     *
     * @return The next available number for a pizza in the menu.
     */
    public int[] getPizzasNumbers() {
        int[] pizzasNumbers = new int[PIZZA_NUMBERS.length];
        int counter = 0;
        for (MenuPizza pizzaNumber : PIZZA_NUMBERS) {
            int value = pizzaNumber.getValue();
            pizzasNumbers[counter] = value;
            counter++;
        }
        return pizzasNumbers;
    }

    private void confirmOrder() {
        if (order.isEmpty()) {
            printer.printLine("Chose pizza to add!");
        } else printer.printLine("Order updated!");
    }

    private void printMenu() {
        MenuPizza[] values = MenuPizza.values();
        for (MenuPizza value : values) {
            printer.printLine(value.getValue() + ") " + value
                    + value.printIngredients() + PizzaIngredients.calcPizzaPriceForMenu(value));
        }
        printer.printLine(OrderPizzaOption.CONFIRM_ORDER.toString());
        printer.printLine(OrderPizzaOption.PRINT_ORDER.toString());
    }

    /**
     * This method allows the user to choose for customize a default menu pizza and prints the total price of the new pizza.
     *
     * @param pizza An object representing the pizza created by the user's choice.
     */
    private void customOrder(Pizza pizza) {
        printer.printLine("Dou you want to custom? (Maxium 7 idngredients!)");
        printer.printLine("Y/N");
        String choice = dataReader.getString();

        if (choice.equalsIgnoreCase("Y")) {
            doCustom(pizza);
            String format = String.format("%s (%.2f%s) +added to your order\n", pizza, pizza.calcPizzaPrice(),Pizza.CURRENCY);
            printer.printLine(format);

        } else if (choice.equalsIgnoreCase("N")) {
            String format = String.format("%s (%.2f%s) +added to your order\n", pizza, pizza.calcPizzaPrice(),Pizza.CURRENCY);
            printer.printLine(format);
        } else {
            throw new InputMismatchException("Wrong option (Y/N)");
        }
    }

    /**
     * This method represents the customization option. It displays available ingredients and allows the user
     * to type number to add ingredients to the pizza.
     *
     * @param pizza An object representing the pizza created by the user's choice.
     */
    private void doCustom(Pizza pizza) {
        int choice;
        do {
            if (pizza.getIngredients().size() < Pizza.MAX_PIZZA_INGREDIENTS) {
                printIngredients(pizza);
                choice = dataReader.getInt();

                final int finalChoice = choice;
                final int[] ingredientsValues = getIngredientsValues();

            /*
            check if ingredientsValues equals choice from user if yes add
            new ingredient from this value
            */
                if (Arrays.stream(ingredientsValues).anyMatch(c -> c == finalChoice)) {
                    customPizza(pizza, finalChoice);
                } else if (finalChoice == ACCEPT_INGREDIENTS_CHOICE) {
                    printer.printLine(">Ingredients confirmed!");
                } else {
                    printer.printLine(">No such ingredient");
                }
            } else {
                printer.printLine(">>>>>>>>>>>Max ingredients reached!");
                choice = ACCEPT_INGREDIENTS_CHOICE;
            }
        } while (choice != ACCEPT_INGREDIENTS_CHOICE);
    }

    /**
     * This method retrieves the values of all ingredients except for dough and tomato sauce.
     *
     * @return An array of ingredient values. These values are non-negative integers corresponding to the ingredients,
     * excluding dough and tomato sauce.
     */
    private int[] getIngredientsValues() {
        int[] ingredientsValue = new int[INGREDIENTS_NUMBERS.length];
        int counter = 0;
        for (PizzaIngredients ingredientsValues : INGREDIENTS_NUMBERS) {
            int value = ingredientsValues.getValue();
            ingredientsValue[counter] = value;
            counter++;
        }
        return Arrays.stream(ingredientsValue)
                .filter(c -> c > 0)
                .toArray();
    }

    /**
     * This method customizes a pizza by adding a selected ingredient to the Pizza object created from user input.
     *
     * @param pizza An object representing the pizza created by the user's choice.
     * @param choice An integer representing the user's ingredient selection.
     */
    private void customPizza(Pizza pizza, int choice) {
        List<PizzaIngredients> pizzaIngredientsList = new ArrayList<>(pizza.getIngredients());
        pizzaIngredientsList.add(PizzaIngredients.fromDescription(choice));
        pizza.setIngredients(pizzaIngredientsList);
        printInfoAboutAddedPizza(pizza, choice);
    }

    /**
     * Print info about Added pizza to cart
     * @param pizza An object representing the pizza created by the user's choice.
     * @param choice An integer representing the user's ingredient selection.
     */
    private void printInfoAboutAddedPizza(Pizza pizza, int choice) {
        printer.printLine(PizzaIngredients.fromDescription(choice).getDescription() + " ->Added +");
        if (pizza.getIngredients().size() < Pizza.MAX_PIZZA_INGREDIENTS) {
            String format = String.format((">> Pizza: %s (%.2f%s)  <<\n"), pizza, pizza.calcPizzaPrice(),Pizza.CURRENCY);
            printer.printLine(format);
        }
    }

    /**
     * This method is responsible for printing all available ingredients and their prices based on the selected pizza size.
     *
     * @param pizza An object representing the pizza created by the user's choice, including its size.
     */
    private void printIngredients(Pizza pizza) {
        printer.printLine("Chosen your additional ingredients for:" + pizza.getSize());
        PizzaIngredients[] values = PizzaIngredients.values();
        for (PizzaIngredients value : values) {
            if (value.equals(PizzaIngredients.DOUGH) || value.equals(PizzaIngredients.TOMATO_SAUCE)) {
                continue;
            }
            String format = String.format(("%d) %s (%s%s)"),
                    value.getValue(), value.getDescription(),
                    value.getPrice(pizza),
                    Pizza.CURRENCY);
            printer.printLine(format);
        }
        printer.printLine(ACCEPT_INGREDIENTS_CHOICE + ")---> ACCEPT");

    }

    /**
     * Prints the user's order details on the screen, including the selected pizza size, ingredients, individual ingredient prices,
     * and the total price of the entire order. If the order is empty, a special message is printed to inform the user.
     */
    public void printOrder() {
        if (order.isEmpty()) {
            printer.printLine("Your cart is empty:");
        } else {
            printer.printLine("Your order");
            int counter = 1;
            double totalValue = 0;
            for (Pizza pizza : order) {
                String format = String.format(("%d) %s %s %s %.2f%s"),
                        counter,
                        pizza.getPizzaName(),
                        pizza.getSize(),
                        pizza.getPizzaIngredients(),
                        pizza.calcPizzaPrice(),
                        Pizza.CURRENCY);
                printer.printLine(format);
                counter++;
                totalValue += pizza.calcPizzaPrice();
            }
            String format = String.format(("Total is: %.2f%s"), totalValue,Pizza.CURRENCY);
            printer.printLine(format);
        }
    }

    /**
     * Creates a new Pizza object based on the user's ingredient selection and invokes the customOrder method with the created pizza.
     *
     * @param choice An integer representing the user's pizza selection.
     */
    private void createPizzaFromInt(int choice) {
        MenuPizza basicPizza = MenuPizza.fromDescription(choice);
        chosePizzaSize();
        try {
            Pizza pizza = basicPizza.toPizza(createSizeFromInt());
            customOrder(pizza);
            order.add(pizza);

        } catch (NoSuchPizzaSize e) {
            printer.printLine(e.getMessage());
        } catch (InputMismatchException e) {
            printer.printLine(">Wrong value");
        }
    }

    private void chosePizzaSize() {
        printer.printLine("Type your pizza size!");
        printPizzaSizes();
    }

    private void printPizzaSizes() {
        PizzaSizes[] values = PizzaSizes.values();
        for (PizzaSizes value : values) {
            printer.printLine(value.getValue() + ") " + value.getDescribtion());
        }
    }

    /**
     * Reads an integer input from the user and creates a PizzaSizes object based on the user's input.
     *
     * @return A PizzaSizes object corresponding to the user's input.
     * @throws NoSuchPizzaSize if the user's input does not match any valid pizza size.
     */
    private PizzaSizes createSizeFromInt() {
        int choice = dataReader.getInt();
        PizzaSizes[] values = PizzaSizes.values();
        for (PizzaSizes value : values) {
            if (choice == value.getValue()) {
                return PizzaSizes.getSizeFromInt(choice);
            }
        }
        throw new NoSuchPizzaSize(">No such pizza size");
    }

    /**
     * This method is responsible for removing a pizza from the user's order based on user input.
     * If the order is empty, a special message is displayed. The user is prompted to select a pizza position to remove,
     * and they can choose to go back to the main menu. If a valid position is selected, the selected pizza is removed from the order.
     */
    public void editOrder() {
        printer.printLine("Chose position to remove");
        printOrder();
        int backOption = getNumberToBackOption();
        printer.printLine(backOption + "-> Go back");
        int choice = dataReader.getInt();
        if (choice == backOption) {
            printer.printLine("Order unchanged!");
        } else if (order.isEmpty()) {
            printer.printLine("Your cart is empty:");
        } else if (choice <= order.size()) {
            int pizzaToRemove = getPostionToRemoveFrom0(choice);
            printer.printLine("Position removed!");
            order.remove(pizzaToRemove);
        } else {
            printer.printLine(">Wrong value");
        }
    }

    private int getNumberToBackOption() {
        return order.size() + 1;
    }

    private static int getPostionToRemoveFrom0(int choice) {
        return choice - 1;
    }

    /**
     * Menu option enum
     */
    private enum OrderPizzaOption {
        CONFIRM_ORDER(CONFIRM_ORDER_CHOICE, "-> Confirm order"),
        PRINT_ORDER(PRINT_ORDER_CHOICE, "-> Print order");

        private final int value;
        private final String description;

        OrderPizzaOption(int value, String description) {
            this.value = value;
            this.description = description;
        }


        @Override
        public String toString() {
            return value + ") " + description;
        }


    }
}

