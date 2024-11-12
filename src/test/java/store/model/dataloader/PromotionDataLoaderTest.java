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

        assertEquals(3, promotions.size());

        verifyPromotion(promotions.get(0), "탄산2+1", 2, 1,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        verifyPromotion(promotions.get(1), "MD추천상품", 1, 1,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );
    }

    private void verifyPromotion(PromotionDTO promotion, String expectedName, int expectedBuy, int expectedGet,
                                 LocalDate expectedStartDate, LocalDate expectedEndDate) {
        assertEquals(expectedName, promotion.name());
        assertEquals(expectedBuy, promotion.buy());
        assertEquals(expectedGet, promotion.get());
        assertEquals(expectedStartDate, promotion.startDate());
        assertEquals(expectedEndDate, promotion.endDate());
    }

    @Test
    void 잘못된_프로모션_파일_예외테스트() {
        promotionDataLoader = new PromotionDataLoader("invalid_promotions.md");

        assertThrows(RuntimeException.class, promotionDataLoader::loadFromFile);
    }
}
