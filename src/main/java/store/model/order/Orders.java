package store.model.order;

import java.util.List;
import store.dto.OrderDTO;
import store.dto.ProductDTO;

public class Orders {

    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders valueOf(List<OrderDTO> orderDTOs, List<ProductDTO> availableProducts) {
        return new Orders(orderDTOs
                .stream()
                .map(orderDTO -> Order.createOrder(orderDTO, availableProducts))
                .toList());
    }
}
