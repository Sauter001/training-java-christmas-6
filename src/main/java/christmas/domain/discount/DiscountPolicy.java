package christmas.domain.discount;

import christmas.domain.event.EventContext;

import java.util.List;

public interface DiscountPolicy {
    List<Discount> apply(EventContext context);
}
