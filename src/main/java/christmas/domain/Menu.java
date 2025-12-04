package christmas.domain;

import christmas.domain.dto.OrderCountDto;
import christmas.exception.InvalidOrderException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Menu {
    private final Map<DishType, List<Dish>> dishesForType;

    public Menu(Map<DishType, List<Dish>> dishesForType) {
        this.dishesForType = dishesForType;
    }

    public Dish findDishByName(String name) {
        Set<Dish> dishSet = dishesForType.entrySet().stream()
                .flatMap(d -> d.getValue().stream())
                .collect(Collectors.toSet());

        return dishSet.stream()
                .filter(d -> d.equalsNameOf(name))
                .findFirst()
                .orElseThrow(InvalidOrderException::new);
    }

    public boolean isDishExist(List<OrderCountDto> orderCountsDto) {
        boolean result = true;

        for (OrderCountDto orderCountDto : orderCountsDto) {
            result &= isDishExist(orderCountDto.getDishName());
        }
        return result;
    }

    private boolean isDishExist(String dishName) {
        return dishesForType.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .anyMatch(d -> d.equalsNameOf(dishName));
    }
}
