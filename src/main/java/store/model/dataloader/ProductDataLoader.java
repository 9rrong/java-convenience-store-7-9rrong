package store.model.dataloader;

import store.dto.ProductDTO;

public class ProductDataLoader extends AbstractDataLoader<ProductDTO> {

    public ProductDataLoader(String filePath) {
        super(filePath);
    }

    @Override
    protected ProductDTO parseLine(String line) {
        String[] fields = line.split(",");
        if (fields.length == 4) {
            String name = fields[0].trim();
            int price = Integer.parseInt(fields[1].trim());
            int quantity = Integer.parseInt(fields[2].trim());
            String promotion = fields[3].trim();

            if ("null".equals(promotion)) {
                promotion = null;
            }

            return new ProductDTO(name, price, quantity, promotion);
        }
        throw new IllegalArgumentException("[ERROR] 초기 데이터가 잘못된 형식입니다.");
    }
}
