package christmas.config;

import christmas.domain.Dish;
import christmas.domain.DishType;
import christmas.domain.Menu;
import christmas.repository.DishRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AppConfig {
    private final DishRepository dishRepository;

    public AppConfig(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public Menu createMenu() {
        Map<DishType, List<Dish>> dishes = new HashMap<>();
        addDishes(dishes, DishType.APPETIZER);
        addDishes(dishes, DishType.MAIN);
        addDishes(dishes, DishType.DESSERT);
        addDishes(dishes, DishType.DRINK);

        return new Menu(dishes);
    }

    private void addDishes(Map<DishType, List<Dish>> dishes, DishType dishType) {
        dishes.put(dishType, this.dishRepository.findDishesOf(dishType));
    }
}
