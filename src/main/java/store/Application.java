package store;

import store.controller.StoreController;
import store.model.InputConverter;
import store.model.Inventory;
import store.model.dataloader.ProductDataLoader;
import store.model.dataloader.PromotionDataLoader;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        ProductDataLoader productDataLoader = new ProductDataLoader("products.md");
        PromotionDataLoader promotionDataLoader = new PromotionDataLoader("promotions.md");
        Inventory inventory = new Inventory(productDataLoader);
        InputConverter inputConverter = new InputConverter();

        StoreController storeController = new StoreController(inputView, outputView, inputConverter, inventory);

        storeController.startStore();
    }
}
