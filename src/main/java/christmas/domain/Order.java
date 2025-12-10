package christmas.domain;

import christmas.domain.dto.OrderCountDto;
import christmas.exception.DiscountException;
import christmas.exception.ErrorMessage;

import java.util.List;
import java.util.Map;

public class Order {
    private final Map<Dish, Integer> dishCounter;

    public Order(Map<Dish, Integer> dishCounter) {
        validateOnlyDrinkExist(dishCounter);
        this.dishCounter = dishCounter;
    }

    private void validateOnlyDrinkExist(Map<Dish, Integer> dishCounter) {
        int totalQuantity = dishCounter.values().stream().mapToInt(Integer::intValue).sum();
        int drinkQuantity = dishCounter.entrySet().stream()
                .filter(e -> e.getKey().typeEquals(DishType.DRINK))
                .mapToInt(Map.Entry::getValue).sum();

        if (totalQuantity == drinkQuantity) {
            throw new DiscountException(ErrorMessage.ONLY_BEVERAGE_NOT_ALLOWED);
        }
    }

    public int findCountOf(Dish dish) {
        return dishCounter.getOrDefault(dish, 0);
    }

    public List<OrderCountDto> toOrderCountsDto() {
        return dishCounter.entrySet().stream()
                .map(entry ->
                        new OrderCountDto(entry.getKey().toDishNameDto(), entry.getValue())
                ).toList();
    }

    public int getFullPrice() {
        int result = 0;

        for (Dish dish : dishCounter.keySet()) {
            int quantity = dishCounter.get(dish);
            result += dish.calculateBoughtPrice(quantity);
        }

        return result;
    }
}
