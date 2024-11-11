package store.model.dataloader;

import store.dto.ProductDTO;
import store.model.ErrorCode;

public class ProductDataLoader extends AbstractDataLoader<ProductDTO> {
    private static final String DELIMITER = ",";
    private static final String EMPTY_VALUE_SIGN = "null";

    public ProductDataLoader(String filePath) {
        super(filePath);
    }

    @Override
    protected ProductDTO parseLine(String line) {
        String[] fields = line.split(DELIMITER);
        if (fields.length == 4) {
            String name = fields[0].trim();
            int price = Integer.parseInt(fields[1].trim());
            int quantity = Integer.parseInt(fields[2].trim());
            String promotion = fields[3].trim();

            if (EMPTY_VALUE_SIGN.equals(promotion)) {
                promotion = null;
            }

            return new ProductDTO(name, price, quantity, promotion);
        }
        throw new IllegalArgumentException(ErrorCode.INVALID_INITIAL_DATA_FORMAT.getMessage());
    }
}
