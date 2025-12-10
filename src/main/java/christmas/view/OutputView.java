package christmas.view;

import christmas.domain.DiscountStatistic;
import christmas.domain.discount.Discount;
import christmas.domain.dto.DishNameDto;
import christmas.domain.dto.OrderCountDto;
import christmas.domain.gift.Gift;
import christmas.domain.vo.Badge;
import christmas.domain.vo.VisitDate;

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
        displayDiscountInfoOfDate(discountStatistic.getVisitDate());
        displayOrderInfo(discountStatistic.getOrderCounts());
        displayFullPrice(discountStatistic.getFullPrice());
        displayGift(discountStatistic.getGifts());
        displayBenefitDetails(discountStatistic.getDiscounts(), discountStatistic.getGifts());
        displayTotalBenefit(discountStatistic.getTotalBenefit());
        displayPaymentAmount(discountStatistic.getPaymentAmount());
        displayBadge(discountStatistic.getTotalBenefit());
    }

    private void displayDiscountInfoOfDate(VisitDate visitDate) {
        System.out.printf("12월 %d일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!\n", visitDate.date());
        System.out.println();
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

    private void displayBenefitDetails(List<Discount> discounts, List<Gift> gifts) {
        printTitle("혜택 내역");

        if (discounts.isEmpty() && gifts.isEmpty()) {
            System.out.println(NONE);
            System.out.println();
            return;
        }

        for (Discount discount : discounts) {
            System.out.printf("%s: -%s원\n", discount.name(), getDecimalFormat(discount.amount()));
        }

        for (Gift gift : gifts) {
            System.out.printf("증정 이벤트: -%s원\n", getDecimalFormat(gift.price()));
        }
        System.out.println();
    }

    private void displayTotalBenefit(int totalBenefit) {
        printTitle("총혜택 금액");
        if (totalBenefit == 0) {
            System.out.println("0원");
        } else {
            System.out.printf("-%s원\n", getDecimalFormat(totalBenefit));
        }
        System.out.println();
    }

    private void displayPaymentAmount(int paymentAmount) {
        printTitle("할인 후 예상 결제 금액");
        System.out.printf("%s원\n\n", getDecimalFormat(paymentAmount));
    }

    private void displayBadge(int totalBenefit) {
        printTitle("12월 이벤트 배지");
        Badge badge = Badge.from(totalBenefit);
        System.out.println(badge.getName());
    }

    private String getDecimalFormat(int number) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(number);
    }
}
