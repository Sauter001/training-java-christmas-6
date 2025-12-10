package christmas.domain.order;

import christmas.domain.dto.DishNameDto;

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

    public DishNameDto toDishNameDto() {
        return new DishNameDto(this.name);
    }

    public int calculateBoughtPrice(int quantity) {
        return this.price * quantity;
    }

    public boolean equalsNameOf(String dishName) {
        return this.name.equals(dishName);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Dish dish)) {
            return false;
        }

        return this.name.equals(dish.name);
    }
}
