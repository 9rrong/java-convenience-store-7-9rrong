package store.model.product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.dto.ProductDTO;
import store.model.dataloader.AbstractDataLoader;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public static Products withLoader(AbstractDataLoader<ProductDTO> productDataLoader) {
        List<Product> products = Products.fromDTOs(productDataLoader.loadFromFile());
        return new Products(products);
    }

    public List<ProductDTO> toDTOs() {
        return products
                .stream()
                .map(Product::toDTO)
                .collect(Collectors.toUnmodifiableList());
    }

    public Map<Boolean, Product> findProductsByName(String name) {
        return products.stream()
                .filter(product -> product.hasName(name))
                .collect(Collectors.toMap(
                        Product::hasPromotion,
                        product -> product
                ));
    }

    private static List<Product> fromDTOs(List<ProductDTO> productDTOs) {
        return productDTOs
                .stream()
                .map(Product::fromDTO)
                .collect(Collectors.toUnmodifiableList());
    }
}
