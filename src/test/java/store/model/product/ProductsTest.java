package store.model.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.dto.ProductDTO;
import store.model.dataloader.AbstractDataLoader;
import store.model.dataloader.ProductDataLoader;

public class ProductsTest {
    private Products products;

    @BeforeEach
    public void setUp() {
        AbstractDataLoader<ProductDTO> productDataLoader = new ProductDataLoader("products.md");
        products = Products.withLoader(productDataLoader);
    }

    @Test
    public void 프로모션과_비프로모션이_있는_제품_조회() {
        Map<Boolean, Product> productMap = products.findProductsByName("콜라");

        assertThat(productMap.get(true)).isNotNull();
        assertThat(productMap.get(false)).isNotNull();

        Product promotionProduct = productMap.get(true);
        assertThat(promotionProduct.hasName("콜라")).isTrue();
        assertThat(promotionProduct.hasPromotion()).isTrue();

        Product nonPromotionProduct = productMap.get(false);
        assertThat(nonPromotionProduct.hasName("콜라")).isTrue();
        assertThat(nonPromotionProduct.hasPromotion()).isFalse();
    }

    @Test
    public void 비프로모션만_있는_제품_조회() {
        Map<Boolean, Product> productMap = products.findProductsByName("물");

        assertThat(productMap.get(true)).isNull();
        assertThat(productMap.get(false)).isNotNull();

        Product nonPromotionProduct = productMap.get(false);
        assertThat(nonPromotionProduct.hasName("물")).isTrue();
        assertThat(nonPromotionProduct.hasPromotion()).isFalse();
    }

    @Test
    public void 프로모션_수량이_충분한_경우_수량_감소() {
        products.decreaseProductQuantityByName("콜라", 5);

        assertThat(products.findProductsByName("콜라").get(true).getQuantity()).isEqualTo(5);
        assertThat(products.findProductsByName("콜라").get(false).getQuantity()).isEqualTo(10);
    }

    @Test
    public void 프로모션_수량이_부족한_경우_비프로모션에서_수량_차감() {
        products.decreaseProductQuantityByName("콜라", 15);

        assertThat(products.findProductsByName("콜라").get(true).getQuantity()).isEqualTo(0);
        assertThat(products.findProductsByName("콜라").get(false).getQuantity()).isEqualTo(5);
    }

    @Test
    public void 비프로모션_수량만_있는_경우_수량_감소() {
        products.decreaseProductQuantityByName("물", 5);

        assertThat(products.findProductsByName("물").get(false).getQuantity()).isEqualTo(5);
    }
}
