package PizzaApp.Model.PizzaModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of Basic menu
 */
public enum MenuPizza {
    MARGHERITA("Margherita", 0, new ArrayList<>() {{
        add(PizzaIngredients.DOUGH);
        add(PizzaIngredients.TOMATO_SAUCE);
        add(PizzaIngredients.CHEESE);
    }}),
    PEPPERONI("Peperoni", 1, new ArrayList<>() {{
        add(PizzaIngredients.DOUGH);
        add(PizzaIngredients.TOMATO_SAUCE);
        add(PizzaIngredients.CHEESE);
        add(PizzaIngredients.PEPPERONI);
    }}), CAPRICCIOSA("Capricciosa", 2, new ArrayList<>() {{
        add(PizzaIngredients.DOUGH);
        add(PizzaIngredients.TOMATO_SAUCE);
        add(PizzaIngredients.CHEESE);
        add(PizzaIngredients.HAM);
        add(PizzaIngredients.MUSHROOMS);
    }}), DIAVOLA("Diavola", 3, new ArrayList<>() {{
        add(PizzaIngredients.DOUGH);
        add(PizzaIngredients.TOMATO_SAUCE);
        add(PizzaIngredients.MOZZARELLA);
        add(PizzaIngredients.FRESH_PEPPERS);
        add(PizzaIngredients.BLACK_OLIVES);
        add(PizzaIngredients.GREEN_OLIVES);
    }}), FUNGHI("Funghi", 4, new ArrayList<>() {{
        add(PizzaIngredients.DOUGH);
        add(PizzaIngredients.TOMATO_SAUCE);
        add(PizzaIngredients.MOZZARELLA);
        add(PizzaIngredients.MUSHROOMS);
        add(PizzaIngredients.WHITE_ONION);
        add(PizzaIngredients.GREEN_OLIVES);
    }}),
    CUSTOM("Custom", 5, new ArrayList<>() {{
        add(PizzaIngredients.DOUGH);
        add(PizzaIngredients.TOMATO_SAUCE);
        add(PizzaIngredients.CHEESE);
    }});

    private final String name;
    private int value;
    private final List<PizzaIngredients> pizzaIngredients;

    MenuPizza(String name, int value, ArrayList<PizzaIngredients> pizzaIngredients) {
        this.name = name;
        this.value = value;
        this.pizzaIngredients = new ArrayList<>(pizzaIngredients);
    }

    MenuPizza(String name, ArrayList<PizzaIngredients> pizzaIngredients) {
        this.name = name;
        this.pizzaIngredients = pizzaIngredients;
    }

    public String getName() {

        return name;
    }

    public int getValue() {
        return value;
    }

    public List<PizzaIngredients> getSkladniki() {
        return pizzaIngredients;
    }

    public String printIngredients() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (PizzaIngredients pizzaIngredients1 : pizzaIngredients) {
            sb.append(",");
            sb.append(pizzaIngredients1.getDescription());
        }
        if (getName().equalsIgnoreCase("Custom")) {
            sb.append(" + your 6 ingredients");
        }
        sb.append("]");
        return sb.toString().replaceFirst(",", "");
    }

    public static MenuPizza fromDescription(int value) {
        MenuPizza[] values = values();
        for (MenuPizza basicPizza : values) {
            if (basicPizza.getValue() == (value))
                return basicPizza;
        }
        throw new RuntimeException("Wrong value ");
    }

    @Override
    public String toString() {
        return name + " ";
    }

    public Pizza toPizza(PizzaSizes size) {
        return new Pizza(name, pizzaIngredients, size);
    }
}
