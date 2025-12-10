package christmas.exception;

public enum ErrorMessage {
    INVALID_VISIT_DATE("유효하지 않은 날짜입니다. 다시 입력해 주세요."),
    INVALID_ORDER("유효하지 않은 주문입니다. 다시 입력해 주세요."),
    ORDER_COUNT_EXCEED("최대 주문 개수를 초과했습니다."),
    ONLY_BEVERAGE_NOT_ALLOWED("음료만 주문할 수 없습니다.");
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
