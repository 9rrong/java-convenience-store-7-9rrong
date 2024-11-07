package store.dto;

public record ProductDTO(
        String name,
        int price,
        int quantity,
        String promotion
) {
}
