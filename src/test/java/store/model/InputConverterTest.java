package store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.dto.PurchaseDTO;

class InputConverterTest {

    private InputConverter inputConverter;

    @BeforeEach
    void 설정() {
        inputConverter = new InputConverter();
    }

    @ParameterizedTest
    @CsvSource({
            "[콜라-abc],",
            "[콜라-]",
            "[콜라10],[사이다-3]",
            "콜라-10,사이다-3]"
    })
    void 잘못된_입력_형식에_대한_테스트(String input) {
        assertThrows(IllegalArgumentException.class, () -> {
            inputConverter.convertToPurchaseDTOs(input);
        });
    }

    @Test
    void 정상_입력_변환_테스트() {
        String input = "[콜라-10],[사이다-3],[오렌지주스-5]";

        List<PurchaseDTO> purchases = inputConverter.convertToPurchaseDTOs(input);

        assertEquals(3, purchases.size());
        assertEquals("콜라", purchases.get(0).productName());
        assertEquals(10, purchases.get(0).quantity());
        assertEquals("사이다", purchases.get(1).productName());
        assertEquals(3, purchases.get(1).quantity());
        assertEquals("오렌지주스", purchases.get(2).productName());
        assertEquals(5, purchases.get(2).quantity());
    }
}
