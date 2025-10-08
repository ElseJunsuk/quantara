package com.quant.quantara.internal.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.IOException;

public class FXLineDeserializer extends JsonDeserializer<Line> {

    @Override
    public Line deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        // 기본 좌표 속성 추출
        double startX = node.get("startX").asDouble();
        double startY = node.get("startY").asDouble();
        double endX = node.get("endX").asDouble();
        double endY = node.get("endY").asDouble();

        // 좌표값이 할당된 라인 인스턴스 생성
        Line line = new Line(startX, startY, endX, endY);
        // 스타일 속성 추출
        line.setStrokeWidth(node.get("strokeWidth").asDouble());

        // stroke (Paint) 추출 (Color)
        JsonNode strokeNode = node.get("stroke");
        if (strokeNode != null && !strokeNode.isNull()) {
            line.setStroke(extractColor(strokeNode));
        }

        // effect 추출
        JsonNode effectNode = node.get("effect");
        if (effectNode != null && !effectNode.isNull()) {
            String type = effectNode.get("type").asText();
            if ("DropShadow".equals(type)) { // DropShadow 처리 (다른 Effect subclass 확장 가능)
                double offsetX = effectNode.get("offsetX").asDouble();
                double offsetY = effectNode.get("offsetY").asDouble();
                double radius = effectNode.get("radius").asDouble();

                JsonNode colorNode = effectNode.get("color");
                DropShadow ds = new DropShadow(radius, offsetX, offsetY, extractColor(colorNode));
                line.setEffect(ds);
            }
            // 이 외 이펙트
        }

        return line;
    }

    /**
     * 노드에서 {@link Color}를 추출합니다.
     *
     * @param node 추출할 노드
     * @return 색 객체
     */
    private Color extractColor(JsonNode node) {
        return new Color(
                node.get("red").asDouble(),
                node.get("blue").asDouble(),
                node.get("green").asDouble(),
                node.get("opacity").asDouble()
        );
    }

}
