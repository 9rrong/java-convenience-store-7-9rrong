package store.dto;

import java.util.List;

public record ReceiptDTO(
        List<OrderDTO> nonPromotionOrders,
        List<OrderDTO> promotionOrders,
        boolean applyMembershipDiscount
) {
}
