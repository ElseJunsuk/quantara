package com.quant.quantara.internal.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.quant.quantara.internal.GCEntityWrapper;
import com.quant.quantara.external.ui.component.WordStar;

import java.io.IOException;

public class WordStarDeserializer extends JsonDeserializer<WordStar> {

    @Override
    public WordStar deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        // 엔티티 정보
        final JsonNode entityNode = node.get("entity");
        String name = entityNode.get("name").asText();
        String type = entityNode.get("type").asText();
        float sentimentScore = (float) entityNode.get("sentimentScore").asDouble();
        float sentimentMagnitude = (float) entityNode.get("sentimentMagnitude").asDouble();
        float salienceScore = (float) entityNode.get("salienceScore").asDouble();

        // raw X, Y 좌표 정보
        double rawX = node.get("rawX").asDouble();
        double rawY = node.get("rawY").asDouble();

        // 객체 생성
        final GCEntityWrapper entity = new GCEntityWrapper(name, type, sentimentScore, sentimentMagnitude, salienceScore);
        return WordStar.of(entity, rawX, rawY).iconSetup();
    }

}
