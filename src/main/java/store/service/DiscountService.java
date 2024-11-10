package store.service;

import java.util.Map;
import store.dto.OrderDTO;
import store.model.product.Product;
import store.model.product.Products;
import store.model.promotion.Promotion;
import store.model.promotion.Promotions;

public class DiscountService {
    private final Products products;
    private final Promotions promotions;

    public DiscountService(Products products, Promotions promotions) {
        this.products = products;
        this.promotions = promotions;
    }

    public boolean isPromotionEligible(OrderDTO orderDTO) {
        String productName = orderDTO.productName();
        Map<Boolean, Product> productsByName = products.findProductsByName(productName);
        Product productWithPromotion = productsByName.get(true);

        if (productWithPromotion == null) {
            return false;
        }

        Promotion promotion = promotions.findPromotionByName(productWithPromotion.getPromotion());

        return promotion.isNowAvailable();
    }

    public boolean isPromotionProductsQuantityEnough(OrderDTO orderDTO) {
        String productName = orderDTO.productName();
        int orderQuantity = orderDTO.quantity();
        Map<Boolean, Product> productsByName = products.findProductsByName(productName);
        Product productWithPromotion = productsByName.get(true);
        int availableQuantity = productWithPromotion.getQuantity();

        return orderQuantity < availableQuantity;
    }

    public OrderDTO processAddPromotionProduct(OrderDTO orderDTO, boolean addProduct) {
        String productName = orderDTO.productName();
        int quantity = orderDTO.quantity();
        Map<Boolean, Product> productsByName = products.findProductsByName(productName);
        Product productWithPromotion = productsByName.get(true);
        Promotion promotion = promotions.findPromotionByName(productWithPromotion.getPromotion());
        int get = promotion.getGet();

        if (addProduct) {
            return new OrderDTO(productName, quantity + get);
        }
        return new OrderDTO(productName, quantity);
    }

    public boolean canAddPromotionProduct(OrderDTO orderDTO) {
        String productName = orderDTO.productName();
        int orderQuantity = orderDTO.quantity();

        Map<Boolean, Product> productsByName = products.findProductsByName(productName);
        Product productWithPromotion = productsByName.get(true);
        Promotion promotion = promotions.findPromotionByName(productWithPromotion.getPromotion());
        int availableQuantity = productWithPromotion.getQuantity();

        return isEligibleForFreeItems(promotion, availableQuantity, orderQuantity);
    }

    public int getMaxPromotionQuantity(OrderDTO orderDTO) {
        String productName = orderDTO.productName();
        Map<Boolean, Product> productsByName = products.findProductsByName(productName);
        Product productWithPromotion = productsByName.get(true);
        Promotion promotion = promotions.findPromotionByName(productWithPromotion.getPromotion());
        int availableQuantity = productWithPromotion.getQuantity();

        int buy = promotion.getBuy();
        int get = promotion.getGet();

        return availableQuantity / (buy + get) * (buy + get);
    }

    public OrderDTO processBuyOnlyPromotionProduct(OrderDTO orderDTO, int nonPromotionQuantity) {
        return new OrderDTO(orderDTO.productName(), orderDTO.quantity() - nonPromotionQuantity);
    }

    public OrderDTO processBuyNonPromotionProduct(OrderDTO orderDTO, int nonPromotionQuantity) {
        return new OrderDTO(orderDTO.productName(), nonPromotionQuantity);
    }

    private boolean isEligibleForFreeItems(Promotion promotion, int availableQuantity, int quantity) {
        int buy = promotion.getBuy();
        int get = promotion.getGet();

        if (quantity % (buy + get) != buy) {
            return false;
        }

        int totalItemsWithPromotion = quantity + get;
        return totalItemsWithPromotion <= availableQuantity;
    }
}
