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
    private int promotionProductQuantity;

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
            this.promotionProductQuantity = getMaxPromotionQuantity();
        }
    }

    public boolean promotionIsNotAvailable() {
        return productWithPromotion == null || !promotion.isNowAvailable();
    }

    public ReceiptProductDTO getDefaultOrder() {
        return new ReceiptProductDTO(productName, orderQuantity, price);
    }

    public ReceiptProductDTO getPromotionAddedOrder() {
        return new ReceiptProductDTO(productName, promotionProductQuantity + get, price);
    }

    public ReceiptProductDTO getPromotionOrder() {
        return new ReceiptProductDTO(productName, promotionProductQuantity, price);
    }

    public String getProductName() {
        return productName;
    }

    public int getNonPromotionProductQuantity() {
        return orderQuantity - availableQuantity;
    }

    public boolean promotionProductIsEnough() {
        return orderQuantity <= availableQuantity;
    }

    public boolean isEligibleForFreeItems() {

        if (orderQuantity % (buy + get) != buy) {
            return false;
        }

        int totalItemsWithPromotion = orderQuantity + get;
        return totalItemsWithPromotion <= availableQuantity;
    }

    private int getMaxPromotionQuantity() {
        return availableQuantity / (buy + get) * (buy + get);
    }
}
