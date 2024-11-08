package store.model;

import java.util.List;
import store.dto.ProductDTO;
import store.model.dataloader.AbstractDataLoader;
import store.model.product.Products;

public class Inventory {
    private final Products products;
    private final AbstractDataLoader<ProductDTO> productDataLoader;

    public Inventory(AbstractDataLoader<ProductDTO> productDataLoader) {
        this.productDataLoader = productDataLoader;
        this.products = loadInitialProducts();
    }

    public List<ProductDTO> getProductDTO() {
        return products.getProductDTOs();
    }

    private Products loadInitialProducts() {
        return Products.valueOf(productDataLoader.loadFromFile());
    }
}
