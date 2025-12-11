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

class SpecialDiscountPolicyTest {

    @DisplayName("일요일에는 특별 할인이 적용된다")
    @Test
    void applyDiscountOnSunday() {
        // given - 2023년 12월 3일은 일요일
        SpecialDiscountPolicy policy = new SpecialDiscountPolicy();
        VisitDate visitDate = new VisitDate(3);
        Order order = createOrder();
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).hasSize(1);
        assertThat(discounts.get(0).amount()).isEqualTo(1000);
    }

    @DisplayName("크리스마스에는 특별 할인이 적용된다")
    @Test
    void applyDiscountOnChristmas() {
        // given - 2023년 12월 25일은 크리스마스 (월요일)
        SpecialDiscountPolicy policy = new SpecialDiscountPolicy();
        VisitDate visitDate = new VisitDate(25);
        Order order = createOrder();
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).hasSize(1);
        assertThat(discounts.get(0).amount()).isEqualTo(1000);
    }

    @DisplayName("특별한 날이 아니면 할인이 적용되지 않는다")
    @Test
    void noDiscountOnNormalDay() {
        // given - 2023년 12월 4일은 월요일 (특별한 날 아님)
        SpecialDiscountPolicy policy = new SpecialDiscountPolicy();
        VisitDate visitDate = new VisitDate(4);
        Order order = createOrder();
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).isEmpty();
    }

    @DisplayName("금요일은 특별한 날이 아니므로 할인이 적용되지 않는다")
    @Test
    void noDiscountOnFriday() {
        // given - 2023년 12월 1일은 금요일
        SpecialDiscountPolicy policy = new SpecialDiscountPolicy();
        VisitDate visitDate = new VisitDate(1);
        Order order = createOrder();
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).isEmpty();
    }

    @DisplayName("토요일은 특별한 날이 아니므로 할인이 적용되지 않는다")
    @Test
    void noDiscountOnSaturday() {
        // given - 2023년 12월 2일은 토요일
        SpecialDiscountPolicy policy = new SpecialDiscountPolicy();
        VisitDate visitDate = new VisitDate(2);
        Order order = createOrder();
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).isEmpty();
    }

    @DisplayName("할인 이벤트 이름은 '특별 할인'이다")
    @Test
    void checkDiscountEventName() {
        // given - 2023년 12월 3일은 일요일
        SpecialDiscountPolicy policy = new SpecialDiscountPolicy();
        VisitDate visitDate = new VisitDate(3);
        Order order = createOrder();
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts.get(0).name()).isEqualTo("특별 할인");
    }

    private Order createOrder() {
        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        return new Order(Map.of(tBoneSteak, 1));
    }
}
