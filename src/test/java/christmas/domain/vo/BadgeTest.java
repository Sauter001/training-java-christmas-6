package christmas.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BadgeTest {

    @DisplayName("총 혜택 금액이 20000원 이상이면 산타 배지를 받는다")
    @Test
    void getSantaBadgeWhenBenefitIsOverTwentyThousand() {
        // given
        int totalBenefit = 20000;

        // when
        Badge badge = Badge.from(totalBenefit);

        // then
        assertThat(badge).isEqualTo(Badge.SANTA);
        assertThat(badge.getName()).isEqualTo("산타");
    }

    @DisplayName("총 혜택 금액이 20000원을 초과하면 산타 배지를 받는다")
    @Test
    void getSantaBadgeWhenBenefitIsMoreThanTwentyThousand() {
        // given
        int totalBenefit = 25000;

        // when
        Badge badge = Badge.from(totalBenefit);

        // then
        assertThat(badge).isEqualTo(Badge.SANTA);
    }

    @DisplayName("총 혜택 금액이 10000원 이상 20000원 미만이면 트리 배지를 받는다")
    @Test
    void getTreeBadgeWhenBenefitIsBetweenTenAndTwentyThousand() {
        // given
        int totalBenefit = 10000;

        // when
        Badge badge = Badge.from(totalBenefit);

        // then
        assertThat(badge).isEqualTo(Badge.TREE);
        assertThat(badge.getName()).isEqualTo("트리");
    }

    @DisplayName("총 혜택 금액이 19999원이면 트리 배지를 받는다")
    @Test
    void getTreeBadgeWhenBenefitIsNineteenThousandNineHundredNinetyNine() {
        // given
        int totalBenefit = 19999;

        // when
        Badge badge = Badge.from(totalBenefit);

        // then
        assertThat(badge).isEqualTo(Badge.TREE);
    }

    @DisplayName("총 혜택 금액이 5000원 이상 10000원 미만이면 별 배지를 받는다")
    @Test
    void getStarBadgeWhenBenefitIsBetweenFiveAndTenThousand() {
        // given
        int totalBenefit = 5000;

        // when
        Badge badge = Badge.from(totalBenefit);

        // then
        assertThat(badge).isEqualTo(Badge.STAR);
        assertThat(badge.getName()).isEqualTo("별");
    }

    @DisplayName("총 혜택 금액이 9999원이면 별 배지를 받는다")
    @Test
    void getStarBadgeWhenBenefitIsNineThousandNineHundredNinetyNine() {
        // given
        int totalBenefit = 9999;

        // when
        Badge badge = Badge.from(totalBenefit);

        // then
        assertThat(badge).isEqualTo(Badge.STAR);
    }

    @DisplayName("총 혜택 금액이 5000원 미만이면 배지를 받지 못한다")
    @Test
    void getNoneBadgeWhenBenefitIsLessThanFiveThousand() {
        // given
        int totalBenefit = 4999;

        // when
        Badge badge = Badge.from(totalBenefit);

        // then
        assertThat(badge).isEqualTo(Badge.NONE);
        assertThat(badge.getName()).isEqualTo("없음");
    }

    @DisplayName("총 혜택 금액이 0원이면 배지를 받지 못한다")
    @Test
    void getNoneBadgeWhenBenefitIsZero() {
        // given
        int totalBenefit = 0;

        // when
        Badge badge = Badge.from(totalBenefit);

        // then
        assertThat(badge).isEqualTo(Badge.NONE);
    }
}
