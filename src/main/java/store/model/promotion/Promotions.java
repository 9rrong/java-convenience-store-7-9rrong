package store.model.promotion;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import store.dto.PromotionDTO;
import store.model.dataloader.AbstractDataLoader;

public class Promotions {
    private final List<Promotion> promotions;
    private final AbstractDataLoader<PromotionDTO> promotionDataLoader;

    public Promotions(AbstractDataLoader<PromotionDTO> promotionDataLoader) {
        this.promotionDataLoader = promotionDataLoader;
        this.promotions = loadInitialProducts();
    }

    private List<Promotion> loadInitialProducts() {
        return fromDTOs(promotionDataLoader.loadFromFile());
    }

    private List<Promotion> fromDTOs(List<PromotionDTO> promotionDTOs) {
        return promotionDTOs
                .stream()
                .map(Promotion::fromDTO)
                .collect(Collectors.toUnmodifiableList());
    }

    public Optional<Promotion> findPromotionByName(String name) {
        return promotions.stream()
                .filter(promotion -> promotion.isName(name) && promotion.isNowAvailable())
                .findFirst();
    }
}
