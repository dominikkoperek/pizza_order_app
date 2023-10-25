package PizzaApp.Model.PizzaModel;

public enum PizzaSizes {
    SMALL(0, "S-26CM"),
    MEDIUM(1, "M-32CM"),
    BIG(2, "L-42CM");
    private String describtion;
    private int value;

    PizzaSizes(int value, String describtion) {
        this.describtion = describtion;
        this.value = value;
    }

    public String getDescribtion() {
        return describtion;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return describtion;
    }

    public static PizzaSizes getSizeFromInt(int value) {
        PizzaSizes[] values = PizzaSizes.values();
        for (PizzaSizes pizzaSizes : values) {
            if (pizzaSizes.getValue() == value) {
                return pizzaSizes;
            }
        }
        throw new RuntimeException("Exception ");
    }
}
