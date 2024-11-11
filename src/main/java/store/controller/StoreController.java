package store.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import store.dto.OrderDTO;
import store.dto.ProductDTO;
import store.dto.ReceiptProductDTO;
import store.model.InputConverter;
import store.model.Receipt;
import store.model.order.OrderProcessor;
import store.model.order.Orders;
import store.model.product.Products;
import store.model.promotion.Promotions;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final InputConverter inputConverter;
    private final Products products;
    private final Promotions promotions;

    public StoreController(InputView inputView,
                           OutputView outputView,
                           InputConverter inputConverter,
                           Products products,
                           Promotions promotions
    ) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.inputConverter = inputConverter;
        this.products = products;
        this.promotions = promotions;
    }

    public void operate() {
        outputView.printGreeting();
        List<ProductDTO> productDTOs = products.toDTOs();
        outputView.printProducts(productDTOs);

        Orders orders = retryUntilValid(() -> createOrders(productDTOs));
        Receipt receipt = processOrders(orders);

        outputView.printReceipt(receipt.generateReceipt());
    }

    private Orders createOrders(List<ProductDTO> productDTOs) {
        String input = inputView.readItem();
        List<OrderDTO> orderDTOs = inputConverter.convertToOrderDTOs(input);
        return Orders.fromDTOs(orderDTOs, productDTOs);
    }

    private Receipt processOrders(Orders orders) {
        List<ReceiptProductDTO> totalOrders = new ArrayList<>();
        List<ReceiptProductDTO> promotionOrders = new ArrayList<>();

        for (OrderDTO orderDTO : orders.getOrderDTOs()) {
            OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderDTO);

            if (orderProcessor.promotionIsNotAvailable()) {
                totalOrders.add(orderProcessor.getDefaultOrder());
            }

            if (!orderProcessor.promotionIsNotAvailable()) {
                processOrderWithPromotion(orderProcessor, promotionOrders, totalOrders);
            }
        }

        boolean applyMembershipDiscount = askMembershipDiscount();
        return new Receipt(totalOrders, promotionOrders, applyMembershipDiscount);
    }

    private void processOrderWithPromotion(OrderProcessor orderProcessor, List<ReceiptProductDTO> promotionOrders,
                                           List<ReceiptProductDTO> totalOrders) {
        if (orderProcessor.promotionProductIsEnough()) {
            handleEligiblePromotion(orderProcessor, promotionOrders, totalOrders);
            return;
        }
        handleNonEligiblePromotion(orderProcessor, promotionOrders, totalOrders);
    }

    private void handleEligiblePromotion(OrderProcessor orderProcessor, List<ReceiptProductDTO> promotionOrders,
                                         List<ReceiptProductDTO> totalOrders) {
        if (orderProcessor.isEligibleForFreeItems()) {
            handleFreeItemsPromotion(orderProcessor, promotionOrders, totalOrders);
        }
    }

    private void handleFreeItemsPromotion(OrderProcessor orderProcessor, List<ReceiptProductDTO> promotionOrders,
                                          List<ReceiptProductDTO> totalOrders) {
        totalOrders.add(orderProcessor.getDefaultOrder());

        if (askAddPromotionProduct(orderProcessor.getProductName())) {
            promotionOrders.add(orderProcessor.getPromotionAddedOrder());
            return;
        }
        promotionOrders.add(orderProcessor.getPromotionOrder());
    }

    private void handleNonEligiblePromotion(OrderProcessor orderProcessor, List<ReceiptProductDTO> promotionOrders,
                                            List<ReceiptProductDTO> totalOrders) {
        int nonPromotionQuantity = orderProcessor.getNonPromotionProductQuantity();
        boolean buyOnlyPromotionProduct = askBuyOnlyPromotionProduct(orderProcessor.getProductName(),
                nonPromotionQuantity);

        promotionOrders.add(orderProcessor.getPromotionOrder());

        if (buyOnlyPromotionProduct) {
            totalOrders.add(orderProcessor.getPromotionOrder());
            return;
        }

        totalOrders.add(orderProcessor.getDefaultOrder());
    }

    private boolean askAddPromotionProduct(String productName) {
        return retryUntilValid(() -> inputConverter.convertToBoolean(
                inputView.promptAddPromotionProduct(productName)));
    }

    private boolean askBuyOnlyPromotionProduct(String productName, int nonPromotionQuantity) {
        return retryUntilValid(() -> inputConverter.convertToBoolean(
                inputView.promptBuyOnlyPromotionProduct(productName, nonPromotionQuantity)));
    }

    private boolean askMembershipDiscount() {
        return retryUntilValid(() -> inputConverter.convertToBoolean(inputView.promptMembershipDiscount()));
    }

    private <T> T retryUntilValid(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }
}
