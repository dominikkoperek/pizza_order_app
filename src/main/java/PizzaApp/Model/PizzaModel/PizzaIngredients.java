package PizzaApp.Model.PizzaModel;

import java.util.List;

public enum PizzaIngredients {

    DOUGH(14, 19, 24, "dough",-1),
    TOMATO_SAUCE(1, 1, 1, "tomato sauce",0),
    CHEESE(6, 7, 8, "cheese",1),
    BLUE_CHEESE(6, 7, 8, "blue cheese",2),
    MOZZARELLA(6.5, 7.5, 8, "mozzarella",3),
    TOMATO(2, 3, 4, "tomato",4),
    COCKTAIL_TOMATO(4, 5, 6, "cocktail tomato",5),
    WHITE_ONION(1, 2, 3, "white onion",6),
    RED_ONION(1, 2, 3, "red onion",7),
    BROCCOLI(4, 5, 6, "broccoli",8),
    ARUGULA(4, 5, 6, "arugula",9),
    CANNED_CORN(3, 4, 5, "canned corn",10),
    BLACK_OLIVES(4, 5, 6, "black olives",11),
    GREEN_OLIVES(4, 5, 6, "green olives",12),
    PICKLED_PEPPERS(3, 4, 5, "pickled peppers",13),
    JALAPENO(3, 3.5, 4.5, "jalapeno",14),
    FRESH_PEPPERS(5, 5.5, 6, "fresh peppers",15),
    SPINACH(2.5, 3.5, 4, "spinach",16),
    MUSHROOMS(1.5, 2.5, 3.5, "mushrooms",17),
    PEPPERONI(7, 8, 9, "pepperoni",18),
    BACON(7, 8, 9, "bacon",19),
    CHICKEN_KEBAB(8, 9, 10, "chicken kebab",20),
    SALAMI(7, 8, 9, "salami",21),
    SPICY_SAUSAGE(7, 8, 9, "spicy sausage",22),
    HAM(6, 7.5, 8.5, "ham",23);


    private final double priceSmall;
    private final double priceMedium;
    private final double priceBig;
    private final String description;
    private final int value;

    public String getDescription() {
        return description;
    }

    PizzaIngredients(double priceSmall, double priceMedium, double priceBig, String description, int value) {
        this.priceSmall = priceSmall;
        this.priceMedium = priceMedium;
        this.priceBig = priceBig;
        this.description = description;
        this.value=value;
    }

    public double getPriceSmall() {
        return priceSmall;
    }

    public double getPriceMedium() {
        return priceMedium;
    }

    public double getPriceBig() {
        return priceBig;
    }

    public int getValue() {
        return value;
    }

    public static String calcPizzaPriceForMenu(MenuPizza menuPizza) {
        double priceS = 0;
        double priceM = 0;
        double priceL = 0;
        List<PizzaIngredients> pizzaIngredients = menuPizza.getSkladniki();
        for (PizzaIngredients pizzaIngredients1 : pizzaIngredients) {
            priceS += pizzaIngredients1.getPriceSmall();
            priceM += pizzaIngredients1.getPriceMedium();
            priceL += pizzaIngredients1.getPriceBig();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" /");
        sb.append(priceS).append(Pizza.CURRENCY).append("/");
        sb.append(priceM).append(Pizza.CURRENCY).append("/");
        sb.append(priceL).append(Pizza.CURRENCY).append("/");
        if (menuPizza.getName().equalsIgnoreCase("Custom")) {
            sb.append(" +extra ingredients ");
        }
        return sb.toString();
    }

    public double getPrice(Pizza pizza) {
        PizzaSizes size = pizza.getSize();
        if (size.equals(PizzaSizes.SMALL)) {
            return priceSmall;
        } else if (size.equals(PizzaSizes.MEDIUM)) {
            return priceMedium;
        } else if (size.equals(PizzaSizes.BIG)) {
            return priceBig;
        }
        throw new RuntimeException("Wrong pizza size");
    }

    public static PizzaIngredients fromDescription(int value) {
        PizzaIngredients[] values = PizzaIngredients.values();
        for (PizzaIngredients pizzaIngredients : values) {
            if (pizzaIngredients.getValue() == (value))
                return pizzaIngredients;
        }
        throw new RuntimeException("Exception wrong value! ");
    }
}