package store.model;

import java.util.ArrayList;
import java.util.List;
import store.dto.ReceiptProductDTO;
import store.view.OutputView;

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

        receipt.add(OutputView.RECEIPT_STORE_NAME);
        receipt.add(String.format(OutputView.RECEIPT_HEADER_FORMAT, "상품명", "수량", "금액"));

        for (ReceiptProductDTO receiptProductDTO : totalOrders) {
            int productPrice = receiptProductDTO.price();
            int totalOrderAmount = receiptProductDTO.quantity() * productPrice;
            totalAmount += totalOrderAmount;
            totalQuantity += receiptProductDTO.quantity();

            receipt.add(String.format(OutputView.RECEIPT_PRODUCT_FORMAT, receiptProductDTO.name(), receiptProductDTO.quantity(), totalOrderAmount));
        }

        receipt.add(OutputView.RECEIPT_PROMOTION_HEADER);

        for (ReceiptProductDTO promotionOrder : promotionOrders) {
            int promotionAmount = promotionOrder.quantity() * promotionOrder.price();
            promotionDiscount += promotionAmount;
            receipt.add(String.format(OutputView.RECEIPT_PRODUCT_FORMAT, promotionOrder.name(), promotionOrder.quantity(), 0)); // No amount for promotion
        }

        if (applyMembershipDiscount) {
            membershipDiscount = (int) ((totalAmount - promotionDiscount) * 0.3);
        }

        receipt.add(OutputView.RECEIPT_PARTITION_LINE);
        receipt.add(String.format(OutputView.RECEIPT_PRODUCT_FORMAT, OutputView.RECEIPT_TOTAL_LABEL, totalQuantity, totalAmount));
        receipt.add(String.format(OutputView.RECEIPT_FOOTER_FORMAT, OutputView.RECEIPT_DISCOUNT_LABEL, promotionDiscount));
        receipt.add(String.format(OutputView.RECEIPT_FOOTER_FORMAT, OutputView.RECEIPT_MEMBERSHIP_DISCOUNT_LABEL, membershipDiscount));

        int finalAmount = totalAmount - promotionDiscount - membershipDiscount;
        receipt.add(String.format(OutputView.RECEIPT_FOOTER_FORMAT, OutputView.RECEIPT_FINAL_AMOUNT_LABEL, finalAmount));

        return receipt;
    }
}
