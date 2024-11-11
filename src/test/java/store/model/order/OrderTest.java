package store.model.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import store.dto.OrderDTO;
import store.dto.ProductDTO;
import store.model.ErrorCode;

class OrderTest {

    @Test
    void 정상_주문_생성_테스트() {
        OrderDTO orderDTO = new OrderDTO("콜라", 5);
        List<ProductDTO> availableProducts = List.of(
                new ProductDTO("콜라", 1500, 10, "2+1"),
                new ProductDTO("사이다", 1300, 15, "1+1")
        );

        Order order = Order.fromDTO(orderDTO, availableProducts);

        assertNotNull(order);
    }

    @Test
    void 없는_상품_주문_예외_테스트() {
        OrderDTO orderDTO = new OrderDTO("없는상품", 5);
        List<ProductDTO> availableProducts = List.of(
                new ProductDTO("콜라", 1500, 10, "2+1"),
                new ProductDTO("사이다", 1300, 15, "1+1")
        );

        Executable executable = () -> Order.fromDTO(orderDTO, availableProducts);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 재고_수량_초과_주문_예외_테스트() {
        OrderDTO orderDTO = new OrderDTO("콜라", 20);
        List<ProductDTO> availableProducts = List.of(
                new ProductDTO("콜라", 1500, 10, "2+1"),
                new ProductDTO("사이다", 1300, 15, "1+1")
        );

        Executable executable = () -> Order.fromDTO(orderDTO, availableProducts);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals(ErrorCode.QUANTITY_EXCEEDS_STOCK.getMessage(), exception.getMessage());
    }
}
