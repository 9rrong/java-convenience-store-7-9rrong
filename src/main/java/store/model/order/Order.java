package store.model.order;

import java.util.List;
import store.dto.OrderDTO;
import store.dto.ProductDTO;
import store.model.ErrorCode;

public class Order {
    private final String name;
    private final int quantity;

    private Order(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public static Order fromDTO(OrderDTO orderDTO, List<ProductDTO> availableProducts) {
        String name = orderDTO.productName();
        int quantity = orderDTO.quantity();

        validateOrder(name, quantity, availableProducts);

        return new Order(name, quantity);
    }

    private static void validateOrder(String productName, int quantity, List<ProductDTO> availableProducts) {
        ProductDTO product = availableProducts.stream()
                .filter(p -> p.name().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));

        if (quantity > product.quantity()) {
            throw new IllegalArgumentException(ErrorCode.QUANTITY_EXCEEDS_STOCK.getMessage());
        }
    }
}
