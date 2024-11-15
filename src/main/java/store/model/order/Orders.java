package store.model.order;

import java.util.List;
import java.util.stream.Collectors;
import store.dto.OrderDTO;
import store.dto.ProductDTO;

public class Orders {

    private final List<Order> orders;

    private Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders fromDTOs(List<OrderDTO> orderDTOs, List<ProductDTO> availableProducts) {
        return new Orders(orderDTOs
                .stream()
                .map(orderDTO -> Order.fromDTO(orderDTO, availableProducts))
                .toList());
    }

    public List<OrderDTO> getOrderDTOs() {
        return orders.stream()
                .map(Order::toDTO)
                .collect(Collectors.toUnmodifiableList());
    }
}
