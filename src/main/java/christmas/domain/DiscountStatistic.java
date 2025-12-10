package christmas.domain;

import christmas.domain.discount.Discount;
import christmas.domain.discount.DiscountEvent;
import christmas.domain.dto.OrderCountDto;
import christmas.domain.event.EventContext;
import christmas.domain.gift.Gift;
import christmas.domain.gift.GiftEvent;
import christmas.domain.order.Order;
import christmas.domain.vo.VisitDate;

import java.util.List;

public class DiscountStatistic {
    private final DiscountEvent discountEvent;
    private final GiftEvent giftEvent;
    private final VisitDate visitDate;
    private final Order order;

    public DiscountStatistic(VisitDate visitDate, Order order, DiscountEvent discountEvent, GiftEvent giftEvent) {
        this.visitDate = visitDate;
        this.order = order;
        this.discountEvent = discountEvent;
        this.giftEvent = giftEvent;
    }

    public VisitDate getVisitDate() {
        return visitDate;
    }

    public List<OrderCountDto> getOrderCounts() {
        return order.toOrderCountsDto();
    }

    public List<Gift> getGifts() {
        EventContext context = new EventContext(visitDate, order);
        return giftEvent.determineGifts(context);
    }

    public List<Discount> getDiscounts() {
        EventContext context = new EventContext(visitDate, order);
        return discountEvent.calculateDiscounts(context);
    }

    public int getTotalDiscount() {
        EventContext context = new EventContext(visitDate, order);
        return discountEvent.getTotalDiscount(context);
    }

    public int getTotalBenefit() {
        EventContext context = new EventContext(visitDate, order);
        int discounts = discountEvent.getTotalDiscount(context);
        int gifts = giftEvent.getTotalGiftPrice(context);
        return discounts + gifts;
    }

    public int getFullPrice() {
        return order.getFullPrice();
    }

    public int getPaymentAmount() {
        return getFullPrice() - getTotalDiscount();
    }
}
