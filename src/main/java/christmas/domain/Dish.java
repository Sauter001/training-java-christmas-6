package christmas.domain;

public class Dish {
    private final String name;
    private final int price;
    private final DishType dishType;

    public Dish(String name, int price, DishType dishType) {
        this.name = name;
        this.price = price;
        this.dishType = dishType;
    }

    public boolean typeEquals(DishType dishType) {
        return this.dishType == dishType;
    }
}
