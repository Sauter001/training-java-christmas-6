package christmas.domain.gift;

public class PriceBasedPolicy implements GiftPolicy {
    @Override
    public boolean isEligible(int totalPrice, GiftProduct giftProduct) {
        return totalPrice >= giftProduct.giftCriteria();
    }
}
