package christmas.domain;

import christmas.domain.discount.*;
import christmas.domain.gift.Gift;
import christmas.domain.gift.GiftEvent;
import christmas.domain.gift.GiftPolicy;
import christmas.domain.gift.GiftProduct;
import christmas.domain.gift.PriceBasedPolicy;
import christmas.domain.order.Dish;
import christmas.domain.order.DishType;
import christmas.domain.order.Order;
import christmas.domain.vo.VisitDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountStatisticIntegrationTest {

    @DisplayName("할인과 선물을 포함한 전체 혜택 금액을 계산할 수 있다")
    @Test
    void calculateTotalBenefitWithDiscountsAndGifts() {
        // given - 2023년 12월 3일 (일요일)
        VisitDate visitDate = new VisitDate(3);

        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish bbqRib = new Dish("바비큐립", 54000, DishType.MAIN);
        Dish chocolateCake = new Dish("초코케이크", 15000, DishType.DESSERT);
        Order order = new Order(Map.of(
                tBoneSteak, 1,
                bbqRib, 1,
                chocolateCake, 2
        ));

        List<DiscountPolicy> discountPolicies = List.of(
                new ChristmasDDayPolicy(),
                new WeekdayDiscountPolicy(),
                new WeekendDiscountPolicy(),
                new SpecialDiscountPolicy()
        );
        DiscountEvent discountEvent = new DiscountEvent(discountPolicies);

        GiftProduct champagne = new GiftProduct("샴페인", 120000, 25000);
        List<GiftPolicy> giftPolicies = List.of(new PriceBasedPolicy(List.of(champagne)));
        GiftEvent giftEvent = new GiftEvent(giftPolicies);

        DiscountStatistic statistic = new DiscountStatistic(visitDate, order, discountEvent, giftEvent);

        // when
        int totalBenefit = statistic.getTotalBenefit();
        int totalDiscount = statistic.getTotalDiscount();
        List<Gift> gifts = statistic.getGifts();
        List<Discount> discounts = statistic.getDiscounts();

        // then
        // 할인: 크리스마스 D-Day(1200) + 평일(4046) + 특별(1000) = 6246
        // 선물: 샴페인(25000)
        // 총 혜택: 31246
        assertThat(totalDiscount).isEqualTo(6246);
        assertThat(gifts).hasSize(1);
        assertThat(gifts.get(0).price()).isEqualTo(25000);
        assertThat(totalBenefit).isEqualTo(31246);
        assertThat(discounts).hasSize(3);
    }

    @DisplayName("주문 금액과 할인 금액을 통해 최종 결제 금액을 계산할 수 있다")
    @Test
    void calculatePaymentAmount() {
        // given - 2023년 12월 25일 (크리스마스)
        VisitDate visitDate = new VisitDate(25);

        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish zeroCoke = new Dish("제로콜라", 3000, DishType.DRINK);
        Order order = new Order(Map.of(
                tBoneSteak, 1,
                zeroCoke, 1
        ));

        List<DiscountPolicy> discountPolicies = List.of(
                new ChristmasDDayPolicy(),
                new WeekdayDiscountPolicy(),
                new WeekendDiscountPolicy(),
                new SpecialDiscountPolicy()
        );
        DiscountEvent discountEvent = new DiscountEvent(discountPolicies);

        GiftProduct champagne = new GiftProduct("샴페인", 120000, 25000);
        List<GiftPolicy> giftPolicies = List.of(new PriceBasedPolicy(List.of(champagne)));
        GiftEvent giftEvent = new GiftEvent(giftPolicies);

        DiscountStatistic statistic = new DiscountStatistic(visitDate, order, discountEvent, giftEvent);

        // when
        int fullPrice = statistic.getFullPrice();
        int paymentAmount = statistic.getPaymentAmount();

        // then
        // 주문 금액: 58000
        // 할인: 크리스마스 D-Day(3400) + 특별(1000) = 4400
        // 결제 금액: 58000 - 4400 = 53600
        assertThat(fullPrice).isEqualTo(58000);
        assertThat(paymentAmount).isEqualTo(53600);
    }

    @DisplayName("주말에 메인 메뉴를 주문하고 샴페인을 받는 경우")
    @Test
    void weekendWithMainMenuAndChampagne() {
        // given - 2023년 12월 1일 (금요일)
        VisitDate visitDate = new VisitDate(1);

        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish bbqRib = new Dish("바비큐립", 54000, DishType.MAIN);
        Dish chocolateCake = new Dish("초코케이크", 15000, DishType.DESSERT);
        Order order = new Order(Map.of(
                tBoneSteak, 1,
                bbqRib, 1,
                chocolateCake, 1
        ));

        List<DiscountPolicy> discountPolicies = List.of(
                new ChristmasDDayPolicy(),
                new WeekdayDiscountPolicy(),
                new WeekendDiscountPolicy(),
                new SpecialDiscountPolicy()
        );
        DiscountEvent discountEvent = new DiscountEvent(discountPolicies);

        GiftProduct champagne = new GiftProduct("샴페인", 120000, 25000);
        List<GiftPolicy> giftPolicies = List.of(new PriceBasedPolicy(List.of(champagne)));
        GiftEvent giftEvent = new GiftEvent(giftPolicies);

        DiscountStatistic statistic = new DiscountStatistic(visitDate, order, discountEvent, giftEvent);

        // when
        int fullPrice = statistic.getFullPrice();
        int totalDiscount = statistic.getTotalDiscount();
        int totalBenefit = statistic.getTotalBenefit();
        int paymentAmount = statistic.getPaymentAmount();

        // then
        // 주문 금액: 124000
        // 할인: 크리스마스 D-Day(1000) + 주말(4046) = 5046
        // 선물: 샴페인(25000)
        // 총 혜택: 30046
        // 결제 금액: 124000 - 5046 = 118954
        assertThat(fullPrice).isEqualTo(124000);
        assertThat(totalDiscount).isEqualTo(5046);
        assertThat(totalBenefit).isEqualTo(30046);
        assertThat(paymentAmount).isEqualTo(118954);
    }

    @DisplayName("주문 금액이 10000원 미만이면 할인과 선물이 모두 적용되지 않는다")
    @Test
    void noDiscountAndGiftWhenOrderPriceLessThan10000() {
        // given
        VisitDate visitDate = new VisitDate(3);

        Dish tapas = new Dish("타파스", 5500, DishType.APPETIZER);
        Order order = new Order(Map.of(tapas, 1));

        List<DiscountPolicy> discountPolicies = List.of(
                new ChristmasDDayPolicy(),
                new WeekdayDiscountPolicy(),
                new WeekendDiscountPolicy(),
                new SpecialDiscountPolicy()
        );
        DiscountEvent discountEvent = new DiscountEvent(discountPolicies);

        GiftProduct champagne = new GiftProduct("샴페인", 120000, 25000);
        List<GiftPolicy> giftPolicies = List.of(new PriceBasedPolicy(List.of(champagne)));
        GiftEvent giftEvent = new GiftEvent(giftPolicies);

        DiscountStatistic statistic = new DiscountStatistic(visitDate, order, discountEvent, giftEvent);

        // when
        int totalBenefit = statistic.getTotalBenefit();
        int totalDiscount = statistic.getTotalDiscount();
        List<Gift> gifts = statistic.getGifts();
        List<Discount> discounts = statistic.getDiscounts();

        // then
        assertThat(totalBenefit).isZero();
        assertThat(totalDiscount).isZero();
        assertThat(gifts).isEmpty();
        assertThat(discounts).isEmpty();
    }

    @DisplayName("26일 이후에는 크리스마스 D-Day 할인이 적용되지 않지만 다른 할인은 적용된다")
    @Test
    void noChristmasDDayDiscountAfter25th() {
        // given - 2023년 12월 26일 (화요일)
        VisitDate visitDate = new VisitDate(26);

        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish chocolateCake = new Dish("초코케이크", 15000, DishType.DESSERT);
        Order order = new Order(Map.of(
                tBoneSteak, 1,
                chocolateCake, 2
        ));

        List<DiscountPolicy> discountPolicies = List.of(
                new ChristmasDDayPolicy(),
                new WeekdayDiscountPolicy(),
                new WeekendDiscountPolicy(),
                new SpecialDiscountPolicy()
        );
        DiscountEvent discountEvent = new DiscountEvent(discountPolicies);

        GiftProduct champagne = new GiftProduct("샴페인", 120000, 25000);
        List<GiftPolicy> giftPolicies = List.of(new PriceBasedPolicy(List.of(champagne)));
        GiftEvent giftEvent = new GiftEvent(giftPolicies);

        DiscountStatistic statistic = new DiscountStatistic(visitDate, order, discountEvent, giftEvent);

        // when
        int totalDiscount = statistic.getTotalDiscount();
        List<Discount> discounts = statistic.getDiscounts();

        // then
        // 할인: 평일(4046)만 적용
        assertThat(totalDiscount).isEqualTo(4046);
        assertThat(discounts).hasSize(1);
        assertThat(discounts.get(0).name()).isEqualTo("평일 할인");
    }
}
