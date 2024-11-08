package store.model.product;

import store.dto.ProductDTO;

public class Product {
    private final String name;
    private final int price;
    private int quantity;
    private final String promotion;

    private Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public boolean isName(String name) {
        return this.name.equals(name);
    }

    public boolean isQuantityEqualOrGreaterThan(int quantity) {
        return this.quantity >= quantity;
    }

    public static Product fromDTO(ProductDTO productDTO) {
        return new Product(
                productDTO.name(),
                productDTO.price(),
                productDTO.quantity(),
                productDTO.promotion()
        );
    }

    public ProductDTO toDTO() {
        return new ProductDTO(name, price, quantity, promotion);
    }
}
