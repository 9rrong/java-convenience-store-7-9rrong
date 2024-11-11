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

    private Products products;
    private Promotions promotions;

    @BeforeEach
    public void setUp() {
        ProductDataLoader productDataLoader = new ProductDataLoader("products.md");
        PromotionDataLoader promotionDataLoader = new PromotionDataLoader("promotions.md");

        products = Products.withLoader(productDataLoader);
        promotions = Promotions.withLoaders(promotionDataLoader);
    }

    @Test
    public void 프로모션_없는_상품_주문_테스트() {
        OrderDTO orderWithoutPromotion = new OrderDTO("물", 5);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderWithoutPromotion);

        assertThat(orderProcessor.promotionIsNotAvailable()).isTrue();
    }

    @Test
    public void 프로모션_있는_상품_주문_테스트() {
        OrderDTO orderWithPromotion = new OrderDTO("콜라", 10);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderWithPromotion);

        assertThat(orderProcessor.promotionIsNotAvailable()).isFalse();
    }

    @Test
    public void 프로모션_추가된_상품_주문_수량_테스트() {
        OrderDTO orderWithPromotion = new OrderDTO("콜라", 10);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderWithPromotion);

        assertThat(orderProcessor.getPromotionAddedOrder().quantity()).isEqualTo(4);
    }

    @Test
    public void 프로모션_적용된_주문_수량_테스트() {
        OrderDTO orderWithPromotion = new OrderDTO("콜라", 10);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderWithPromotion);

        assertThat(orderProcessor.getPromotionOrder().quantity()).isEqualTo(9);
    }

    @Test
    public void 프로모션_미적용_상품_수량_테스트() {
        OrderDTO orderWithPromotion = new OrderDTO("콜라", 10);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderWithPromotion);

        assertThat(orderProcessor.getNonPromotionProductQuantity()).isEqualTo(1);
    }

    @Test
    public void 무료_상품_자격_조건_테스트() {
        OrderDTO orderWithPromotion = new OrderDTO("콜라", 10);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderWithPromotion);

        assertThat(orderProcessor.isEligibleForFreeItems()).isFalse();
    }

    @Test
    public void 기본_주문_생성_테스트() {
        OrderDTO orderWithPromotion = new OrderDTO("콜라", 10);
        ReceiptProductDTO expectedReceipt = new ReceiptProductDTO("콜라", 10, 1000);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderWithPromotion);

        assertThat(orderProcessor.getDefaultOrder()).isEqualTo(expectedReceipt);
    }

    @Test
    public void 프로모션_적용_상품_재고_충분성_테스트() {
        OrderDTO orderWithPromotion = new OrderDTO("콜라", 10);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderWithPromotion);

        assertThat(orderProcessor.promotionProductIsEnough()).isFalse();
    }

    @Test
    public void 다른_제품_주문_테스트() {
        OrderDTO orderForSoda = new OrderDTO("사이다", 7);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderForSoda);

        assertThat(orderProcessor.getPromotionOrder().quantity()).isEqualTo(6);
        assertThat(orderProcessor.promotionIsNotAvailable()).isFalse();
    }

    @Test
    public void 부분_프로모션_적용된_주문_수량_테스트() {
        OrderDTO orderForSparklingWater = new OrderDTO("탄산수", 5);
        OrderProcessor orderProcessor = new OrderProcessor(products, promotions, orderForSparklingWater);

        assertThat(orderProcessor.getPromotionOrder().quantity()).isEqualTo(3);
    }
}
