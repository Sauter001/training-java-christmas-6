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

class WeekendDiscountPolicyTest {

    @DisplayName("주말(금요일)에 메인 메뉴가 있으면 할인이 적용된다")
    @Test
    void applyDiscountOnFridayWithMain() {
        // given - 2023년 12월 1일은 금요일
        WeekendDiscountPolicy policy = new WeekendDiscountPolicy();
        VisitDate visitDate = new VisitDate(1);
        Order order = createOrderWithMain(2);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).hasSize(1);
        assertThat(discounts.get(0).amount()).isEqualTo(2023 * 2);
    }

    @DisplayName("주말(토요일)에 메인 메뉴가 있으면 할인이 적용된다")
    @Test
    void applyDiscountOnSaturdayWithMain() {
        // given - 2023년 12월 2일은 토요일
        WeekendDiscountPolicy policy = new WeekendDiscountPolicy();
        VisitDate visitDate = new VisitDate(2);
        Order order = createOrderWithMain(1);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).hasSize(1);
        assertThat(discounts.get(0).amount()).isEqualTo(2023);
    }

    @DisplayName("평일(일요일)에는 주말 할인이 적용되지 않는다")
    @Test
    void noDiscountOnSunday() {
        // given - 2023년 12월 3일은 일요일
        WeekendDiscountPolicy policy = new WeekendDiscountPolicy();
        VisitDate visitDate = new VisitDate(3);
        Order order = createOrderWithMain(2);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).isEmpty();
    }

    @DisplayName("평일(월요일)에는 주말 할인이 적용되지 않는다")
    @Test
    void noDiscountOnMonday() {
        // given - 2023년 12월 4일은 월요일
        WeekendDiscountPolicy policy = new WeekendDiscountPolicy();
        VisitDate visitDate = new VisitDate(4);
        Order order = createOrderWithMain(2);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).isEmpty();
    }

    @DisplayName("주말이지만 메인 메뉴가 없으면 할인이 적용되지 않는다")
    @Test
    void noDiscountOnWeekendWithoutMain() {
        // given - 2023년 12월 1일은 금요일
        WeekendDiscountPolicy policy = new WeekendDiscountPolicy();
        VisitDate visitDate = new VisitDate(1);
        Order order = createOrderWithoutMain();
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).isEmpty();
    }

    @DisplayName("메인 메뉴 개수만큼 할인 금액이 증가한다")
    @Test
    void discountAmountIncreasesWithMainCount() {
        // given - 2023년 12월 1일은 금요일
        WeekendDiscountPolicy policy = new WeekendDiscountPolicy();
        VisitDate visitDate = new VisitDate(1);
        Order order = createOrderWithMain(3);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).hasSize(1);
        assertThat(discounts.get(0).amount()).isEqualTo(2023 * 3);
    }

    @DisplayName("할인 이벤트 이름은 '주말 할인'이다")
    @Test
    void checkDiscountEventName() {
        // given - 2023년 12월 1일은 금요일
        WeekendDiscountPolicy policy = new WeekendDiscountPolicy();
        VisitDate visitDate = new VisitDate(1);
        Order order = createOrderWithMain(1);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts.get(0).name()).isEqualTo("주말 할인");
    }

    private Order createOrderWithMain(int mainCount) {
        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish zeroCoke = new Dish("제로콜라", 3000, DishType.DRINK);
        return new Order(Map.of(
                tBoneSteak, mainCount,
                zeroCoke, 1
        ));
    }

    private Order createOrderWithoutMain() {
        Dish chocolateCake = new Dish("초코케이크", 15000, DishType.DESSERT);
        Dish zeroCoke = new Dish("제로콜라", 3000, DishType.DRINK);
        return new Order(Map.of(
                chocolateCake, 2,
                zeroCoke, 1
        ));
    }
}
