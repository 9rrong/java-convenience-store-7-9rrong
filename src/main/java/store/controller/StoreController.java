package store.controller;

import java.util.List;
import java.util.function.Supplier;
import store.dto.ProductDTO;
import store.dto.OrderDTO;
import store.model.InputConverter;
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

    public void startStore() {
        outputView.printGreeting();
        List<ProductDTO> productDTOs = products.getProductDTOs();
        outputView.printProducts(productDTOs);

        List<OrderDTO> orderDTOs = retryUntilValid(this::readOrders);

    }

    private List<OrderDTO> readOrders() {
        String input = inputView.readItem();
        return inputConverter.convertToOrderDTOs(input);
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
