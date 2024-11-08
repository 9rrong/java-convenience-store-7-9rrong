package store.model.promotion;

import java.util.List;
import java.util.stream.Collectors;
import store.dto.PromotionDTO;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public static List<Promotion> fromDTOs(List<PromotionDTO> promotionDTOs) {
        return promotionDTOs
                .stream()
                .map(Promotion::fromDTO)
                .collect(Collectors.toUnmodifiableList());
    }
}
