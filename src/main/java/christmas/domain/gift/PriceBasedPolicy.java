package christmas.domain.gift;

import christmas.domain.event.EventContext;

import java.util.List;

public class PriceBasedPolicy implements GiftPolicy {
    private final List<GiftProduct> giftProducts;

    public PriceBasedPolicy(List<GiftProduct> giftProducts) {
        this.giftProducts = giftProducts;
    }

    @Override
    public List<Gift> apply(EventContext context) {
        int totalPrice = context.getTotalPrice();

        return giftProducts.stream()
                .filter(gp -> totalPrice >= gp.giftCriteria())
                .map(Gift::from)
                .toList();
    }
}
