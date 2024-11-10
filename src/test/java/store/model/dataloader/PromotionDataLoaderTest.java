package store.model.dataloader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import store.dto.PromotionDTO;

class PromotionDataLoaderTest {

    private PromotionDataLoader promotionDataLoader;

    @Test
    void 프로모션_파일_불러오기() {
        promotionDataLoader = new PromotionDataLoader("promotions.md");
        List<PromotionDTO> promotions = promotionDataLoader.loadFromFile();

        assertEquals(2, promotions.size());

        PromotionDTO firstPromotion = promotions.get(0);
        assertEquals("탄산2+1", firstPromotion.name());
        assertEquals(2, firstPromotion.buy());
        assertEquals(1, firstPromotion.get());
        assertEquals(LocalDate.of(2024, 1, 1), firstPromotion.startDate());
        assertEquals(LocalDate.of(2024, 12, 31), firstPromotion.endDate());

        PromotionDTO secondPromotion = promotions.get(1);
        assertEquals("MD추천상품", secondPromotion.name());
        assertEquals(1, secondPromotion.buy());
        assertEquals(1, secondPromotion.get());
        assertEquals(LocalDate.of(2024, 2, 1), secondPromotion.startDate());
        assertEquals(LocalDate.of(2024, 6, 30), secondPromotion.endDate());
    }

    @Test
    void 없는_프로모션_파일_예외테스트() {
        promotionDataLoader = new PromotionDataLoader("empty_promotions.md");

        assertThrows(RuntimeException.class, promotionDataLoader::loadFromFile);
    }

    @Test
    void 잘못된_프로모션_파일_예외테스트() {
        promotionDataLoader = new PromotionDataLoader("invalid_promotions.md");

        assertThrows(RuntimeException.class, promotionDataLoader::loadFromFile);
    }
}
