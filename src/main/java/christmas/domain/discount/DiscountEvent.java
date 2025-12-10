package christmas.domain.discount;

import christmas.domain.event.EventContext;

import java.util.List;

import static christmas.contant.DiscountConstant.MINIMUM_ORDER_AMOUNT;

public class DiscountEvent {
    private final List<DiscountPolicy> policies;

    public DiscountEvent(List<DiscountPolicy> policies) {
        this.policies = policies;
    }

    public List<Discount> calculateDiscounts(EventContext context) {
        if (context.getTotalPrice() < MINIMUM_ORDER_AMOUNT) {
            return List.of();
        }

        return policies.stream()
                .flatMap(policy -> policy.apply(context).stream())
                .toList();
    }

    public int getTotalDiscount(EventContext context) {
        return calculateDiscounts(context).stream()
                .mapToInt(Discount::amount)
                .sum();
    }
}
