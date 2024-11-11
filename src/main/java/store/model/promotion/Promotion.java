package store.model.promotion;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import store.dto.PromotionDTO;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;


    public Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
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

    public int getGet() {
        return this.get;
    }

    public int getBuy() {
        return this.buy;
    }

    public boolean isName(String name) {
        return this.name.equals(name);
    }

    public boolean isNowAvailable() {
        LocalDateTime now = DateTimes.now();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return !now.isBefore(startDateTime) && !now.isAfter(endDateTime);
    }

}
