package com.quant.quantara.external.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.quant.quantara.internal.serializer.*;
import com.quant.quantara.external.ui.component.Constellation;
import com.quant.quantara.external.ui.component.WordStar;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;

public class HistoriesHolder {

    @JsonIgnore
    static ObjectMapper mapper = new ObjectMapper();
    @JsonIgnore
    static Path path = Paths.get("priv-resources", "histories.json");

    static {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule
                .addSerializer(Line.class, new FXLineSerializer())
                .addDeserializer(Line.class, new FXLineDeserializer())

                .addSerializer(WordStar.class, new WordStarSerializer())
                .addDeserializer(WordStar.class, new WordStarDeserializer())

                .addDeserializer(Constellation.class, new ConstellationDeserializer());
        HistoriesHolder.mapper.registerModule(simpleModule);
    }

    private TreeMap<Integer, Constellation> histories;

    private int nextID;

    public HistoriesHolder() {
        this(new TreeMap<>(), 0);
    }

    public HistoriesHolder(TreeMap<Integer, Constellation> histories, int nextID) {
        this.histories = histories != null ? histories : new TreeMap<>();
        this.nextID = nextID;
    }

    // 새 데이터 추가: ID 부여하고 반환
    public int addData(Constellation constellation) {
        int id = nextID++;
        this.histories.put(id, constellation);
        return id;
    }

    // 데이터 제거: ID로 제거, nextID不变
    public void removeData(int id) {
        this.histories.remove(id);
    }

    // 데이터 조회
    public Constellation getData(int id) {
        return this.histories.get(id);
    }

    public TreeMap<Integer, Constellation> getHistories() {
        return histories;
    }

    public void setHistories(TreeMap<Integer, Constellation> histories) {
        this.histories = histories;
    }

    public int getNextID() {
        return nextID;
    }

    public void setNextID(int nextID) {
        this.nextID = nextID;
    }

    public static HistoriesHolder deserialize() {
        if (Files.notExists(path)) {
            createFile(path);
            return new HistoriesHolder();
        }
        try {
            return mapper.readValue(Files.readAllBytes(path), HistoriesHolder.class);
        } catch (IOException e) {
            throw new RuntimeException("별자리 데이터 파일 \"histories.json\"을 읽는 도중 예외가 발생했습니다! 상세: " + e.getMessage());
        }
    }

    public static void serialize(HistoriesHolder holder) {
        if (Files.notExists(path)) {
            createFile(path);
            return;
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(Files.newOutputStream(path), holder);
        } catch (IOException e) {
            throw new RuntimeException("별자리 데이터 파일 \"histories.json\"에 직렬화(쓰기)중에 예외가 발생했습니다!");
        }
        // 디버그 메시지
        System.out.println("[Debug] 현재 런타임에서 생성된 모든 별자리가 직렬화되었습니다!");
    }

    private static void createFile(final Path path) {
        // 디버그 메시지
        System.out.println("[Debug] \"priv-resources\" 디렉토리 하위에 \"histories.json\" 파일이 존재하지 않아 생성했습니다! 앞으로 생성되는 별자리는 이 곳에 직렬화됩니다.");
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new RuntimeException("별자리 데이터 파일 \"histories.json\"을 생성하는 도중 예외가 발생했습니다!");
        }
    }
}
