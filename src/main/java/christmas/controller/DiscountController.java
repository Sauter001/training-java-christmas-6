package christmas.controller;

import christmas.domain.DiscountStatistic;
import christmas.domain.order.Menu;
import christmas.domain.order.Order;
import christmas.domain.order.UnvalidatedOrder;
import christmas.domain.discount.DiscountEvent;
import christmas.domain.gift.GiftEvent;
import christmas.domain.vo.VisitDate;
import christmas.exception.InvalidOrderException;
import christmas.view.InputView;
import christmas.view.OutputView;

public class DiscountController {
    private final InputView inputView;
    private final OutputView outputView;
    private final Menu menu;
    private final DiscountEvent discountEvent;
    private final GiftEvent giftEvent;

    public DiscountController(InputView inputView, Menu menu, DiscountEvent discountEvent, GiftEvent giftEvent) {
        this.inputView = inputView;
        this.outputView = new OutputView();
        this.menu = menu;
        this.discountEvent = discountEvent;
        this.giftEvent = giftEvent;
    }

    public void run() {
        VisitDate visitDate = inputView.readDate();
        UnvalidatedOrder unvalidatedOrder = readUnvalidatedOrderWithRetry();
        Order order = unvalidatedOrder.makeOrderFrom(this.menu);
        DiscountStatistic discountStatistic = new DiscountStatistic(visitDate, order, discountEvent, giftEvent);

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

