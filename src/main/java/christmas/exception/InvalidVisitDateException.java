package christmas.exception;

public class InvalidVisitDateException extends DiscountException {
    public InvalidVisitDateException() {
        super(ErrorMessage.INVALID_VISIT_DATE);
    }
}
