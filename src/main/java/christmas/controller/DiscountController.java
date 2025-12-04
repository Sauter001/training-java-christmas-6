package christmas.controller;

import christmas.domain.Menu;
import christmas.domain.vo.VisitDate;
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
    }
}
