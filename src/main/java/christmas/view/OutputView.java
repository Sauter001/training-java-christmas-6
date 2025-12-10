package christmas.view;

import christmas.domain.DiscountStatistic;
import christmas.domain.dto.DishNameDto;
import christmas.domain.dto.OrderCountDto;
import christmas.domain.gift.Gift;

import java.text.DecimalFormat;
import java.util.List;

public class OutputView {
    private static final String NONE = "없음";
    private static final String TITLE_FORMAT = "<%s>\n";
    public static final String DISH_ORDER_FORMAT = "%s %d개\n";

    private static void printDish(OrderCountDto orderCountDto) {
        System.out.printf(DISH_ORDER_FORMAT, orderCountDto.getDishName(), orderCountDto.count());
    }

    private static void printTitle(String title) {
        System.out.printf(TITLE_FORMAT, title);
    }

    public void printError(Exception e) {
        System.out.println(e.getMessage());
    }

    public void displayDiscount(DiscountStatistic discountStatistic) {
        displayOrderInfo(discountStatistic.getOrderCounts());
        displayFullPrice(discountStatistic.getFullPrice());
        displayGift(discountStatistic.getGifts());
    }

    private void displayGift(List<Gift> gifts) {
        printTitle("증정 메뉴");
        if (gifts.isEmpty()) {
            System.out.println(NONE);
            System.out.println();
            return;
        }

        for (Gift gift : gifts) {
            System.out.printf(DISH_ORDER_FORMAT, gift.name(), gift.quantity());
        }
        System.out.println();
    }

    private void displayOrderInfo(List<OrderCountDto> orderCountDtos) {
        printTitle("주문 메뉴");
        for (OrderCountDto orderCountDto : orderCountDtos) {
            printDish(orderCountDto);
        }
        System.out.println();
    }

    private void displayFullPrice(int fullPrice) {
        printTitle("할인 전 총주문 금액");
        String fullPriceString = getDecimalFormat(fullPrice);

        System.out.printf("%s원\n\n", fullPriceString);
    }

    private String getDecimalFormat(int number) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(number);
    }
}
