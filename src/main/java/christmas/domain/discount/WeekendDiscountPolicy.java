package christmas.domain.discount;

import christmas.domain.order.DishType;
import christmas.domain.event.EventContext;
import christmas.util.DateUtil;

import java.util.List;

import static christmas.contant.DiscountConstant.*;

public class WeekendDiscountPolicy implements DiscountPolicy {
    private static final String EVENT_NAME = "주말 할인";

    @Override
    public List<Discount> apply(EventContext context) {
        int day = context.getVisitDate().date();

        if (isWeekday(day)) {
            return List.of();
        }

        int mainCount = context.countMenuByType(DishType.MAIN);
        if (mainCount == 0) {
            return List.of();
        }

        int discount = mainCount * DISCOUNT_AMOUNT_PER_WEEKDAY;
        return List.of(new Discount(EVENT_NAME, discount));
    }

    private static boolean isWeekday(int day) {
        return !DateUtil.isWeekend(CURRENT_YEAR, CURRENT_MONTH, day);
    }
}
