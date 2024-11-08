package store.controller;

import java.util.List;
import java.util.function.Supplier;
import store.dto.ProductDTO;
import store.dto.PurchaseDTO;
import store.model.InputConverter;
import store.model.Inventory;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final InputConverter inputConverter;
    private final Inventory inventory;

    public StoreController(InputView inputView, OutputView outputView, InputConverter inputConverter,
                           Inventory inventory) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.inputConverter = inputConverter;
        this.inventory = inventory;
    }

    public void startStore() {
        outputView.printGreeting();
        List<ProductDTO> productDTOs = inventory.getProductDTO();
        outputView.printProducts(productDTOs);

        List<PurchaseDTO> purchaseDTOs = retryUntilValid(this::readPurchases);

    }

    private List<PurchaseDTO> readPurchases() {
        String input = inputView.readItem();
        return inputConverter.convertToPurchaseDTOs(input);
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
