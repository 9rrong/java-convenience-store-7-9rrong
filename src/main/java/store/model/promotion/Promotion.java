package store.model.promotion;

import java.time.LocalDateTime;
import store.dto.PromotionDTO;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;


    private Promotion(String name, int buy, int get, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Promotion fromDTO(PromotionDTO promotionDTO) {
        String name = promotionDTO.name();
        int buy = promotionDTO.buy();
        int get = promotionDTO.get();
        LocalDateTime startDate = promotionDTO.startDate();
        LocalDateTime endDate = promotionDTO.endDate();

        return new Promotion(name, buy, get, startDate, endDate);
    }
}
