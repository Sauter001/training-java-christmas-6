package christmas.view;

import christmas.domain.DiscountStatistic;
import christmas.domain.dto.DishNameDto;
import christmas.domain.dto.OrderCountDto;

import java.text.DecimalFormat;
import java.util.List;

public class OutputView {
    private static final String TITLE_FORMAT = "<%s>\n";

    public void printError(Exception e) {
        System.out.println(e.getMessage());
    }

    public void displayDiscount(DiscountStatistic  discountStatistic) {
        displayOrderInfo(discountStatistic.getOrderCounts());
        displayFullPrice(discountStatistic.getFullPrice());
        displayGift(discountStatistic.canGetGift());
    }

    private void displayGift(boolean canGetGift) {
        OrderCountDto champagneDto = new OrderCountDto(new DishNameDto("샴페인"), 1);
        System.out.printf(TITLE_FORMAT, "증정 메뉴");
        printDish(champagneDto);
        System.out.println();
    }

    private void displayOrderInfo(List<OrderCountDto> orderCountDtos) {
        System.out.printf(TITLE_FORMAT, "주문 메뉴");
        for (OrderCountDto orderCountDto : orderCountDtos) {
            printDish(orderCountDto);
        }
        System.out.println();
    }

    private static void printDish(OrderCountDto orderCountDto) {
        System.out.printf("%s %d개\n", orderCountDto.getDishName(), orderCountDto.count());
    }

    private void displayFullPrice(int fullPrice) {
        System.out.printf(TITLE_FORMAT, "할인 전 총주문 금액");
        String fullPriceString = getDecimalFormat(fullPrice);

        System.out.printf("%s원\n\n", fullPriceString);
    }

    private String getDecimalFormat(int number) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(number);
    }
}
