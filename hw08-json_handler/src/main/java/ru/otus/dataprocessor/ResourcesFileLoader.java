package ru.otus.dataprocessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        List<Measurement> measurements;
        try (var is = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            var jsonMapper = new ObjectMapper();
            measurements = Arrays.asList(jsonMapper.readValue(is, Measurement[].class));
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
        return measurements;
    }
}
