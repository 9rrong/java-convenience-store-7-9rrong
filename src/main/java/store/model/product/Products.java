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
        Product promotionProduct = findProductsByName(name).get(true);
        Product nonPromotionProduct = findProductsByName(name).get(false);

        if (promotionProduct != null) {
            int availablePromotionQuantity = promotionProduct.getQuantity();
            if (promotionProduct.hasEqualOrMoreQuantityThan(quantity)) {
                promotionProduct.decreaseQuantity(quantity);
                return;
            }
            promotionProduct.decreaseQuantity(availablePromotionQuantity);
            quantity -= availablePromotionQuantity;
        }

        if (isRemaining(quantity) && nonPromotionProduct.hasEqualOrMoreQuantityThan(quantity)) {
                nonPromotionProduct.decreaseQuantity(quantity);
        }
    }

    private static boolean isRemaining(int quantity) {
        return quantity > MIN_QUANTITY;
    }

    private static List<Product> fromDTOs(List<ProductDTO> productDTOs) {
        Map<String, List<ProductDTO>> productDTOsByName = productDTOs.stream()
                .collect(Collectors.groupingBy(ProductDTO::name));

        productDTOsByName.values().forEach(products -> {
            boolean hasNonPromotionProduct = products.stream()
                    .anyMatch(productDTO -> productDTO.promotion() == null);

            if (!hasNonPromotionProduct) {
                ProductDTO productDTO = products.getFirst();
                ProductDTO nonPromotionProductDTO = new ProductDTO(
                        productDTO.name(),
                        productDTO.price(),
                        0,
                        null
                );
                products.add(nonPromotionProductDTO);
            }
        });

        return productDTOsByName.values().stream()
                .flatMap(List::stream)
                .map(Product::fromDTO)
                .collect(Collectors.toUnmodifiableList());
    }
}
