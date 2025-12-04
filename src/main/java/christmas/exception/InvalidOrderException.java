package christmas.exception;

public class InvalidOrderException extends DiscountException {
    public InvalidOrderException() {
        super(ErrorMessage.INVALID_ORDER);
    }
}
