package store.model.product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.dto.ProductDTO;
import store.model.dataloader.AbstractDataLoader;

public class Products {
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

    private static List<Product> fromDTOs(List<ProductDTO> productDTOs) {
        Map<String, List<ProductDTO>> productDTOsByName = productDTOs.stream()
                .collect(Collectors.groupingBy(ProductDTO::name));

        productDTOsByName.values().forEach(products -> {
            boolean hasNonPromotionProduct = products.stream()
                    .anyMatch(productDTO -> productDTO.promotion() == null);

            if (!hasNonPromotionProduct) {
                ProductDTO firstProductDTO = products.getFirst();
                ProductDTO nonPromotionProductDTO = new ProductDTO(
                        firstProductDTO.name(),
                        firstProductDTO.price(),
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
