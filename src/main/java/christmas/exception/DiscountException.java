package christmas.exception;

public class DiscountException extends IllegalArgumentException {
    public DiscountException(ErrorMessage errorMessage) {
        super(formatErrorMessage(errorMessage));
    }

    private static String formatErrorMessage(ErrorMessage errorMessage) {
        return String.format("[ERROR] %s\n", errorMessage.getMessage());
    }
}
