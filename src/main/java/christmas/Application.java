package christmas;

import christmas.config.AppConfig;
import christmas.controller.DiscountController;
import christmas.repository.DishRepository;
import christmas.view.InputView;
import christmas.view.InputViewImpl;

public class Application {
    public static void main(String[] args) {
        DishRepository dishRepository = new DishRepository();
        AppConfig appConfig = new AppConfig(dishRepository);
        InputView inputView = new InputViewImpl();

        DiscountController discountController = new DiscountController(inputView, appConfig.createMenu());
        discountController.run();
    }
}
