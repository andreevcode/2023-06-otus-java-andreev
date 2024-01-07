package ru.otus.dataprocessor;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FileSerializer implements Serializer {
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        var file = new File(fileName);
        try {
            var objectMapper = new ObjectMapper();
            objectMapper.writeValue(file, data);
        } catch (IOException e) {
            throw new FileProcessException("Serialization failed");
        }
    }
}
