package christmas.domain.discount;

import christmas.domain.order.DishType;
import christmas.domain.event.EventContext;
import christmas.util.DateUtil;

import java.util.List;

import static christmas.contant.DiscountConstant.*;

public class WeekdayDiscountPolicy implements DiscountPolicy {
    private static final String EVENT_NAME = "평일 할인";

    @Override
    public List<Discount> apply(EventContext context) {
        int day = context.getVisitDate().date();

        if (DateUtil.isWeekend(CURRENT_YEAR, CURRENT_MONTH, day)) {
            return List.of();
        }

        int dessertCount = context.countMenuByType(DishType.DESSERT);
        if (dessertCount == 0) {
            return List.of();
        }

        int discount = dessertCount * DISCOUNT_AMOUNT_PER_WEEKDAY;
        return List.of(new Discount(EVENT_NAME, discount));
    }
}
