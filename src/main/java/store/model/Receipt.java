package store.model;

import java.util.ArrayList;
import java.util.List;
import store.dto.ReceiptProductDTO;

public class Receipt {
    private final List<ReceiptProductDTO> totalOrders;
    private final List<ReceiptProductDTO> promotionOrders;
    boolean applyMembershipDiscount;

    public Receipt(List<ReceiptProductDTO> totalOrders, List<ReceiptProductDTO> promotionOrders,
                   boolean applyMembershipDiscount) {
        this.totalOrders = totalOrders;
        this.promotionOrders = promotionOrders;
        this.applyMembershipDiscount = applyMembershipDiscount;
    }

    public List<String> generateReceipt() {
        List<String> receipt = new ArrayList<>();
        int totalAmount = 0;
        int totalQuantity = 0;
        int promotionDiscount = 0;
        int membershipDiscount  = 0;

        receipt.add("==============W 편의점================");
        receipt.add("상품명\t\t수량\t금액");

        for (ReceiptProductDTO receiptProductDTO : totalOrders) {
            int productPrice = receiptProductDTO.price();
            int totalOrderAmount = receiptProductDTO.quantity() * productPrice;
            totalAmount += totalOrderAmount;
            totalQuantity += receiptProductDTO.quantity();
            receipt.add(receiptProductDTO.name() + "\t\t" + receiptProductDTO.quantity() + "\t" + String.format("%,d",
                    totalOrderAmount));
        }

        receipt.add("=============증	정===============");
        for (ReceiptProductDTO promotionOrder : promotionOrders) {
            receipt.add(promotionOrder.name() + "\t\t" + promotionOrder.quantity());
            int promotionAmount = (promotionOrder.quantity() * promotionOrder.price());
            promotionDiscount += promotionAmount;
        }

        if (applyMembershipDiscount) {
            membershipDiscount = (int) ((totalAmount - promotionDiscount) * 0.3);
        }

        receipt.add("====================================");
        receipt.add("총구매액\t\t" + totalQuantity + "\t" + String.format("%,d", totalAmount));
        receipt.add("행사할인\t\t" + String.format("%,d", promotionDiscount));
        receipt.add("멤버십할인\t\t" + String.format("%,d", membershipDiscount));

        int finalAmount = totalAmount - promotionDiscount - membershipDiscount;
        receipt.add("내실돈\t\t" + String.format("%,d", finalAmount));

        return receipt;
    }
}
