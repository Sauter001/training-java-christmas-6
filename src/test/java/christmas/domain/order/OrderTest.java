package christmas.domain.order;

import christmas.exception.DiscountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @DisplayName("음료만 주문하면 예외가 발생한다")
    @Test
    void validateOnlyDrink() {
        // given
        Dish zeroCoke = new Dish("제로콜라", 3000, DishType.DRINK);
        Dish champagne = new Dish("샴페인", 25000, DishType.DRINK);
        Map<Dish, Integer> dishCounter = Map.of(
                zeroCoke, 2,
                champagne, 1
        );

        // when & then
        assertThatThrownBy(() -> new Order(dishCounter))
                .isInstanceOf(DiscountException.class);
    }

    @DisplayName("음료와 다른 메뉴를 함께 주문하면 정상적으로 주문된다")
    @Test
    void createOrderWithDrinkAndOther() {
        // given
        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish zeroCoke = new Dish("제로콜라", 3000, DishType.DRINK);
        Map<Dish, Integer> dishCounter = Map.of(
                tBoneSteak, 1,
                zeroCoke, 2
        );

        // when
        Order order = new Order(dishCounter);

        // then
        assertThat(order).isNotNull();
    }

    @DisplayName("특정 요리의 수량을 조회할 수 있다")
    @Test
    void findCountOfDish() {
        // given
        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish zeroCoke = new Dish("제로콜라", 3000, DishType.DRINK);
        Map<Dish, Integer> dishCounter = Map.of(
                tBoneSteak, 1,
                zeroCoke, 2
        );
        Order order = new Order(dishCounter);

        // when
        int tBoneSteakCount = order.findCountOf(tBoneSteak);
        int zeroCokeCount = order.findCountOf(zeroCoke);

        // then
        assertThat(tBoneSteakCount).isEqualTo(1);
        assertThat(zeroCokeCount).isEqualTo(2);
    }

    @DisplayName("주문하지 않은 요리의 수량은 0이다")
    @Test
    void findCountOfNotOrderedDish() {
        // given
        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish bbqRib = new Dish("바비큐립", 54000, DishType.MAIN);
        Map<Dish, Integer> dishCounter = Map.of(
                tBoneSteak, 1
        );
        Order order = new Order(dishCounter);

        // when
        int bbqRibCount = order.findCountOf(bbqRib);

        // then
        assertThat(bbqRibCount).isEqualTo(0);
    }

    @DisplayName("주문의 전체 가격을 계산할 수 있다")
    @Test
    void getFullPrice() {
        // given
        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish zeroCoke = new Dish("제로콜라", 3000, DishType.DRINK);
        Dish chocolateCake = new Dish("초코케이크", 15000, DishType.DESSERT);
        Map<Dish, Integer> dishCounter = Map.of(
                tBoneSteak, 1,      // 55,000
                zeroCoke, 2,        // 6,000
                chocolateCake, 2    // 30,000
        );
        Order order = new Order(dishCounter);

        // when
        int fullPrice = order.getFullPrice();

        // then
        assertThat(fullPrice).isEqualTo(91000);
    }

    @DisplayName("타입별 주문 수량을 카운트할 수 있다")
    @Test
    void countByType() {
        // given
        Dish tBoneSteak = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish bbqRib = new Dish("바비큐립", 54000, DishType.MAIN);
        Dish chocolateCake = new Dish("초코케이크", 15000, DishType.DESSERT);
        Dish zeroCoke = new Dish("제로콜라", 3000, DishType.DRINK);
        Map<Dish, Integer> dishCounter = Map.of(
                tBoneSteak, 1,
                bbqRib, 1,
                chocolateCake, 2,
                zeroCoke, 1
        );
        Order order = new Order(dishCounter);

        // when
        int mainCount = order.countByType(DishType.MAIN);
        int dessertCount = order.countByType(DishType.DESSERT);
        int drinkCount = order.countByType(DishType.DRINK);
        int appetizerCount = order.countByType(DishType.APPETIZER);

        // then
        assertThat(mainCount).isEqualTo(2);
        assertThat(dessertCount).isEqualTo(2);
        assertThat(drinkCount).isEqualTo(1);
        assertThat(appetizerCount).isEqualTo(0);
    }
}
