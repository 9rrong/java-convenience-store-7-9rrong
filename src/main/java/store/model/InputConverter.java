package store.model;

import java.util.ArrayList;
import java.util.List;
import store.dto.PurchaseDTO;

public class InputConverter {

    public static final String ITEMS_DELIMITER = ",";
    public static final String ITEM_DELIMITER = "-";

    public List<PurchaseDTO> convertToPurchaseDTOs(String input) {
        List<PurchaseDTO> purchases = new ArrayList<>();

        validateNonEmptyFormat(input);

        String[] items = input.split(ITEMS_DELIMITER);

        for (String item : items) {
            item = item.trim();

            validateItemFormat(item);

            item = item.substring(1, item.length() - 1).trim();
            String[] parts = item.split(ITEM_DELIMITER);

            validateProductAndQuantityFormat(parts);

            String productName = parts[0].trim();
            String quantityStr = parts[1].trim();

            validateNonEmptyProductName(productName);

            try {
                int quantity = Integer.parseInt(quantityStr);
                purchases.add(new PurchaseDTO(productName, quantity));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(ErrorCode.GENERAL_INVALID_INPUT.getMessage());
            }
        }

        return purchases;
    }

    private void validateNonEmptyProductName(String productName) {
        if (productName.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.GENERAL_INVALID_INPUT.getMessage());
        }
    }

    private void validateProductAndQuantityFormat(String[] parts) {
        if (parts.length != 2) {
            throw new IllegalArgumentException(ErrorCode.INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private void validateItemFormat(String item) {
        if (!item.startsWith("[") || !item.endsWith("]")) {
            throw new IllegalArgumentException(ErrorCode.INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private void validateNonEmptyFormat(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.GENERAL_INVALID_INPUT.getMessage());
        }
    }
}
