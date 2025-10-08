package com.quant.quantara.internal.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.quant.quantara.external.ui.component.WordStar;

import java.io.IOException;

public class WordStarSerializer extends JsonSerializer<WordStar> {

    @Override
    public void serialize(WordStar wordStar, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        // 엔티티 정보
        jsonGenerator.writeObjectField("entity", wordStar.getEntity());

        // raw X, Y 좌표
        jsonGenerator.writeNumberField("rawX", wordStar.getRawX());
        jsonGenerator.writeNumberField("rawY", wordStar.getRawY());

        jsonGenerator.writeEndObject();
    }
}
