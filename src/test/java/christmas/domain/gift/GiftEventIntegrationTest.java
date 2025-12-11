package christmas.domain.gift;

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

class GiftEventIntegrationTest {

    @DisplayName("주문 금액이 10000원 미만이면 선물이 증정되지 않는다")
    @Test
    void noGiftWhenOrderPriceLessThanMinimum() {
        // given
        GiftProduct champagne = new GiftProduct("샴페인", 120000, 25000);
        List<GiftPolicy> policies = List.of(new PriceBasedPolicy(List.of(champagne)));
        GiftEvent giftEvent = new GiftEvent(policies);

        Dish tapas = new Dish("타파스", 5500, DishType.APPETIZER);
        Order order = new Order(Map.of(tapas, 1));
        VisitDate visitDate = new VisitDate(3);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Gift> gifts = giftEvent.determineGifts(context);
        int totalGiftPrice = giftEvent.getTotalGiftPrice(context);

        // then
        assertThat(gifts).isEmpty();
        assertThat(totalGiftPrice).isZero();
    }

    @DisplayName("주문 금액이 120000원 이상이면 샴페인이 증정된다")
    @Test
    void giveChampagneWhenOrderPriceOver120000() {
        // given
        GiftProduct champagne = new GiftProduct("샴페인", 120000, 25000);
        List<GiftPolicy> policies = List.of(new PriceBasedPolicy(List.of(champagne)));
        GiftEvent giftEvent = new GiftEvent(policies);

        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish bbqRib = new Dish("바비큐립", 54000, DishType.MAIN);
        Dish chocolateCake = new Dish("초코케이크", 15000, DishType.DESSERT);
        Order order = new Order(Map.of(
                tBoneSteak, 1,
                bbqRib, 1,
                chocolateCake, 1
        ));
        VisitDate visitDate = new VisitDate(3);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Gift> gifts = giftEvent.determineGifts(context);
        int totalGiftPrice = giftEvent.getTotalGiftPrice(context);

        // then
        assertThat(gifts).hasSize(1);
        assertThat(gifts.get(0).name()).isEqualTo("샴페인");
        assertThat(gifts.get(0).quantity()).isEqualTo(1);
        assertThat(gifts.get(0).price()).isEqualTo(25000);
        assertThat(totalGiftPrice).isEqualTo(25000);
    }

    @DisplayName("주문 금액이 정확히 120000원이면 샴페인이 증정된다")
    @Test
    void giveChampagneWhenOrderPriceExactly120000() {
        // given
        GiftProduct champagne = new GiftProduct("샴페인", 120000, 25000);
        List<GiftPolicy> policies = List.of(new PriceBasedPolicy(List.of(champagne)));
        GiftEvent giftEvent = new GiftEvent(policies);

        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish bbqRib = new Dish("바비큐립", 54000, DishType.MAIN);
        Dish tapas = new Dish("타파스", 5500, DishType.APPETIZER);
        Order order = new Order(Map.of(
                tBoneSteak, 1,
                bbqRib, 1,
                tapas, 2
        ));
        VisitDate visitDate = new VisitDate(3);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Gift> gifts = giftEvent.determineGifts(context);

        // then
        assertThat(gifts).hasSize(1);
        assertThat(gifts.get(0).name()).isEqualTo("샴페인");
    }

    @DisplayName("주문 금액이 119999원이면 샴페인이 증정되지 않는다")
    @Test
    void noChampagneWhenOrderPrice119999() {
        // given
        GiftProduct champagne = new GiftProduct("샴페인", 120000, 25000);
        List<GiftPolicy> policies = List.of(new PriceBasedPolicy(List.of(champagne)));
        GiftEvent giftEvent = new GiftEvent(policies);

        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish bbqRib = new Dish("바비큐립", 54000, DishType.MAIN);
        Dish tapas = new Dish("타파스", 5500, DishType.APPETIZER);
        Order order = new Order(Map.of(
                tBoneSteak, 1,
                bbqRib, 1,
                tapas, 1
        ));
        // Order total: 55000 + 54000 + 5500 = 114500
        VisitDate visitDate = new VisitDate(3);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Gift> gifts = giftEvent.determineGifts(context);

        // then
        assertThat(gifts).isEmpty();
    }

    @DisplayName("여러 가격 기준의 선물 정책이 있을 때 조건을 만족하는 모든 선물이 증정된다")
    @Test
    void giveMultipleGiftsWhenMultipleCriteriaMet() {
        // given
        GiftProduct champagne = new GiftProduct("샴페인", 120000, 25000);
        GiftProduct dessert = new GiftProduct("케이크", 50000, 10000);
        List<GiftPolicy> policies = List.of(
                new PriceBasedPolicy(List.of(champagne, dessert))
        );
        GiftEvent giftEvent = new GiftEvent(policies);

        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish bbqRib = new Dish("바비큐립", 54000, DishType.MAIN);
        Dish chocolateCake = new Dish("초코케이크", 15000, DishType.DESSERT);
        Order order = new Order(Map.of(
                tBoneSteak, 1,
                bbqRib, 1,
                chocolateCake, 1
        ));
        // Order total: 55000 + 54000 + 15000 = 124000
        VisitDate visitDate = new VisitDate(3);
        EventContext context = new EventContext(visitDate, order);

        // when
        List<Gift> gifts = giftEvent.determineGifts(context);
        int totalGiftPrice = giftEvent.getTotalGiftPrice(context);

        // then
        assertThat(gifts).hasSize(2);
        assertThat(gifts)
                .extracting(Gift::name)
                .containsExactlyInAnyOrder("샴페인", "케이크");
        assertThat(totalGiftPrice).isEqualTo(35000);
    }
}
