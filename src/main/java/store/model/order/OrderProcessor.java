package store.model.order;

import store.dto.OrderDTO;
import store.dto.ReceiptProductDTO;
import store.model.product.Product;
import store.model.product.Products;
import store.model.promotion.Promotion;
import store.model.promotion.Promotions;

public class OrderProcessor {
    private final String productName;
    private final int orderQuantity;
    private final Product productWithPromotion;
    private final Product productWithNonPromotion;
    private int price;
    private Promotion promotion;
    private int buy;
    private int get;
    private int availableQuantity;

    public OrderProcessor(Products products, Promotions promotions, OrderDTO orderDTO) {
        this.productName = orderDTO.productName();
        this.orderQuantity = orderDTO.quantity();
        this.productWithNonPromotion = products.findProductsByName(productName).get(false);
        this.productWithPromotion = products.findProductsByName(productName).get(true);
        this.price = productWithNonPromotion.getPrice();

        if (productWithPromotion != null) {
            this.price = productWithPromotion.getPrice();
            this.promotion = promotions.findPromotionByName(productWithPromotion.getPromotion());
            this.buy = promotion.getBuy();
            this.get = promotion.getGet();
            this.availableQuantity = productWithPromotion.getQuantity();
        }
    }

    public boolean promotionIsNotAvailable() {
        return productWithPromotion == null || !promotion.isNowAvailable();
    }

    public ReceiptProductDTO getDefaultOrder() {
        return new ReceiptProductDTO(productName, orderQuantity, price);
    }

    public ReceiptProductDTO getProductAddedPromotionOrder() {
        return new ReceiptProductDTO(productName, getOrderPromotionGetQuantity() + get, price);
    }

    public ReceiptProductDTO getDefaultPromotionOrder() {
        return new ReceiptProductDTO(productName, getOrderPromotionGetQuantity(), price);
    }

    public ReceiptProductDTO getProductAddedTotalOrder(){
        return new ReceiptProductDTO(productName, orderQuantity + get, price);
    }

    public ReceiptProductDTO getPromotionAddedOrder() {
        return new ReceiptProductDTO(productName, getPromotionGetQuantity() + get, price);
    }

    public ReceiptProductDTO getPromotionOrder() {
        return new ReceiptProductDTO(productName, getPromotionQuantity(), price);
    }

    public ReceiptProductDTO getPromotionGetOrder() {
        return new ReceiptProductDTO(productName, getPromotionGetQuantity(), price);
    }

    public String getProductName() {
        return productName;
    }

    public int getNonPromotionProductQuantity() {
        return orderQuantity - getPromotionQuantity();
    }

    public boolean promotionProductIsEnough() {
        return getMaxOrderPromotionGetQuantity() <= getPromotionGetQuantity();
    }

    public boolean isEligibleForFreeItems() {

        if (orderQuantity % (buy + get) != buy) {
            return false;
        }

        int totalItemsWithPromotion = orderQuantity + get;
        return totalItemsWithPromotion <= availableQuantity;
    }

    private int getMaxOrderPromotionGetQuantity() {
        return orderQuantity / (buy) * (get);
    }

    private int getOrderPromotionGetQuantity() {
        return orderQuantity / (buy + get) * (get);
    }

    private int getPromotionGetQuantity() {
        return availableQuantity / (buy + get) * (get);
    }

    private int getPromotionQuantity(){
        return availableQuantity / (buy + get) * (buy + get);
    }
}
