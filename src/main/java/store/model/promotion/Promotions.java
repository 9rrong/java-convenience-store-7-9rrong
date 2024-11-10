package store.model.promotion;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import store.dto.PromotionDTO;
import store.model.ErrorCode;
import store.model.dataloader.AbstractDataLoader;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public static Promotions withLoaders(AbstractDataLoader<PromotionDTO> promotionDataLoader) {
        List<Promotion> promotions = Promotions.fromDTOs(promotionDataLoader.loadFromFile());
        return new Promotions(promotions);
    }

    private static List<Promotion> fromDTOs(List<PromotionDTO> promotionDTOs) {
        return promotionDTOs
                .stream()
                .map(Promotion::fromDTO)
                .collect(Collectors.toUnmodifiableList());
    }

    public Promotion findPromotionByName(String name) {
        return promotions.stream()
                .filter(promotion -> promotion.isName(name))
                .findFirst()
                .orElseThrow(()-> new NoSuchElementException(ErrorCode.PROMOTION_NOT_FOUND.getMessage()));
    }
}
