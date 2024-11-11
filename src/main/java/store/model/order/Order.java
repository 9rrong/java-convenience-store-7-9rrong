package store.model.order;

import java.util.List;
import java.util.NoSuchElementException;
import store.dto.OrderDTO;
import store.dto.ProductDTO;
import store.model.ErrorCode;

public class Order {
    private final String productName;
    private final int quantity;

    private Order(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public OrderDTO toDTO() {
        return new OrderDTO(productName, quantity);
    }

    public static Order fromDTO(OrderDTO orderDTO, List<ProductDTO> availableProducts) {
        String name = orderDTO.productName();
        int quantity = orderDTO.quantity();

        validateOrder(name, quantity, availableProducts);

        return new Order(name, quantity);
    }

    private static void validateOrder(String productName, int quantity, List<ProductDTO> availableProducts) {
        int availableQuantity = availableProducts.stream()
                .filter(p -> p.name().equals(productName))
                .mapToInt(ProductDTO::quantity)
                .sum();

        if (availableQuantity == 0) {
            throw new NoSuchElementException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }

        if (quantity > availableQuantity) {
            throw new IllegalArgumentException(ErrorCode.QUANTITY_EXCEEDS_STOCK.getMessage());
        }
    }
}
