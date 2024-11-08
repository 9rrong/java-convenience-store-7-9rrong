package store.model.order;

import store.dto.OrderDTO;

public class Order {
    private final String name;
    private final int quantity;

    public Order(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public static Order valueOf(OrderDTO orderDTO) {
        String name = orderDTO.productName();
        int quantity = orderDTO.quantity();

        return new Order(name, quantity);
    }

}
