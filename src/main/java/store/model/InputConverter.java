package store.model;

import java.util.ArrayList;
import java.util.List;
import store.dto.PurchaseDTO;

public class InputConverter {

    public List<PurchaseDTO> convertToPurchaseDTOs(String input) {
        List<PurchaseDTO> purchases = new ArrayList<>();

        String[] items = input.split("\\],\\[");

        for (String item : items) {
            item = item.replaceAll("[\\[\\]]", "").trim();
            String[] parts = item.split("-");

            if (parts.length != 2) {
                throw new IllegalArgumentException(ErrorCode.INVALID_INPUT_FORMAT.getMessage());
            }

            String productName = parts[0].trim();
            int quantity;

            try {
                quantity = Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(ErrorCode.INVALID_INPUT_FORMAT.getMessage());
            }

            purchases.add(new PurchaseDTO(productName, quantity));
        }

        return purchases;
    }
}
