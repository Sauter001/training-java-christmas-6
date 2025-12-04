package christmas.domain.vo;

import christmas.exception.InvalidVisitDateException;

public record VisitDate(int date) {
    private static final int START_DATE = 1;
    private static final int END_DATE = 31;

    public VisitDate {
        validateRange(date);
    }

    private void validateRange(int date) {
        if (date < START_DATE || date > END_DATE) {
            throw new InvalidVisitDateException();
        }
    }
}
