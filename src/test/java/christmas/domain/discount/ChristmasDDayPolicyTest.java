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

class ChristmasDDayPolicyTest {

    @DisplayName("12월 1일에는 1000원 할인이 적용된다")
    @Test
    void applyDiscountOnFirstDay() {
        // given
        ChristmasDDayPolicy policy = new ChristmasDDayPolicy();
        VisitDate visitDate = new VisitDate(1);
        Order order = createOrder();
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).hasSize(1);
        assertThat(discounts.get(0).amount()).isEqualTo(1000);
    }

    @DisplayName("12월 2일에는 1100원 할인이 적용된다")
    @Test
    void applyDiscountOnSecondDay() {
        // given
        ChristmasDDayPolicy policy = new ChristmasDDayPolicy();
        VisitDate visitDate = new VisitDate(2);
        Order order = createOrder();
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).hasSize(1);
        assertThat(discounts.get(0).amount()).isEqualTo(1100);
    }

    @DisplayName("12월 25일(크리스마스)에는 3400원 할인이 적용된다")
    @Test
    void applyDiscountOnChristmas() {
        // given
        ChristmasDDayPolicy policy = new ChristmasDDayPolicy();
        VisitDate visitDate = new VisitDate(25);
        Order order = createOrder();
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).hasSize(1);
        assertThat(discounts.get(0).amount()).isEqualTo(3400);
    }

    @DisplayName("12월 26일에는 할인이 적용되지 않는다")
    @Test
    void noDiscountAfterChristmas() {
        // given
        ChristmasDDayPolicy policy = new ChristmasDDayPolicy();
        VisitDate visitDate = new VisitDate(26);
        Order order = createOrder();
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).isEmpty();
    }

    @DisplayName("12월 31일에는 할인이 적용되지 않는다")
    @Test
    void noDiscountOnLastDay() {
        // given
        ChristmasDDayPolicy policy = new ChristmasDDayPolicy();
        VisitDate visitDate = new VisitDate(31);
        Order order = createOrder();
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts).isEmpty();
    }

    @DisplayName("할인 이벤트 이름은 '크리스마스 디데이 할인'이다")
    @Test
    void checkDiscountEventName() {
        // given
        ChristmasDDayPolicy policy = new ChristmasDDayPolicy();
        VisitDate visitDate = new VisitDate(1);
        Order order = createOrder();
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Discount> discounts = policy.apply(context);

        // then
        assertThat(discounts.get(0).name()).isEqualTo("크리스마스 디데이 할인");
    }

    private Order createOrder() {
        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        return new Order(Map.of(tBoneSteak, 1));
    }
}
