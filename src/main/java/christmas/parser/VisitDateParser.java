package christmas.parser;

import christmas.domain.vo.VisitDate;
import christmas.exception.InvalidVisitDateException;

public class VisitDateParser implements Parser<VisitDate> {
    @Override
    public VisitDate parse(String date) {
        try {
            String strippedDate = date.strip();
            int dateValue = Integer.parseInt(strippedDate);
            return new VisitDate(dateValue);
        } catch (NumberFormatException e) {
            throw new InvalidVisitDateException();
        }
    }
}
