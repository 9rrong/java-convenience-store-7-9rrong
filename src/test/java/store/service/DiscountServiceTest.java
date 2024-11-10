package store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.dto.OrderDTO;
import store.model.product.Product;
import store.model.product.Products;
import store.model.promotion.Promotion;
import store.model.promotion.Promotions;

public class DiscountServiceTest {

    private DiscountService discountService;
    private Products products;
    private Promotions promotions;

    @BeforeEach
    public void setup() {
        products = new Products(List.of(
                new Product("탄산수", 1000, 10, "탄산1+1"),
                new Product("콜라", 1000, 10, "탄산2+1"),
                new Product("사이다", 1000, 5, "탄산3+1"),
                new Product("소다", 1000, 10, "탄산2+2"),
                new Product("물", 500, 10, null)
        ));

        promotions = new Promotions(List.of(
                new Promotion("탄산1+1", 1, 1, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-31")),
                new Promotion("탄산2+1", 2, 1, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-31")),
                new Promotion("탄산3+1", 3, 1, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-31")),
                new Promotion("탄산2+2", 2, 2, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-31"))
        ));

        discountService = new DiscountService(products, promotions);
    }

    @Test
    public void 유효한_프로모션이_있는_경우_프로모션_권유() {
        OrderDTO orderDTO = new OrderDTO("콜라", 5);
        assertTrue(discountService.isPromotionEligible(orderDTO), "유효한 프로모션이 있을 경우 권유해야 합니다.");
    }

    @Test
    public void 프로모션이_없는_제품은_권유하지_않음() {
        OrderDTO orderDTO = new OrderDTO("물", 3);
        assertFalse(discountService.isPromotionEligible(orderDTO), "프로모션이 없는 제품은 권유하지 않아야 합니다.");
    }

    @Test
    public void 만료된_프로모션은_권유하지_않음() {
        promotions = new Promotions(List.of(
                new Promotion("탄산2+1", 2, 1, LocalDate.parse("2022-01-01"), LocalDate.parse("2022-12-31"))
        ));
        discountService = new DiscountService(products, promotions);
        OrderDTO orderDTO = new OrderDTO("콜라", 2);

        assertFalse(discountService.isPromotionEligible(orderDTO), "만료된 프로모션은 권유하지 않아야 합니다.");
    }

    @Test
    public void 재고가_충분한_경우_프로모션_수량_충분() {
        OrderDTO orderDTO = new OrderDTO("사이다", 4);
        assertTrue(discountService.isPromotionProductsQuantityEnough(orderDTO), "재고가 충분할 경우 프로모션 수량은 충분해야 합니다.");
    }

    @Test
    public void 주문량이_재고보다_많으면_프로모션_수량_부족() {
        OrderDTO orderDTO = new OrderDTO("사이다", 6);
        assertFalse(discountService.isPromotionProductsQuantityEnough(orderDTO), "주문량이 재고보다 많으면 프로모션 수량이 부족해야 합니다.");
    }

    @ParameterizedTest
    @CsvSource({
            "탄산수, 3, true",
            "콜라, 5, true",
            "사이다, 4, false",
            "소다, 6, true"
    })
    public void 다양한_제품에_대한_프로모션_적용_여부_테스트(String productName, int quantity, boolean expected) {
        OrderDTO orderDTO = new OrderDTO(productName, quantity);
        assertEquals(expected, discountService.canAddPromotionProduct(orderDTO),
                "프로모션 적용 가능 여부가 예상 결과와 일치해야 합니다.");
    }

    @Test
    public void 추가_프로모션_적용_주문량_증가() {
        OrderDTO orderDTO = new OrderDTO("콜라", 4);
        OrderDTO updatedOrder = discountService.processAddPromotionProduct(orderDTO, true);
        assertEquals(5, updatedOrder.quantity(), "프로모션이 추가되면 주문량이 증가해야 합니다.");
    }

    @Test
    public void 추가_프로모션_미적용_주문량_유지() {
        OrderDTO orderDTO = new OrderDTO("콜라", 4);
        OrderDTO updatedOrder = discountService.processAddPromotionProduct(orderDTO, false);
        assertEquals(4, updatedOrder.quantity(), "프로모션이 추가되지 않으면 주문량이 유지되어야 합니다.");
    }

    @Test
    public void 최대_프로모션_적용_수량_계산() {
        OrderDTO orderDTO = new OrderDTO("소다", 8);
        int maxQuantity = discountService.getMaxPromotionQuantity(orderDTO);
        assertEquals(8, maxQuantity, "최대 프로모션 적용 수량이 계산된 값과 일치해야 합니다.");
    }

    @Test
    public void 프로모션만_구매할_경우_적절한_주문량_반환() {
        OrderDTO orderDTO = new OrderDTO("콜라", 6);
        OrderDTO updatedOrder = discountService.processBuyOnlyPromotionProduct(orderDTO, 4);
        assertEquals(2, updatedOrder.quantity(), "프로모션만 구매할 경우 적절한 주문량이 반환되어야 합니다.");
    }

    @Test
    public void 비프로모션_상품만_구매할_경우_적절한_주문량_반환() {
        OrderDTO orderDTO = new OrderDTO("콜라", 6);
        OrderDTO updatedOrder = discountService.processBuyNonPromotionProduct(orderDTO, 4);
        assertEquals(4, updatedOrder.quantity(), "비프로모션 상품만 구매할 경우 적절한 주문량이 반환되어야 합니다.");
    }
}
