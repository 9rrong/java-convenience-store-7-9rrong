package store.model;

public enum ErrorCode {
    INVALID_INPUT_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    PRODUCT_NOT_FOUND("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    QUANTITY_EXCEEDS_INVENTORY("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    GENERAL_INVALID_INPUT("잘못된 입력입니다. 다시 입력해 주세요.");

    private static final String ERROR_PREFIX = "[ERROR] ";

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return ERROR_PREFIX + message;
    }
}
