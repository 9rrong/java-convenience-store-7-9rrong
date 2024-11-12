package store.model.dataloader;

import store.dto.ProductDTO;
import store.model.ErrorCode;

public class ProductDataLoader extends AbstractDataLoader<ProductDTO> {
    private static final String DELIMITER = ",";
    private static final String EMPTY_VALUE_SIGN = "null";
    public static final int PRODUCT_FORMAT_SIZE = 4;

    public ProductDataLoader(String filePath) {
        super(filePath);
    }

    @Override
    protected ProductDTO parseLine(String line) {
        String[] fields = line.split(DELIMITER);
        validateFormat(fields);

        String promotion = fields[3].trim();

        if (EMPTY_VALUE_SIGN.equals(promotion)) {
            promotion = null;
        }

        return new ProductDTO(fields[0].trim(), Integer.parseInt(fields[1].trim()),
                Integer.parseInt(fields[2].trim()), promotion);
    }

    private static void validateFormat(String[] fields) {
        if (fields.length != PRODUCT_FORMAT_SIZE) {
            throw new IllegalArgumentException(ErrorCode.INVALID_INITIAL_DATA_FORMAT.getMessage());
        }
    }
}
