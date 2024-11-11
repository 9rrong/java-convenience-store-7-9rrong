package store.model.product;

import store.dto.ProductDTO;

public class Product {
    private final String name;
    private final int price;
    private int quantity;
    private final String promotion;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
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

    public boolean hasName(String name) {
        return this.name.equals(name);
    }

    public boolean hasPromotion() {
        return this.promotion != null;
    }

    public String getPromotion() {
        return this.promotion;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public int getPrice(){return this.price;}

    public void decreaseQuantity(int value){
        this.quantity -= value;
    }

    public boolean hasEqualOrMoreQuantityThan(int value) {
        return quantity >= value;
    }

}
