package christmas.domain.discount;

import christmas.domain.event.EventContext;

import java.util.List;

public class ChristmasDDayPolicy implements DiscountPolicy {
    private static final int START_DISCOUNT = 1000;
    private static final int DAILY_INCREMENT = 100;
    private static final int DDAY_START = 1;
    private static final int DDAY_END = 25;
    private static final String EVENT_NAME = "크리스마스 디데이 할인";

    @Override
    public List<Discount> apply(EventContext context) {
        int day = context.getVisitDate().date();

        if (day < DDAY_START || day > DDAY_END) {
            return List.of();
        }

        int discount = calculateDiscountAmount(day);
        return List.of(new Discount(EVENT_NAME, discount));
    }

    private int calculateDiscountAmount(int day) {
        return START_DISCOUNT + (day - 1) * DAILY_INCREMENT;
    }
}
