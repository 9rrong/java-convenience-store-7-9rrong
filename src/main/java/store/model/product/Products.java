package store.model.product;

import java.util.List;
import store.dto.ProductDTO;

public class Products {
    private final List<Product> products;

    private Products(List<Product> products) {
        this.products = products;
    }

    public static Products valueOf(List<ProductDTO> productDTOs) {

        return new Products(productDTOs
                .stream()
                .map(Product::valueOf)
                .toList());
    }

    public List<ProductDTO> getProductDTOs() {

        return products
                .stream()
                .map(Product::toDTO)
                .toList();
    }
}
