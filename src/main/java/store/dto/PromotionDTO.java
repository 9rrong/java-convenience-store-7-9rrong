package store.dto;

import java.time.LocalDateTime;

public record PromotionDTO(
        String name,
        int buy,
        int get,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
