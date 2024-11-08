package store.view;

import java.util.List;
import store.dto.ProductDTO;

public class OutputView {

    private static final String GREETING_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String AVAILABLE_PRODUCTS_MESSAGE = "현재 보유하고 있는 상품입니다." + System.lineSeparator();

    public void printGreeting() {
        System.out.println(GREETING_MESSAGE);
    }

    public void printProducts(List<ProductDTO> productDTOS) {
        System.out.println(AVAILABLE_PRODUCTS_MESSAGE);

        for (ProductDTO productDTO : productDTOS) {
            System.out.println(buildProductString(productDTO));
        }
    }

    public void printError(String message) {
        System.out.println(message);
    }

    private String buildProductString(ProductDTO productDTO) {
        String name = productDTO.name();
        int price = productDTO.price();
        int quantity = productDTO.quantity();
        String promotion = productDTO.promotion();

        if (quantity == 0) {
            return "- " + name + " " + price + "원 재고 없음";
        }

        if (promotion == null) {
            return "- " + name + " " + price + "원 " + quantity + "개";
        }

        return "- " + name + " " + price + "원 " + quantity + "개 " + promotion;
    }
}
