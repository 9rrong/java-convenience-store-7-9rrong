package store.view;

import java.util.List;
import store.dto.ProductDTO;

public class OutputView {

    private static final String GREETING_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String AVAILABLE_PRODUCTS_MESSAGE = "현재 보유하고 있는 상품입니다." + System.lineSeparator();
    private static final String CURRENCY_FORMAT = "원";
    private static final String QUANTITY_SUFFIX = "개";
    private static final String NO_STOCK = " 재고 없음";
    private static final String PRODUCT_FORMAT_NO_QUANTITY = "- %s %,d" + CURRENCY_FORMAT + NO_STOCK;
    private static final String PRODUCT_FORMAT_NO_PROMOTION = "- %s %,d" + CURRENCY_FORMAT + " %d" + QUANTITY_SUFFIX;
    private static final String PRODUCT_FORMAT_WITH_PROMOTION =
            "- %s %,d" + CURRENCY_FORMAT + " %d" + QUANTITY_SUFFIX + " %s";
    public static final String RECEIPT_STORE_NAME = "==============W 편의점================";
    public static final String RECEIPT_HEADER_FORMAT = "%-20s%-10s%-15s";
    public static final String RECEIPT_PRODUCT_FORMAT = "%-20s%,-10d%,-15d";
    public static final String RECEIPT_PROMOTION_PRODUCT_FORMAT = "%-20s%,-10d";
    public static final String RECEIPT_PROMOTION_HEADER = "=============증\t    정===============";
    public static final String RECEIPT_FOOTER_FORMAT = "%-30s%,-10d";
    public static final String RECEIPT_TOTAL_LABEL = "총구매액";
    public static final String RECEIPT_DISCOUNT_LABEL = "행사할인";
    public static final String RECEIPT_MEMBERSHIP_DISCOUNT_LABEL = "멤버십할인";
    public static final String RECEIPT_FINAL_AMOUNT_LABEL = "내실돈";
    public static final String RECEIPT_PARTITION_LINE = "====================================";

    public void printGreeting() {
        System.out.println(GREETING_MESSAGE);
    }

    public void printProducts(List<ProductDTO> productDTOS) {
        System.out.println(AVAILABLE_PRODUCTS_MESSAGE);

        for (ProductDTO productDTO : productDTOS) {
            System.out.println(buildProductString(
                    productDTO.name(),
                    productDTO.price(),
                    productDTO.quantity(),
                    productDTO.promotion())
            );
        }
    }

    public void printError(String message) {
        System.out.println(message);
    }

    public void printReceipt(List<String> receiptLines) {
        for (String receiptLine : receiptLines) {
            System.out.println(receiptLine);
        }
    }

    private String buildProductString(String name, int price, int quantity, String promotion) {

        if (quantity == 0) {
            return String.format(PRODUCT_FORMAT_NO_QUANTITY, name, price);
        }

        if (promotion == null) {
            return String.format(PRODUCT_FORMAT_NO_PROMOTION, name, price, quantity);
        }

        return String.format(PRODUCT_FORMAT_WITH_PROMOTION, name, price, quantity, promotion);
    }
}
