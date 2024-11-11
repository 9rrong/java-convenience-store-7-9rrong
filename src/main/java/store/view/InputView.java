package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    private static final String ORDER_INPUT_PROMPT =
            System.lineSeparator() + "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String YES_NO_PROMPT = " (Y/N)";
    private static final String ADD_PROMOTION_PRODUCT_PROMPT =
            "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까?" + YES_NO_PROMPT;
    private static final String BUY_ONLY_PROMOTION_PRODUCTS_PROMPT =
            "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까?" + YES_NO_PROMPT;
    private static final String MEMBERSHIP_DISCOUNT_PROMPT = "멤버십 할인을 받으시겠습니까?" + YES_NO_PROMPT;
    private static final String BUY_MORE_PRODUCT_PROMPT = "감사합니다. 구매하고 싶은 다른 상품이 있나요?" + YES_NO_PROMPT;

    public String readItem() {
        System.out.println(ORDER_INPUT_PROMPT);
        return Console.readLine();
    }

    public String promptAddPromotionProduct(String productName) {
        System.out.printf(ADD_PROMOTION_PRODUCT_PROMPT, productName);
        return Console.readLine();
    }

    public String promptBuyOnlyPromotionProduct(String productName, int nonPromotionQuantity) {
        System.out.printf(BUY_ONLY_PROMOTION_PRODUCTS_PROMPT, productName, nonPromotionQuantity);
        return Console.readLine();
    }

    public String promptMembershipDiscount() {
        System.out.println(MEMBERSHIP_DISCOUNT_PROMPT);
        return Console.readLine();
    }

    public String promptBuyMoreProducts() {
        System.out.println(BUY_MORE_PRODUCT_PROMPT);
        return Console.readLine();
    }
}