package christmas.domain.gift;

public record Gift(String name, int quantity, int price) {
    public static Gift from(GiftProduct giftProduct) {
        return new Gift(giftProduct.dishName(), 1, giftProduct.giftPrice());
    }
}
