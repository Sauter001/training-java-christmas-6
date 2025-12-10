package christmas.domain.gift;

import christmas.domain.Dish;
import christmas.domain.dto.DishNameDto;

public record Gift(String  name, int quantity, int price) {
    public static Gift from(GiftProduct giftProduct) {
        return new Gift(giftProduct.dishName(), 1, giftProduct.giftPrice());
    }
}
