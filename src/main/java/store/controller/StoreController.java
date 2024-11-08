package store.controller;

import java.util.List;
import java.util.function.Supplier;
import store.dto.OrderDTO;
import store.dto.ProductDTO;
import store.model.InputConverter;
import store.model.order.Orders;
import store.model.product.Products;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final InputConverter inputConverter;
    private final Products products;

    public StoreController(InputView inputView, OutputView outputView, InputConverter inputConverter,
                           Products products) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.inputConverter = inputConverter;
        this.products = products;
    }

    public void operate() {
        outputView.printGreeting();
        List<ProductDTO> productDTOs = products.getProductDTOs();
        outputView.printProducts(productDTOs);

        Orders orders = retryUntilValid(() -> createOrders(productDTOs));
    }

    private Orders createOrders(List<ProductDTO> productDTOs) {
        String input = inputView.readItem();
        List<OrderDTO> orderDTOs = inputConverter.convertToOrderDTOs(input);
        return Orders.valueOf(orderDTOs, productDTOs);
    }

    private <T> T retryUntilValid(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }
}
