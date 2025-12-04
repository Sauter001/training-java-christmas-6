package christmas.controller;

import christmas.domain.DiscountStatistic;
import christmas.domain.Menu;
import christmas.domain.Order;
import christmas.domain.UnvalidatedOrder;
import christmas.domain.vo.VisitDate;
import christmas.exception.InvalidOrderException;
import christmas.view.InputView;
import christmas.view.OutputView;

public class DiscountController {
    private final InputView inputView;
    private final OutputView outputView;
    private final Menu menu;

    public DiscountController(InputView inputView, Menu menu) {
        this.inputView = inputView;
        this.outputView = new OutputView();
        this.menu = menu;
    }

    public void run() {
        VisitDate visitDate = inputView.readDate();
        UnvalidatedOrder unvalidatedOrder =  readUnvalidatedOrderWithRetry();
        Order order = unvalidatedOrder.makeOrderFrom(this.menu);
        DiscountStatistic discountStatistic = new DiscountStatistic(visitDate, order);

        outputView.displayDiscount(discountStatistic);
    }

    private UnvalidatedOrder readUnvalidatedOrderWithRetry() {
        try {
            UnvalidatedOrder unvalidatedOrder = inputView.readUnvalidatedOrder();
            if (!menu.isDishExist(unvalidatedOrder.toOrderCountsDto())) {
                throw new InvalidOrderException();
            }

            return unvalidatedOrder;
        } catch (InvalidOrderException e) {
            outputView.printError(e);
            return inputView.readUnvalidatedOrder();
        }
    }
}

