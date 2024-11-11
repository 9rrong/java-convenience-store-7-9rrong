package store.model.dataloader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import store.dto.ProductDTO;

class ProductDataLoaderTest {

    private ProductDataLoader productDataLoader;

    @Test
    void 상품_파일_불러오기() {
        productDataLoader = new ProductDataLoader("products.md");
        List<ProductDTO> products = productDataLoader.loadFromFile();

        assertEquals(16, products.size());
        assertProduct(products.get(0), "콜라", 1000, 10, "탄산2+1");
        assertProduct(products.get(1), "콜라", 1000, 10, null);
        assertProduct(products.get(2), "사이다", 1000, 8, "탄산2+1");
    }

    private void assertProduct(ProductDTO product, String expectedName, int expectedPrice, int expectedQuantity,
                               String expectedPromotion) {
        assertEquals(expectedName, product.name());
        assertEquals(expectedPrice, product.price());
        assertEquals(expectedQuantity, product.quantity());
        assertEquals(expectedPromotion, product.promotion());
    }

    @Test
    void 잘못된_상품_파일_예외테스트() {
        productDataLoader = new ProductDataLoader("invalid_products.md");

        assertThrows(RuntimeException.class, productDataLoader::loadFromFile);
    }
}
