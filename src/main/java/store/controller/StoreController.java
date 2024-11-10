package store.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import store.dto.OrderDTO;
import store.dto.ProductDTO;
import store.dto.ReceiptDTO;
import store.model.InputConverter;
import store.model.order.Orders;
import store.model.product.Products;
import store.model.promotion.Promotions;
import store.service.DiscountService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final InputConverter inputConverter;
    private final Products products;
    private final Promotions promotions;
    private final DiscountService discountService;

    public StoreController(InputView inputView,
                           OutputView outputView,
                           InputConverter inputConverter,
                           Products products,
                           Promotions promotions,
                           DiscountService discountService
    ) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.inputConverter = inputConverter;
        this.products = products;
        this.promotions = promotions;
        this.discountService = discountService;
    }

    public void operate() {
        outputView.printGreeting();
        List<ProductDTO> productDTOs = products.toDTOs();
        outputView.printProducts(productDTOs);

        Orders orders = retryUntilValid(() -> createOrders(productDTOs));
        ReceiptDTO receiptDTO = processOrders(orders);
    }

    private Orders createOrders(List<ProductDTO> productDTOs) {
        String input = inputView.readItem();
        List<OrderDTO> orderDTOs = inputConverter.convertToOrderDTOs(input);
        return Orders.fromDTOs(orderDTOs, productDTOs);
    }

    private ReceiptDTO processOrders(Orders orders) {
        List<OrderDTO> nonPromotionOrders = new ArrayList<>();
        List<OrderDTO> promotionOrders = new ArrayList<>();

        for (OrderDTO orderDTO : orders.getOrderDTOs()) {
            if (!discountService.isPromotionEligible(orderDTO)) {
                nonPromotionOrders.add(orderDTO);
                continue;
            }

            if (discountService.isPromotionProductsQuantityEnough(orderDTO)) {
                processPromotionOrder(orderDTO, promotionOrders);
            } else {
                processNonPromotionOrder(orderDTO, nonPromotionOrders, promotionOrders);
            }
        }

        boolean applyMembershipDiscount = askMembershipDiscount();

        return new ReceiptDTO(nonPromotionOrders, promotionOrders, applyMembershipDiscount);
    }

    private void processPromotionOrder(OrderDTO orderDTO, List<OrderDTO> promotionOrders) {
        if (discountService.canAddPromotionProduct(orderDTO)) {
            boolean addProduct = askAddPromotionProduct(orderDTO.productName());
            promotionOrders.add(discountService.processAddPromotionProduct(orderDTO, addProduct));
        }
    }

    private void processNonPromotionOrder(OrderDTO orderDTO,
                                          List<OrderDTO> nonPromotionOrders,
                                          List<OrderDTO> promotionOrders) {
        int nonPromotionQuantity = discountService.getMaxPromotionQuantity(orderDTO);
        boolean buyOnlyPromotionProduct = askBuyOnlyPromotionProduct(orderDTO.productName(), nonPromotionQuantity);

        promotionOrders.add(discountService.processBuyOnlyPromotionProduct(orderDTO, nonPromotionQuantity));

        if (!buyOnlyPromotionProduct) {
            nonPromotionOrders.add(discountService.processBuyNonPromotionProduct(orderDTO, nonPromotionQuantity));
        }
    }


    private boolean askAddPromotionProduct(String productName) {
        return inputConverter.convertToBoolean(
                inputView.promptAddPromotionProduct(productName));
    }

    private boolean askBuyOnlyPromotionProduct(String productName, int nonPromotionQuantity) {
        return inputConverter.convertToBoolean(
                inputView.promptBuyOnlyPromotionProduct(productName, nonPromotionQuantity));
    }

    private boolean askMembershipDiscount() {
        return inputConverter.convertToBoolean(inputView.promptMembershipDiscount());
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
