package kr.co.mountaincc.maps.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;

public class DurationDeserializer extends JsonDeserializer<Duration> {

    @Override
    public Duration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return Duration.between(LocalTime.MIN, LocalTime.parse(p.getText())); // "HH:mm:ss" → Duration 변환
    }
}