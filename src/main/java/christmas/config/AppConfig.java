package christmas.config;

import christmas.domain.order.Dish;
import christmas.domain.order.DishType;
import christmas.domain.order.Menu;
import christmas.domain.discount.*;
import christmas.domain.gift.GiftEvent;
import christmas.domain.gift.GiftPolicy;
import christmas.domain.gift.GiftProduct;
import christmas.domain.gift.PriceBasedPolicy;
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

    public DiscountEvent createDiscountEvent() {
        List<DiscountPolicy> policies = List.of(
                new ChristmasDDayPolicy(),
                new WeekdayDiscountPolicy(),
                new WeekendDiscountPolicy(),
                new SpecialDiscountPolicy()
        );
        return new DiscountEvent(policies);
    }

    public GiftEvent createGiftEvent() {
        List<GiftProduct> giftProducts = this.dishRepository.findAllGiftProducts();
        GiftPolicy policy = new PriceBasedPolicy(giftProducts);
        return new GiftEvent(List.of(policy));
    }

    private void addDishes(Map<DishType, List<Dish>> dishes, DishType dishType) {
        dishes.put(dishType, this.dishRepository.findDishesOf(dishType));
    }
}
