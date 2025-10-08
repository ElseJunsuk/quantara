package com.quant.quantara.external.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Configuration {

    public static UserConfiguration getUserConfiguration() {
        final ObjectMapper mapper = new ObjectMapper();
        Path configPath = Paths.get("priv-resources", "config.json");
        if (Files.notExists(configPath))
            throw new RuntimeException("사용자 구성 파일이 존재하지 않습니다! \"priv-resources\" 디렉토리 하위에 \"config.json\" 파일을 추가하세요!");
        try (InputStream stream = Files.newInputStream(configPath)) {
            return mapper.readValue(stream, UserConfiguration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
