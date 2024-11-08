package store.model.promotion;

import camp.nextstep.edu.missionutils.DateTimes;
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

        return new Promotion(
                promotionDTO.name(),
                promotionDTO.buy(),
                promotionDTO.get(),
                promotionDTO.startDate(),
                promotionDTO.endDate()
        );
    }

    public boolean isName(String name) {
        return this.name.equals(name);
    }

    public boolean isNowAvailable() {
        LocalDateTime now = DateTimes.now();
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }
}
