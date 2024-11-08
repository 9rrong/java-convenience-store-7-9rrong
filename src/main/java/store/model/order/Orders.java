package store.model.order;

import java.util.List;
import store.dto.OrderDTO;

public class Orders {

    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders valueOf(List<OrderDTO> orderDTOs) {
        return new Orders(orderDTOs
                .stream()
                .map(Order::valueOf)
                .toList());
    }
}
