package store.model.product;

import java.util.ArrayList;
import java.util.List;
import store.dto.ProductDTO;

public class Products {
    private final List<Product> products;

    private Products(List<Product> products) {
        this.products = products;
    }

    public static Products valueOf(List<ProductDTO> productDtos) {
        List<Product> productList = new ArrayList<>();

        for (ProductDTO productDTO : productDtos) {
            String name = productDTO.name();
            int price = productDTO.price();
            int quantity = productDTO.quantity();
            String promotion = productDTO.promotion();

            productList.add(new Product(name, price, quantity, promotion));
        }

        return new Products(productList);
    }
}
