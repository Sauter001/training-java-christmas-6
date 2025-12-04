package christmas.domain;

import java.util.List;
import java.util.Map;

public class Menu {
    private final Map<DishType, List<Dish>> dishes;

    public Menu(Map<DishType, List<Dish>> dishes) {
        this.dishes = dishes;
    }
}
