package christmas.domain.vo;

import christmas.exception.DiscountException;
import christmas.exception.ErrorMessage;
import christmas.exception.InvalidVisitDateException;

public record VisitDate(int date) {
    private static final int START_DATE = 1;
    private static final int END_DATE = 31;

    public VisitDate {
        validateRange();
    }

    private void validateRange() {
        if (date < START_DATE || date > END_DATE) {
            throw new InvalidVisitDateException();
        }
    }
}
