package christmas.domain.event;

import christmas.domain.order.DishType;
import christmas.domain.order.Order;
import christmas.domain.vo.VisitDate;

public class EventContext {
    private final VisitDate visitDate;
    private final Order order;

    public EventContext(VisitDate visitDate, Order order) {
        this.visitDate = visitDate;
        this.order = order;
    }

    public VisitDate getVisitDate() {
        return visitDate;
    }

    public int getTotalPrice() {
        return order.getFullPrice();
    }

    public int countMenuByType(DishType dishType) {
        return order.countByType(dishType);
    }
}
