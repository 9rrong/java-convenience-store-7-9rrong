package store.model.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.dto.OrderDTO;
import store.dto.ReceiptProductDTO;
import store.model.dataloader.ProductDataLoader;
import store.model.dataloader.PromotionDataLoader;
import store.model.product.Products;
import store.model.promotion.Promotions;

public class OrderProcessorTest {

    private ProductDataLoader productDataLoader;
    private PromotionDataLoader promotionDataLoader;
    private Products products;
    private Promotions promotions;

    private OrderDTO orderDTO;

    @BeforeEach
    public void setUp() {
        productDataLoader = new ProductDataLoader("products.md");
        promotionDataLoader = new PromotionDataLoader("promotions.md");

        products = Products.withLoader(productDataLoader);
        promotions = Promotions.withLoaders(promotionDataLoader);
    }

    @Test
    public void 프로모션이_없을_때_프로모션이_적용되지_않는지_확인() {
        OrderDTO orderDTOWithoutPromotion = new OrderDTO("물", 5);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderDTOWithoutPromotion);

        assertThat(orderProcessor.promotionIsNotAvailable()).isTrue();
    }

    @Test
    public void 프로모션이_있을_때_프로모션이_적용되는지_확인() {
        orderDTO = new OrderDTO("콜라", 10);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderDTO);

        assertThat(orderProcessor.promotionIsNotAvailable()).isFalse();
    }

    @Test
    public void 프로모션_추가된_주문_수량_확인() {
        orderDTO = new OrderDTO("콜라", 10);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderDTO);

        assertThat(orderProcessor.getPromotionAddedOrder().quantity()).isEqualTo(10);
    }

    @Test
    public void 프로모션_주문_수량_확인() {
        orderDTO = new OrderDTO("콜라", 10);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderDTO);

        assertThat(orderProcessor.getPromotionOrder().quantity()).isEqualTo(9);
    }

    @Test
    public void 프로모션_적용되지_않은_상품_수량_확인() {
        orderDTO = new OrderDTO("콜라", 10);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderDTO);

        assertThat(orderProcessor.getNonPromotionProductQuantity()).isEqualTo(0);
    }

    @Test
    public void 무료_상품_자격_확인() {
        orderDTO = new OrderDTO("콜라", 10);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderDTO);

        assertThat(orderProcessor.isEligibleForFreeItems()).isFalse();
    }

    @Test
    public void 기본_주문_확인() {
        orderDTO = new OrderDTO("콜라", 10);
        ReceiptProductDTO receiptProductDTO = new ReceiptProductDTO("콜라",10,1000);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderDTO);

        assertThat(orderProcessor.getDefaultOrder()).isEqualTo(receiptProductDTO);
    }

    @Test
    public void 프로모션_상품_수량_충분한지_확인() {
        orderDTO = new OrderDTO("콜라", 10);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderDTO);

        assertThat(orderProcessor.promotionProductIsEnough()).isTrue();
    }

    @Test
    public void 다른_제품에_대한_주문_확인() {
        OrderDTO orderDTOForSoda = new OrderDTO("사이다", 7);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderDTOForSoda);

        assertThat(orderProcessor.getPromotionOrder().quantity()).isEqualTo(6);
        assertThat(orderProcessor.promotionIsNotAvailable()).isFalse();
    }

    @Test
    public void 부분_프로모션_적용_주문_확인() {
        OrderDTO orderDTOForSoda = new OrderDTO("탄산수", 5);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderDTOForSoda);

        assertThat(orderProcessor.getPromotionOrder().quantity()).isEqualTo(3);
    }
}
