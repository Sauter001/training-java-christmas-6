package christmas.domain.gift;

public interface GiftPolicy {
    boolean isEligible(int totalPrice, GiftProduct giftProduct);
}
