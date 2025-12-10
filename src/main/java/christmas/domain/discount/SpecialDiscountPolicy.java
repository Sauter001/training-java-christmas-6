package christmas.domain.discount;

import christmas.domain.event.EventContext;
import christmas.util.DateUtil;

import java.util.List;

import static christmas.contant.DiscountConstant.*;

public class SpecialDiscountPolicy implements DiscountPolicy {
    private static final String EVENT_NAME = "특별 할인";

    @Override
    public List<Discount> apply(EventContext context) {
        int day = context.getVisitDate().date();

        if (!DateUtil.isSpecialDay(CURRENT_YEAR, CURRENT_MONTH, day)) {
            return List.of();
        }

        return List.of(new Discount(EVENT_NAME, DISCOUNT_SPECIAL));
    }
}
