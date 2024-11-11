package store.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import store.dto.OrderDTO;

public class InputConverter {

    private static final String ITEMS_DELIMITER = ",";
    private static final String ITEM_DELIMITER = "-";
    private static final String YES_SIGN = "Y";
    private static final String NO_SIGN = "N";
    private static final String ITEM_START = "[";
    private static final String ITEM_END = "]";
    private static final int FORMAT_SIZE = 2;
    private static final int START_INDEX = 1;
    private static final int END_INDEX_OFFSET = 1;
    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int PRODUCT_QUANTITY_INDEX = 1;

    public List<OrderDTO> convertToOrderDTOs(String input) {
        List<OrderDTO> orderDTOs = new ArrayList<>();
        Set<String> productNames = new HashSet<>();

        validateNonEmptyFormat(input);

        String[] items = input.split(ITEMS_DELIMITER);

        for (String item : items) {
            item = item.trim();

            validateItemFormat(item);

            item = item.substring(START_INDEX, item.length() - END_INDEX_OFFSET).trim();
            String[] parts = item.split(ITEM_DELIMITER);

            validateProductAndQuantityFormat(parts);

            String productName = parts[PRODUCT_NAME_INDEX].trim();
            String productQuantity = parts[PRODUCT_QUANTITY_INDEX].trim();

            validateNonEmptyProductName(productName);
            validateUniqueProductName(productNames, productName);

            try {
                int quantity = Integer.parseInt(productQuantity);
                orderDTOs.add(new OrderDTO(productName, quantity));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(ErrorCode.GENERAL_INVALID_INPUT.getMessage());
            }
        }
        return orderDTOs;
    }

    public boolean convertToBoolean(String input) {
        if (!input.equals(YES_SIGN) && !input.equals(NO_SIGN)) {
            throw new IllegalArgumentException(ErrorCode.GENERAL_INVALID_INPUT.getMessage());
        }
        return input.equals(YES_SIGN);
    }

    private void validateUniqueProductName(Set<String> productNames, String productName) {
        if (!productNames.add(productName)) {
            throw new IllegalArgumentException(ErrorCode.GENERAL_INVALID_INPUT.getMessage());
        }
    }

    private void validateNonEmptyProductName(String productName) {
        if (productName.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.GENERAL_INVALID_INPUT.getMessage());
        }
    }

    private void validateProductAndQuantityFormat(String[] parts) {
        if (parts.length != FORMAT_SIZE) {
            throw new IllegalArgumentException(ErrorCode.INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private void validateItemFormat(String item) {
        if (!item.startsWith(ITEM_START) || !item.endsWith(ITEM_END)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private void validateNonEmptyFormat(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.GENERAL_INVALID_INPUT.getMessage());
        }
    }
}
