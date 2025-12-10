package christmas.view;

import christmas.domain.order.UnvalidatedOrder;
import christmas.domain.vo.VisitDate;

public interface InputView {
    VisitDate readDate();

    UnvalidatedOrder readUnvalidatedOrder();
}
