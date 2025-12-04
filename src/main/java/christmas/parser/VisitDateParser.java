package christmas.parser;

import christmas.domain.vo.VisitDate;
import christmas.exception.InvalidVisitDateException;

public class VisitDateParser implements Parser<VisitDate> {
    @Override
    public VisitDate parse(String date) {
        try {
            int dateValue = Integer.parseInt(date);
            return new VisitDate(dateValue);
        } catch (NumberFormatException e) {
            throw new InvalidVisitDateException();
        }
    }
}
