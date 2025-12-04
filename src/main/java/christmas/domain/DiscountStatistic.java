package christmas.domain;

import christmas.domain.dto.OrderCountDto;
import christmas.domain.vo.VisitDate;

import java.util.List;

public class DiscountStatistic {
    public static final int MIN_PRICE_FOR_GIFT = 120000;
    private final VisitDate visitDate;
    private final Order order;

    public DiscountStatistic(VisitDate visitDate, Order order) {
        this.visitDate = visitDate;
        this.order = order;
    }

    public List<OrderCountDto> getOrderCounts() {
        return order.toOrderCountsDto();
    }

    public boolean canGetGift() {
        return order.getFullPrice() >=  MIN_PRICE_FOR_GIFT;
    }

    public int getFullPrice() {
        return order.getFullPrice();
    }
}
