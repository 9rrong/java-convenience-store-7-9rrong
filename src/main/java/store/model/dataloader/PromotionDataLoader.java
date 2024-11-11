package store.model.dataloader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import store.dto.PromotionDTO;
import store.model.ErrorCode;

public class PromotionDataLoader extends AbstractDataLoader<PromotionDTO> {
    private static final String DELIMITER = ",";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final int PROMOTION_FORMAT_SIZE = 5;

    public PromotionDataLoader(String filePath) {
        super(filePath);
    }

    @Override
    protected PromotionDTO parseLine(String line) {
        String[] fields = line.split(DELIMITER);
        validateFormat(fields);

        return new PromotionDTO(
                fields[0].trim(), Integer.parseInt(fields[1].trim()), Integer.parseInt(fields[2].trim()),
                parseDate(fields[3].trim()), parseDate(fields[4].trim())
        );
    }

    private static void validateFormat(String[] fields) {
        if (fields.length != PROMOTION_FORMAT_SIZE) {
            throw new IllegalArgumentException(ErrorCode.INVALID_INITIAL_DATA_FORMAT.getMessage());
        }
    }

    private LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_FORMAT);
    }
}
