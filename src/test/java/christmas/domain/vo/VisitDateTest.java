package christmas.domain.vo;

import christmas.exception.InvalidVisitDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VisitDateTest {

    @DisplayName("유효한 날짜로 VisitDate 객체를 생성할 수 있다")
    @ParameterizedTest
    @ValueSource(ints = {1, 15, 25, 31})
    void createValidVisitDate(int date) {
        // when
        VisitDate visitDate = new VisitDate(date);

        // then
        assertThat(visitDate.date()).isEqualTo(date);
    }

    @DisplayName("1보다 작은 날짜는 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void createVisitDateWithLessThanOne(int date) {
        // when & then
        assertThatThrownBy(() -> new VisitDate(date))
                .isInstanceOf(InvalidVisitDateException.class);
    }

    @DisplayName("31보다 큰 날짜는 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {32, 40, 100})
    void createVisitDateWithGreaterThanThirtyOne(int date) {
        // when & then
        assertThatThrownBy(() -> new VisitDate(date))
                .isInstanceOf(InvalidVisitDateException.class);
    }
}
