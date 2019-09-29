package ru.v1as.tg.autopoller.model;

import java.util.Map.Entry;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class LongProperty {

    String name;
    Long value;

    @Override
    public String toString() {
        return name + ": " + value;
    }

    public boolean isPositive() {
        return value != null && value > 0;
    }

    public LongProperty(Entry<String, ? extends Number> entry) {
        this.name = entry.getKey();
        this.value = entry.getValue().longValue();
    }

}
