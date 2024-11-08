package store.model.product;

import java.util.List;
import java.util.stream.Collectors;
import store.dto.ProductDTO;
import store.model.dataloader.AbstractDataLoader;

public class Products {
    private final List<Product> products;
    private final AbstractDataLoader<ProductDTO> productDataLoader;

    public Products(AbstractDataLoader<ProductDTO> productDataLoader) {
        this.productDataLoader = productDataLoader;
        this.products = loadInitialProducts();
    }

    private static List<Product> fromDTOs(List<ProductDTO> productDTOs) {
        return productDTOs
                .stream()
                .map(Product::fromDTO)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<ProductDTO> getProductDTOs() {
        return products
                .stream()
                .map(Product::toDTO)
                .collect(Collectors.toUnmodifiableList());
    }

    private List<Product> loadInitialProducts() {
        return fromDTOs(productDataLoader.loadFromFile());
    }
}
