package christmas.domain;

import christmas.domain.dto.DishNameDto;
import christmas.domain.dto.OrderCountDto;
import christmas.exception.DiscountException;
import christmas.exception.ErrorMessage;
import christmas.exception.InvalidOrderException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UnvalidatedOrder {
    private static final int MIN_ORDER_COUNT = 1;
    private static final int MAX_ORDER_COUNT = 20;
    private final Map<String, Integer> dishCounter;

    public UnvalidatedOrder() {
        this.dishCounter = new HashMap<>();
    }

    private static void validateOrderQuantity(int quantity) {
        if (quantity < MIN_ORDER_COUNT) {
            throw new InvalidOrderException();
        }
    }

    public void putOrder(String dishName, int quantity) {
        validateDishDisjoint(dishName);
        validateTotalQuantity(quantity);
        validateOrderQuantity(quantity);

        dishCounter.put(dishName, quantity);
    }

    private void validateTotalQuantity(int quantity) {
        int totalQuantity = this.dishCounter.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        totalQuantity += quantity;

        if (totalQuantity >= MAX_ORDER_COUNT) {
            throw new DiscountException(ErrorMessage.ORDER_COUNT_EXCEED);
        }
    }

    public List<OrderCountDto> toOrderCountsDto() {
        return dishCounter.entrySet().stream()
                .map(entry ->
                        new OrderCountDto(new DishNameDto(entry.getKey()), entry.getValue())
                ).toList();
    }

    public Order makeOrderFrom(Menu menu) {
        Map<Dish, Integer> dishCounter = this.dishCounter.entrySet().stream()
                .collect(Collectors.toMap(k -> menu.findDishByName(k.getKey()), Map.Entry::getValue));

        return new Order(dishCounter);
    }

    private void validateDishDisjoint(String dishName) {
        if (dishCounter.containsKey(dishName)) {
            throw new InvalidOrderException();
        }
    }
}
