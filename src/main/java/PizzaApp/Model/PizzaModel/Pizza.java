package PizzaApp.Model.PizzaModel;

import java.util.List;

public class Pizza {

    public static final byte MAX_PIZZA_INGREDIENTS = 9;
    public static final char CURRENCY = '\u20AC';
    private String name;
    private List<PizzaIngredients> ingredients;
    private PizzaSizes size;


    public Pizza(String name, List<PizzaIngredients> ingredients, PizzaSizes size) {
        this.name = name;
        this.ingredients = ingredients;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PizzaIngredients> getIngredients() {
        return ingredients;
    }


    public void setIngredients(List<PizzaIngredients> ingredients) {
        this.ingredients = ingredients;
    }

    public PizzaSizes getSize() {
        return size;
    }

    public void setSize(PizzaSizes size) {
        this.size = size;
    }

    public String getPizzaIngredients() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (PizzaIngredients pizzaIngredients1 : ingredients) {
            sb.append(",");
            sb.append(pizzaIngredients1.getDescription());

        }
        sb.append("]");
        return sb.toString().replaceFirst(",", "");
    }

    public double calcPizzaPrice() {
        double price = 0;
        List<PizzaIngredients> ingredients1 = getIngredients();
        for (PizzaIngredients pizzaIngredients : ingredients1) {
            if (size.equals(PizzaSizes.SMALL)) {
                price += pizzaIngredients.getPriceSmall();
            } else if (size.equals(PizzaSizes.MEDIUM)) {
                price += pizzaIngredients.getPriceMedium();
            } else if (size.equals(PizzaSizes.BIG)) {
                price += pizzaIngredients.getPriceBig();
            }
        }
        return price;
    }

    public String getPizzaName() {
        MenuPizza[] values = MenuPizza.values();
        for (MenuPizza value : values) {
            if (ingredients.equals(value.getSkladniki())) {
                return value.getName();
            }
        }
        return "Custom";
    }

    @Override
    public String toString() {
        return getPizzaName() + " " + size + " " + getPizzaIngredients();
    }
}



