package store.model.dataloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDataLoader<T> {

    private final String filePath;

    public AbstractDataLoader(String filePath) {
        this.filePath = filePath;
    }

    public List<T> loadFromFile() {
        List<T> dataList = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
        ) {
            List<String> lines = bufferedReader.lines()
                    .skip(1)
                    .toList();

            if (lines.isEmpty()) {
                throw new IOException();
            }

            for (String line : lines) {
                T data = parseLine(line);
                dataList.add(data);
            }
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 초기 데이터를 읽어오는 데에 실패했습니다.");
        }

        return dataList;
    }

    protected abstract T parseLine(String line);
}
