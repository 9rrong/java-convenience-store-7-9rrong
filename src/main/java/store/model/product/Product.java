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

    public static Product valueOf(ProductDTO productDTO) {
        String name = productDTO.name();
        int price = productDTO.price();
        int quantity = productDTO.quantity();
        String promotion = productDTO.promotion();

        return new Product(name, price, quantity, promotion);
    }

    public ProductDTO toDTO() {
        return new ProductDTO(name, price, quantity, promotion);
    }
}
