package store.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import store.dto.OrderDTO;

public class InputConverter {

    public static final String ITEMS_DELIMITER = ",";
    public static final String ITEM_DELIMITER = "-";

    public List<OrderDTO> convertToOrderDTOs(String input) {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        Set<String> productNames = new HashSet<>();

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

            validateUniqueProductName(productNames, productName);

            try {
                int quantity = Integer.parseInt(quantityStr);
                orderDTOS.add(new OrderDTO(productName, quantity));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(ErrorCode.GENERAL_INVALID_INPUT.getMessage());
            }
        }

        return orderDTOS;
    }

    public boolean convertToBoolean(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new IllegalArgumentException(ErrorCode.GENERAL_INVALID_INPUT.getMessage());
        }
        return input.equals("Y");
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
