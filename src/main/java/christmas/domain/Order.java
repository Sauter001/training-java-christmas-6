package christmas.domain;

import christmas.domain.dto.OrderCountDto;

import java.util.List;
import java.util.Map;

public class Order {
    private final Map<Dish, Integer> dishCounter;

    public Order(Map<Dish, Integer> dishCounter) {
        this.dishCounter = dishCounter;
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
}
