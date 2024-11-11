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
        int membershipDiscount = 0;

        receipt.add("==============W 편의점================");
        receipt.add(String.format("%-20s%-10s%-15s", "상품명", "수량", "금액"));

        for (ReceiptProductDTO receiptProductDTO : totalOrders) {
            int productPrice = receiptProductDTO.price();
            int totalOrderAmount = receiptProductDTO.quantity() * productPrice;
            totalAmount += totalOrderAmount;
            totalQuantity += receiptProductDTO.quantity();

            receipt.add(String.format("%-20s%,-10d%,-15d", receiptProductDTO.name(), receiptProductDTO.quantity(), totalOrderAmount));
        }

        receipt.add("=============증\t    정===============");

        for (ReceiptProductDTO promotionOrder : promotionOrders) {
            int promotionAmount = promotionOrder.quantity() * promotionOrder.price();
            promotionDiscount += promotionAmount;
            receipt.add(String.format("%-20s%,-10d", promotionOrder.name(), promotionOrder.quantity()));
        }

        if (applyMembershipDiscount) {
            membershipDiscount = (int) ((totalAmount - promotionDiscount) * 0.3);
        }

        receipt.add("====================================");
        receipt.add(String.format("%-20s%,-10d%,-15d", "총구매액", totalQuantity, totalAmount));
        receipt.add(String.format("%-30s%,-10d","행사할인", promotionDiscount));
        receipt.add(String.format("%-30s%,-10d","멤버십할인", membershipDiscount));

        int finalAmount = totalAmount - promotionDiscount - membershipDiscount;
        receipt.add(String.format("%-30s%,-10d", "내실돈", finalAmount));

        return receipt;
    }
}
