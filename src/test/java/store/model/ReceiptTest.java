package store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import store.dto.ReceiptProductDTO;
import store.view.OutputView;

class ReceiptTest {

    public static final int MAX_MEMBERSHIP_DISCOUNT_AMOUNT = 8000;

    @Test
    void 멤버십_할인_최댓값_적용() {
        List<ReceiptProductDTO> totalOrders = List.of(
                new ReceiptProductDTO("콜라", 30, 1000)
        );
        List<ReceiptProductDTO> promotionOrders = new ArrayList<>();

        Receipt receipt = new Receipt(totalOrders, promotionOrders, true);
        List<String> generatedReceipt = receipt.generateReceipt();

        String membershipDiscountLine = generatedReceipt.stream()
                .filter(line -> line.contains(OutputView.RECEIPT_MEMBERSHIP_DISCOUNT_LABEL))
                .findAny().get();

        assertEquals(
                String.format(OutputView.RECEIPT_FOOTER_FORMAT, OutputView.RECEIPT_MEMBERSHIP_DISCOUNT_LABEL,
                        MAX_MEMBERSHIP_DISCOUNT_AMOUNT), membershipDiscountLine);
    }

    @Test
    void 멤버십_할인_최댓값_이하_계산() {
        int expectedValue = 6000;

        List<ReceiptProductDTO> totalOrders = List.of(
                new ReceiptProductDTO("콜라", 20, 1000)
        );
        List<ReceiptProductDTO> promotionOrders = List.of(
        );

        Receipt receipt = new Receipt(totalOrders, promotionOrders, true);
        List<String> generatedReceipt = receipt.generateReceipt();

        String membershipDiscountLine = generatedReceipt.stream()
                .filter(line -> line.contains(OutputView.RECEIPT_MEMBERSHIP_DISCOUNT_LABEL))
                .findAny().get();

        assertEquals(
                String.format(OutputView.RECEIPT_FOOTER_FORMAT, OutputView.RECEIPT_MEMBERSHIP_DISCOUNT_LABEL,
                        expectedValue), membershipDiscountLine);
    }
}
