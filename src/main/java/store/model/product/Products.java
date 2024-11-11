package store.model.product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.dto.ProductDTO;
import store.model.dataloader.AbstractDataLoader;

public class Products {
    private static final int MIN_QUANTITY = 0;

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

    public void decreaseProductQuantityByName(String name, int quantity) {
        Map<Boolean, Product> productsByType = findProductsByName(name);
        Product promotionProduct = productsByType.get(true);
        Product nonPromotionProduct = productsByType.get(false);

        quantity = decreasePromotionProductQuantity(promotionProduct, quantity);

        if (isRemaining(quantity)) {
            decreaseNonPromotionProductQuantity(nonPromotionProduct, quantity);
        }
    }

    private int decreasePromotionProductQuantity(Product promotionProduct, int quantity) {
        if (promotionProduct != null) {
            int availablePromotionQuantity = promotionProduct.getQuantity();
            if (promotionProduct.hasEqualOrMoreQuantityThan(quantity)) {
                promotionProduct.decreaseQuantity(quantity);
                return 0;
            }
            promotionProduct.decreaseQuantity(availablePromotionQuantity);
            return quantity - availablePromotionQuantity;
        }
        return quantity;
    }

    private void decreaseNonPromotionProductQuantity(Product nonPromotionProduct, int quantity) {
        if (nonPromotionProduct.hasEqualOrMoreQuantityThan(quantity)) {
            nonPromotionProduct.decreaseQuantity(quantity);
        }
    }


    private static boolean isRemaining(int quantity) {
        return quantity > MIN_QUANTITY;
    }

    private static List<Product> fromDTOs(List<ProductDTO> productDTOs) {
        Map<String, List<ProductDTO>> productDTOsByName = groupByProductName(productDTOs);

        productDTOsByName.values().forEach(Products::ensureNonPromotionProduct);

        return convertToProductList(productDTOsByName);
    }

    private static Map<String, List<ProductDTO>> groupByProductName(List<ProductDTO> productDTOs) {
        return productDTOs.stream()
                .collect(Collectors.groupingBy(ProductDTO::name));
    }

    private static void ensureNonPromotionProduct(List<ProductDTO> products) {
        if (products.stream()
                .noneMatch(productDTO -> productDTO.promotion() == null)) {
            ProductDTO productDTO = products.getFirst();
            products.add(new ProductDTO(productDTO.name(), productDTO.price(), 0, null));
        }
    }

    private static List<Product> convertToProductList(Map<String, List<ProductDTO>> productDTOsByName) {
        return productDTOsByName.values().stream()
                .flatMap(List::stream)
                .map(Product::fromDTO)
                .collect(Collectors.toUnmodifiableList());
    }

}
