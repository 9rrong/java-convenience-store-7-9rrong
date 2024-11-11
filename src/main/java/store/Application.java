package store;

import store.controller.StoreController;
import store.model.InputConverter;
import store.model.dataloader.ProductDataLoader;
import store.model.dataloader.PromotionDataLoader;
import store.model.product.Products;
import store.model.promotion.Promotions;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    private static final String PRODUCTS_FILE_PATH = "products.md";
    private static final String PROMOTIONS_FILE_PATH = "promotions.md";

    public static void main(String[] args) {
        new Application().run();
    }

    private void run() {
        StoreController storeController = initializeComponents();
        storeController.operate();
    }

    private StoreController initializeComponents() {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        ProductDataLoader productDataLoader = new ProductDataLoader(PRODUCTS_FILE_PATH);
        PromotionDataLoader promotionDataLoader = new PromotionDataLoader(PROMOTIONS_FILE_PATH);
        Promotions promotions = Promotions.withLoaders(promotionDataLoader);
        Products products = Products.withLoader(productDataLoader);
        InputConverter inputConverter = new InputConverter();

        return new StoreController(inputView, outputView, inputConverter, products, promotions
        );
    }
}
