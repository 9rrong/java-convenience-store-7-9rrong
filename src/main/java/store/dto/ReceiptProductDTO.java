package store.dto;

public record ReceiptProductDTO(
        String name,
        int quantity,
        int price
) {
}
