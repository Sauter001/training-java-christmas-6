package christmas.domain.discount;

import christmas.domain.event.EventContext;
import christmas.domain.order.Dish;
import christmas.domain.order.DishType;
import christmas.domain.order.Order;
import christmas.domain.vo.VisitDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountEventIntegrationTest {

    @DisplayName("주문 금액이 10000원 미만이면 어떤 할인도 적용되지 않는다")
    @Test
    void noDiscountWhenOrderPriceLessThanMinimum() {
        // given
        List<DiscountPolicy> policies = List.of(
                new ChristmasDDayPolicy(),
                new WeekdayDiscountPolicy(),
                new WeekendDiscountPolicy(),
                new SpecialDiscountPolicy()
        );
        DiscountEvent discountEvent = new DiscountEvent(policies);

        Dish tapas = new Dish("타파스", 5500, DishType.APPETIZER);
        Order order = new Order(Map.of(tapas, 1));
        VisitDate visitDate = new VisitDate(3);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = discountEvent.calculateDiscounts(context);
        int totalDiscount = discountEvent.getTotalDiscount(context);

        // then
        assertThat(discounts).isEmpty();
        assertThat(totalDiscount).isZero();
    }

    @DisplayName("평일(일요일)에 디저트를 주문하면 크리스마스 D-Day, 평일, 특별 할인이 모두 적용된다")
    @Test
    void applyMultipleDiscountsOnWeekdayWithDessert() {
        // given - 2023년 12월 3일은 일요일 (크리스마스 D-Day 기간, 평일, 특별한 날)
        List<DiscountPolicy> policies = List.of(
                new ChristmasDDayPolicy(),
                new WeekdayDiscountPolicy(),
                new WeekendDiscountPolicy(),
                new SpecialDiscountPolicy()
        );
        DiscountEvent discountEvent = new DiscountEvent(policies);

        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish chocolateCake = new Dish("초코케이크", 15000, DishType.DESSERT);
        Order order = new Order(Map.of(
                tBoneSteak, 1,
                chocolateCake, 2
        ));
        VisitDate visitDate = new VisitDate(3);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = discountEvent.calculateDiscounts(context);
        int totalDiscount = discountEvent.getTotalDiscount(context);

        // then
        assertThat(discounts).hasSize(3);
        assertThat(discounts)
                .extracting(Discount::name)
                .containsExactlyInAnyOrder("크리스마스 디데이 할인", "평일 할인", "특별 할인");

        // 크리스마스 디데이 할인: 1000 + (3-1) * 100 = 1200
        // 평일 할인: 2023 * 2 = 4046
        // 특별 할인: 1000
        // 총 할인: 6246
        assertThat(totalDiscount).isEqualTo(6246);
    }

    @DisplayName("주말(금요일)에 메인을 주문하면 크리스마스 D-Day와 주말 할인이 적용된다")
    @Test
    void applyMultipleDiscountsOnWeekendWithMain() {
        // given - 2023년 12월 1일은 금요일 (크리스마스 D-Day 기간, 주말)
        List<DiscountPolicy> policies = List.of(
                new ChristmasDDayPolicy(),
                new WeekdayDiscountPolicy(),
                new WeekendDiscountPolicy(),
                new SpecialDiscountPolicy()
        );
        DiscountEvent discountEvent = new DiscountEvent(policies);

        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish bbqRib = new Dish("바비큐립", 54000, DishType.MAIN);
        Dish zeroCoke = new Dish("제로콜라", 3000, DishType.DRINK);
        Order order = new Order(Map.of(
                tBoneSteak, 1,
                bbqRib, 1,
                zeroCoke, 1
        ));
        VisitDate visitDate = new VisitDate(1);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = discountEvent.calculateDiscounts(context);
        int totalDiscount = discountEvent.getTotalDiscount(context);

        // then
        assertThat(discounts).hasSize(2);
        assertThat(discounts)
                .extracting(Discount::name)
                .containsExactlyInAnyOrder("크리스마스 디데이 할인", "주말 할인");

        // 크리스마스 디데이 할인: 1000
        // 주말 할인: 2023 * 2 = 4046
        // 총 할인: 5046
        assertThat(totalDiscount).isEqualTo(5046);
    }

    @DisplayName("12월 25일(크리스마스, 월요일)에는 크리스마스 D-Day와 특별 할인만 적용된다")
    @Test
    void applyDiscountsOnChristmas() {
        // given - 2023년 12월 25일은 월요일이자 크리스마스
        List<DiscountPolicy> policies = List.of(
                new ChristmasDDayPolicy(),
                new WeekdayDiscountPolicy(),
                new WeekendDiscountPolicy(),
                new SpecialDiscountPolicy()
        );
        DiscountEvent discountEvent = new DiscountEvent(policies);

        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish zeroCoke = new Dish("제로콜라", 3000, DishType.DRINK);
        Order order = new Order(Map.of(
                tBoneSteak, 1,
                zeroCoke, 1
        ));
        VisitDate visitDate = new VisitDate(25);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = discountEvent.calculateDiscounts(context);
        int totalDiscount = discountEvent.getTotalDiscount(context);

        // then
        assertThat(discounts).hasSize(2);
        assertThat(discounts)
                .extracting(Discount::name)
                .containsExactlyInAnyOrder("크리스마스 디데이 할인", "특별 할인");

        // 크리스마스 디데이 할인: 1000 + (25-1) * 100 = 3400
        // 특별 할인: 1000
        // 총 할인: 4400
        assertThat(totalDiscount).isEqualTo(4400);
    }

    @DisplayName("12월 26일 이후에는 크리스마스 D-Day 할인이 적용되지 않는다")
    @Test
    void noChristmasDDayDiscountAfter25th() {
        // given - 2023년 12월 26일은 화요일
        List<DiscountPolicy> policies = List.of(
                new ChristmasDDayPolicy(),
                new WeekdayDiscountPolicy(),
                new WeekendDiscountPolicy(),
                new SpecialDiscountPolicy()
        );
        DiscountEvent discountEvent = new DiscountEvent(policies);

        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish chocolateCake = new Dish("초코케이크", 15000, DishType.DESSERT);
        Order order = new Order(Map.of(
                tBoneSteak, 1,
                chocolateCake, 2
        ));
        VisitDate visitDate = new VisitDate(26);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = discountEvent.calculateDiscounts(context);
        int totalDiscount = discountEvent.getTotalDiscount(context);

        // then
        assertThat(discounts).hasSize(1);
        assertThat(discounts)
                .extracting(Discount::name)
                .containsExactly("평일 할인");

        // 평일 할인: 2023 * 2 = 4046
        assertThat(totalDiscount).isEqualTo(4046);
    }
}
