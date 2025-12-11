package christmas.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DishTest {

    @DisplayName("요리의 타입을 확인할 수 있다")
    @Test
    void checkDishType() {
        // given
        Dish dish = new Dish("티본스테이크", 55000, DishType.MAIN);

        // when
        boolean isMain = dish.typeEquals(DishType.MAIN);
        boolean isDessert = dish.typeEquals(DishType.DESSERT);

        // then
        assertThat(isMain).isTrue();
        assertThat(isDessert).isFalse();
    }

    @DisplayName("요리의 가격을 수량에 맞게 계산할 수 있다")
    @Test
    void calculatePrice() {
        // given
        Dish dish = new Dish("티본스테이크", 55000, DishType.MAIN);

        // when
        int price = dish.calculateBoughtPrice(2);

        // then
        assertThat(price).isEqualTo(110000);
    }

    @DisplayName("요리의 가격을 수량 1로 계산할 수 있다")
    @Test
    void calculatePriceWithOne() {
        // given
        Dish dish = new Dish("제로콜라", 3000, DishType.DRINK);

        // when
        int price = dish.calculateBoughtPrice(1);

        // then
        assertThat(price).isEqualTo(3000);
    }

    @DisplayName("요리의 이름을 비교할 수 있다")
    @Test
    void compareNameEquals() {
        // given
        Dish dish = new Dish("티본스테이크", 55000, DishType.MAIN);

        // when
        boolean result = dish.equalsNameOf("티본스테이크");

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("요리의 이름이 다르면 false를 반환한다")
    @Test
    void compareNameNotEquals() {
        // given
        Dish dish = new Dish("티본스테이크", 55000, DishType.MAIN);

        // when
        boolean result = dish.equalsNameOf("바비큐립");

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("같은 이름을 가진 요리는 동등하다")
    @Test
    void dishEqualsWithSameName() {
        // given
        Dish dish1 = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish dish2 = new Dish("티본스테이크", 55000, DishType.MAIN);

        // when
        boolean result = dish1.equals(dish2);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("다른 이름을 가진 요리는 동등하지 않다")
    @Test
    void dishNotEqualsWithDifferentName() {
        // given
        Dish dish1 = new Dish("티본스테이크", 55000, DishType.MAIN);
        Dish dish2 = new Dish("바비큐립", 54000, DishType.MAIN);

        // when
        boolean result = dish1.equals(dish2);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("요리와 다른 타입의 객체는 동등하지 않다")
    @Test
    void dishNotEqualsWithDifferentType() {
        // given
        Dish dish = new Dish("티본스테이크", 55000, DishType.MAIN);
        String notDish = "티본스테이크";

        // when
        boolean result = dish.equals(notDish);

        // then
        assertThat(result).isFalse();
    }
}
