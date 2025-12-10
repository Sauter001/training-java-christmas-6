package christmas.domain.gift;

import java.util.List;

public class GiftEvent {
    private final List<GiftProduct> giftProducts;
    private final GiftPolicy giftPolicy;

    public GiftEvent(List<GiftProduct> giftProducts, GiftPolicy giftPolicy) {
        this.giftProducts = giftProducts;
        this.giftPolicy = giftPolicy;
    }

    public List<Gift> determineGifts(int totalPrice) {
        return this.giftProducts.stream()
                .filter(gp -> this.giftPolicy.isEligible(totalPrice, gp))
                .map(Gift::from)
                .toList();
    }
}
