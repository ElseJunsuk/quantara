package com.quant.quantara.internal.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quant.quantara.external.ui.component.Constellation;
import com.quant.quantara.external.ui.component.WordStar;
import com.quant.quantara.internal.Prompt;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ConstellationDeserializer extends JsonDeserializer<Constellation> {
    private static final Logger LOGGER = Logger.getLogger(ConstellationDeserializer.class.getName());

    @Override
    public Constellation deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        Constellation constellation = new Constellation();
        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        // stars 필드 처리
        JsonNode starsNode = node.get("stars");
        List<WordStar> stars = new ArrayList<>();
        if (starsNode != null && starsNode.isArray()) {
            for (JsonNode starNode : starsNode) {
                try {
                    if (!starNode.isNull()) {
                        WordStar wordStar = mapper.treeToValue(starNode, WordStar.class);
                        if (wordStar != null) {
                            stars.add(wordStar);
                        } else {
                            LOGGER.warning("WordStar 오브젝트 역직렬화를 실패했습니다! 상세: " + starNode);
                        }
                    } else {
                        LOGGER.warning("starNode 에서 stars 이(가) null 입니다");
                    }
                } catch (Exception e) {
                    LOGGER.severe("WordStar 역직렬화중 예외가 발생했습니다! 상세: " + e.getMessage());
                }
            }
        } else {
            LOGGER.warning("stars 노드가 null 이거나 배열이 아닙니다!");
        }
        constellation.setStars(stars);

        // lines 필드 처리
        JsonNode linesNode = node.get("lines");
        List<Line> lines = new ArrayList<>();
        if (linesNode != null && linesNode.isArray()) {
            for (JsonNode lineNode : linesNode) {
                try {
                    if (!lineNode.isNull()) {
                        Line line = mapper.treeToValue(lineNode, Line.class);
                        if (line != null) {
                            lines.add(line);
                        } else {
                            LOGGER.warning("Line 오브젝트 역직렬화를 실패했습니다! 상세: " + lineNode);
                        }
                    } else {
                        LOGGER.warning("lineNode 에서 lines 이(가) null 입니다");
                    }
                } catch (Exception e) {
                    LOGGER.severe("Line 역직렬화중 예외가 발생했습니다! 상세: " + e.getMessage());
                }
            }
        } else {
            LOGGER.warning("lines 노드가 null 이거나 배열이 아닙니다!");
        }
        constellation.setPrompt(mapper.treeToValue(node.get("prompt"), Prompt.class));
        constellation.setLines(lines);
        constellation.setFullySuccess(true);
        return constellation;
    }
}