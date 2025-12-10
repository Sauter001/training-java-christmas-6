package christmas.domain;

import christmas.domain.dto.OrderCountDto;
import christmas.domain.gift.Gift;
import christmas.domain.gift.GiftEvent;
import christmas.domain.vo.VisitDate;

import java.util.List;

public class DiscountStatistic {
    public static final int MIN_PRICE_FOR_GIFT = 120000;
    private final GiftEvent giftEvent;
    private final Order order;
    private final VisitDate visitDate;

    public DiscountStatistic(VisitDate visitDate, Order order, GiftEvent giftEvent) {
        this.visitDate = visitDate;
        this.order = order;
        this.giftEvent = giftEvent;
    }

    public List<OrderCountDto> getOrderCounts() {
        return order.toOrderCountsDto();
    }

    public List<Gift> getGifts() {
        return this.giftEvent.determineGifts(order.getFullPrice());
    }

    public int getTotalPrice() {
        return getGifts().stream()
                .mapToInt(Gift::price)
                .sum();
    }

    public int getFullPrice() {
        return order.getFullPrice();
    }
}
