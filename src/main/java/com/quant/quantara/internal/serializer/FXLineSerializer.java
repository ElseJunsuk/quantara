package com.quant.quantara.internal.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

import java.io.IOException;

public class FXLineSerializer extends JsonSerializer<Line> {

    @Override
    public void serialize(Line line, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        // 기본 속성
        jsonGenerator.writeNumberField("startX", line.getStartX());
        jsonGenerator.writeNumberField("startY", line.getStartY());
        jsonGenerator.writeNumberField("endX", line.getEndX());
        jsonGenerator.writeNumberField("endY", line.getEndY());

        // 추가 속성들
        jsonGenerator.writeNumberField("strokeWidth", line.getStrokeWidth());
        // stroke (Paint) 속성의 경우 Color로 가정하고 RGBA 추출
        Paint stroke = line.getStroke();
        if (stroke instanceof Color color) {
            jsonGenerator.writeObjectFieldStart("stroke");
            jsonGenerator.writeNumberField("red", color.getRed());
            jsonGenerator.writeNumberField("green", color.getGreen());
            jsonGenerator.writeNumberField("blue", color.getBlue());
            jsonGenerator.writeNumberField("opacity", color.getOpacity());
            jsonGenerator.writeEndObject();
        } else {
            // 다른 Paint 타입인 경우 별도 처리 필요
            jsonGenerator.writeNullField("stroke");  // 또는 커스텀 로직 추가
        }

        // effect 속성은 Effect subclass에 따라 분기 처리
        Effect effect = line.getEffect();
        if (effect == null) {
            jsonGenerator.writeNullField("effect");
        } else {
            jsonGenerator.writeObjectFieldStart("effect");
            jsonGenerator.writeStringField("type", effect.getClass().getSimpleName());
            // 구체적 subclass 속성 추출 (예: DropShadow인 경우)
            if (effect instanceof javafx.scene.effect.DropShadow ds) {
                jsonGenerator.writeNumberField("offsetX", ds.getOffsetX());
                jsonGenerator.writeNumberField("offsetY", ds.getOffsetY());
                jsonGenerator.writeNumberField("radius", ds.getRadius());

                // 색상도 Color로 처리
                Color shadowColor = ds.getColor();
                jsonGenerator.writeObjectFieldStart("color");
                jsonGenerator.writeNumberField("red", shadowColor.getRed());
                jsonGenerator.writeNumberField("green", shadowColor.getGreen());
                jsonGenerator.writeNumberField("blue", shadowColor.getBlue());
                jsonGenerator.writeNumberField("opacity", shadowColor.getOpacity());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndObject();
    }
}
