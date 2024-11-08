package store.model.product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import store.dto.ProductDTO;
import store.model.ErrorCode;
import store.model.dataloader.AbstractDataLoader;

public class Products {
    private final List<Product> products;
    private final AbstractDataLoader<ProductDTO> productDataLoader;

    public Products(AbstractDataLoader<ProductDTO> productDataLoader) {
        this.productDataLoader = productDataLoader;
        this.products = loadInitialProducts();
    }

    public List<ProductDTO> toDTOs() {
        return products
                .stream()
                .map(Product::toDTO)
                .collect(Collectors.toUnmodifiableList());
    }

    public Optional<Product> findProductByName(String name) {
        return products.stream()
                .filter(product -> product.isName(name))
                .findFirst();
    }

    public boolean isAvailable(String productName, int quantity) {
        return findProductByName(productName)
                .map(product -> product.isQuantityEqualOrGreaterThan(quantity))
                .orElse(false);
    }

    public void reduceStock(String productName, int quantity) {
        Product product = findProductByName(productName)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));

        product.reduceQuantity(quantity);
    }

    private List<Product> loadInitialProducts() {
        return fromDTOs(productDataLoader.loadFromFile());
    }

    private List<Product> fromDTOs(List<ProductDTO> productDTOs) {
        return productDTOs
                .stream()
                .map(Product::fromDTO)
                .collect(Collectors.toUnmodifiableList());
    }
}
