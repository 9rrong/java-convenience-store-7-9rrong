package store.model.dataloader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

        assertEquals(3, products.size());

        ProductDTO firstProduct = products.get(0);
        assertEquals("콜라", firstProduct.name());
        assertEquals(1000, firstProduct.price());
        assertEquals(10, firstProduct.quantity());
        assertEquals("탄산2+1", firstProduct.promotion());

        ProductDTO secondProduct = products.get(1);
        assertEquals("사이다", secondProduct.name());
        assertEquals(1000, secondProduct.price());
        assertEquals(8, secondProduct.quantity());
        assertNull(secondProduct.promotion());

        ProductDTO thirdProduct = products.get(2);
        assertEquals("오렌지주스", thirdProduct.name());
        assertEquals(1800, thirdProduct.price());
        assertEquals(9, thirdProduct.quantity());
        assertEquals("MD추천상품", thirdProduct.promotion());
    }

    @Test
    void 없는_상품_파일_예외테스트() {
        productDataLoader = new ProductDataLoader("empty_products.md");

        assertThrows(RuntimeException.class, productDataLoader::loadFromFile);
    }

    @Test
    void 잘못된_상품_파일_예외테스트() {
        productDataLoader = new ProductDataLoader("invalid_products.md");

        assertThrows(RuntimeException.class, productDataLoader::loadFromFile);
    }
}
