package store.model.dataloader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import store.dto.PromotionDTO;
import store.model.ErrorCode;

public class PromotionDataLoader extends AbstractDataLoader<PromotionDTO> {
    private static final String DELIMITER = ",";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PromotionDataLoader(String filePath) {
        super(filePath);
    }

    @Override
    protected PromotionDTO parseLine(String line) {
        String[] fields = line.split(DELIMITER);
        if (fields.length == 5) {
            String name = fields[0].trim();
            int buy = Integer.parseInt(fields[1].trim());
            int get = Integer.parseInt(fields[2].trim());
            LocalDate startDate = LocalDate.parse(fields[3].trim(), DATE_FORMAT);
            LocalDate endDate = LocalDate.parse(fields[4].trim(), DATE_FORMAT);

            return new PromotionDTO(name, buy, get, startDate, endDate);
        }
        throw new IllegalArgumentException(ErrorCode.INVALID_INITIAL_DATA_FORMAT.getMessage());
    }
}
