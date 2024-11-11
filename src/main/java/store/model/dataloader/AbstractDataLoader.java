package store.model.dataloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import store.model.ErrorCode;

public abstract class AbstractDataLoader<T> {
    private static final int HEADER_LINE_LENGTH = 1;
    private final String filePath;

    public AbstractDataLoader(String filePath) {
        this.filePath = filePath;
    }

    public List<T> loadFromFile() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            return bufferedReader.lines()
                    .skip(HEADER_LINE_LENGTH)
                    .map(this::parseLine)
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.INITIAL_DATA_LOADING_FAILURE.getMessage());
        }
    }

    protected abstract T parseLine(String line);
}
