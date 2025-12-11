package christmas.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilTest {

    @DisplayName("금요일은 주말로 판별된다")
    @Test
    void isFridayWeekend() {
        // given - 2023년 12월 1일은 금요일
        int year = 2023;
        int month = 12;
        int day = 1;

        // when
        boolean result = DateUtil.isWeekend(year, month, day);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("토요일은 주말로 판별된다")
    @Test
    void isSaturdayWeekend() {
        // given - 2023년 12월 2일은 토요일
        int year = 2023;
        int month = 12;
        int day = 2;

        // when
        boolean result = DateUtil.isWeekend(year, month, day);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("일요일은 주말이 아니다")
    @Test
    void isSundayNotWeekend() {
        // given - 2023년 12월 3일은 일요일
        int year = 2023;
        int month = 12;
        int day = 3;

        // when
        boolean result = DateUtil.isWeekend(year, month, day);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("월요일은 주말이 아니다")
    @Test
    void isMondayNotWeekend() {
        // given - 2023년 12월 4일은 월요일
        int year = 2023;
        int month = 12;
        int day = 4;

        // when
        boolean result = DateUtil.isWeekend(year, month, day);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("일요일로 판별된다")
    @Test
    void isSunday() {
        // given - 2023년 12월 3일은 일요일
        int year = 2023;
        int month = 12;
        int day = 3;

        // when
        boolean result = DateUtil.isSunday(year, month, day);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("월요일은 일요일이 아니다")
    @Test
    void isMondayNotSunday() {
        // given - 2023년 12월 4일은 월요일
        int year = 2023;
        int month = 12;
        int day = 4;

        // when
        boolean result = DateUtil.isSunday(year, month, day);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("12월 25일은 크리스마스로 판별된다")
    @Test
    void isChristmas() {
        // given
        int month = 12;
        int day = 25;

        // when
        boolean result = DateUtil.isChristmas(month, day);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("12월 24일은 크리스마스가 아니다")
    @Test
    void isNotChristmas() {
        // given
        int month = 12;
        int day = 24;

        // when
        boolean result = DateUtil.isChristmas(month, day);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("일요일은 특별한 날로 판별된다")
    @Test
    void isSundaySpecialDay() {
        // given - 2023년 12월 3일은 일요일
        int year = 2023;
        int month = 12;
        int day = 3;

        // when
        boolean result = DateUtil.isSpecialDay(year, month, day);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("크리스마스는 특별한 날로 판별된다")
    @Test
    void isChristmasSpecialDay() {
        // given - 2023년 12월 25일은 크리스마스 (월요일)
        int year = 2023;
        int month = 12;
        int day = 25;

        // when
        boolean result = DateUtil.isSpecialDay(year, month, day);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("월요일이지만 크리스마스가 아니면 특별한 날이 아니다")
    @Test
    void isMondayNotSpecialDay() {
        // given - 2023년 12월 4일은 월요일
        int year = 2023;
        int month = 12;
        int day = 4;

        // when
        boolean result = DateUtil.isSpecialDay(year, month, day);

        // then
        assertThat(result).isFalse();
    }
}
