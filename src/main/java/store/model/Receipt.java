package store.model;

import java.util.ArrayList;
import java.util.List;
import store.dto.ReceiptProductDTO;
import store.view.OutputView;

public class Receipt {

    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final String RECEIPT_PRODUCT_NAME = "상품명";
    private static final String RECEIPT_QUANTITY = "수량";
    private static final String RECEIPT_PRICE = "금액";
    private static final int TOTAL_AMOUNT_INDEX = 0;
    private static final int TOTAL_QUANTITY_INDEX = 1;
    private static final int MAX_MEMBERSHIP_DISCOUNT_AMOUNT = 8000;

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

        addReceiptHeader(receipt);

        int[] totals = addProductsToReceipt(receipt, totalAmount, totalQuantity);
        totalAmount = totals[TOTAL_AMOUNT_INDEX];
        totalQuantity = totals[TOTAL_QUANTITY_INDEX];

        promotionDiscount = addPromotionsToReceipt(receipt, promotionDiscount);

        if (applyMembershipDiscount) {
            membershipDiscount = calculateMembershipDiscount(totalAmount, promotionDiscount);
        }

        addReceiptFooter(receipt, totalAmount, totalQuantity, promotionDiscount, membershipDiscount);

        return receipt;
    }

    private int[] addProductsToReceipt(List<String> receipt, int totalAmount, int totalQuantity) {
        for (ReceiptProductDTO receiptProductDTO : totalOrders) {
            int[] totals = addProductToReceipt(receipt, totalAmount, totalQuantity, receiptProductDTO);
            totalAmount = totals[TOTAL_AMOUNT_INDEX];
            totalQuantity = totals[TOTAL_QUANTITY_INDEX];
        }
        return new int[]{totalAmount, totalQuantity};
    }

    private int addPromotionsToReceipt(List<String> receipt, int promotionDiscount) {
        receipt.add(OutputView.RECEIPT_PROMOTION_HEADER);
        for (ReceiptProductDTO promotionOrder : promotionOrders) {
            promotionDiscount = addPromotionToReceipt(receipt, promotionDiscount, promotionOrder);
        }
        return promotionDiscount;
    }


    private void addReceiptHeader(List<String> receipt) {
        receipt.add(OutputView.RECEIPT_STORE_NAME);
        receipt.add(String.format(OutputView.RECEIPT_HEADER_FORMAT, RECEIPT_PRODUCT_NAME, RECEIPT_QUANTITY,
                RECEIPT_PRICE));
    }

    private int[] addProductToReceipt(List<String> receipt, int totalAmount, int totalQuantity,
                                      ReceiptProductDTO receiptProductDTO) {
        int productPrice = receiptProductDTO.price();
        int totalOrderAmount = receiptProductDTO.quantity() * productPrice;
        totalAmount += totalOrderAmount;
        totalQuantity += receiptProductDTO.quantity();

        receipt.add(String.format(OutputView.RECEIPT_PRODUCT_FORMAT, receiptProductDTO.name(),
                receiptProductDTO.quantity(), totalOrderAmount));

        return new int[]{totalAmount, totalQuantity};
    }

    private int addPromotionToReceipt(List<String> receipt, int promotionDiscount, ReceiptProductDTO promotionOrder) {
        int promotionAmount = promotionOrder.quantity() * promotionOrder.price();
        promotionDiscount += promotionAmount;
        receipt.add(String.format(OutputView.RECEIPT_PROMOTION_PRODUCT_FORMAT, promotionOrder.name(),
                promotionOrder.quantity()));

        return promotionDiscount;
    }

    private int calculateMembershipDiscount(int totalAmount, int promotionDiscount) {
        int discount = (int) ((totalAmount - promotionDiscount) * MEMBERSHIP_DISCOUNT_RATE);
        return Math.min(discount, MAX_MEMBERSHIP_DISCOUNT_AMOUNT);
    }

    private void addReceiptFooter(List<String> receipt, int totalAmount, int totalQuantity, int promotionDiscount,
                                  int membershipDiscount) {
        receipt.add(OutputView.RECEIPT_PARTITION_LINE);
        receipt.add(String.format(OutputView.RECEIPT_PRODUCT_FORMAT, OutputView.RECEIPT_TOTAL_LABEL, totalQuantity,
                totalAmount));
        receipt.add(
                String.format(OutputView.RECEIPT_FOOTER_FORMAT, OutputView.RECEIPT_DISCOUNT_LABEL, promotionDiscount));
        receipt.add(String.format(OutputView.RECEIPT_FOOTER_FORMAT, OutputView.RECEIPT_MEMBERSHIP_DISCOUNT_LABEL,
                membershipDiscount));

        int finalAmount = totalAmount - promotionDiscount - membershipDiscount;
        receipt.add(
                String.format(OutputView.RECEIPT_FOOTER_FORMAT, OutputView.RECEIPT_FINAL_AMOUNT_LABEL, finalAmount));
    }

}
