package ru.otus.dataprocessor;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.otus.model.Measurement;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {

        return data.stream()
                .sorted(Comparator.comparing(Measurement::name))
                .collect(groupingBy(
                                Measurement::name,
                                LinkedHashMap::new,
                                summingDouble(Measurement::value)
                        )
                );
    }
}
