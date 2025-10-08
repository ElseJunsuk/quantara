package com.quant.quantara.external.ui.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.quant.quantara.external.config.HistoriesHolder;
import com.quant.quantara.external.config.UserConfiguration;
import com.quant.quantara.external.ui.LineGenerator;
import com.quant.quantara.external.util.AnalyzeTask;
import com.quant.quantara.external.util.Async;
import com.quant.quantara.external.util.BiResult;
import com.quant.quantara.internal.GCEntityWrapper;
import com.quant.quantara.internal.Prompt;
import com.quant.quantara.internal.serializer.ConstellationDeserializer;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@JsonDeserialize(using = ConstellationDeserializer.class)
public class Constellation {

    private Prompt prompt;
    private List<WordStar> stars;
    private List<Line> lines;

    @JsonIgnore
    private boolean isFullySuccess = false;

    /**
     * 별자리 객체를 역직렬화할 때 사용되는 빈 별자리를 만들기 위한 생성자입니다.
     * <p>{@link ConstellationDeserializer}에서 사용됩니다.
     */
    public Constellation() {
        this.prompt = Prompt.empty();
        this.stars = new ArrayList<>();
        this.lines = new ArrayList<>();
    }

    /**
     * 별 객체 {@link WordStar} 리스트를 전달받고 별자리 객체를 만들기 위한 생성자입니다.
     * <p>새로운 별자리를 만들 때 사용됩니다.
     *
     * @param stars 별 리스트
     */
    public Constellation(Prompt prompt, List<WordStar> stars) {
        this.prompt = prompt;
        this.stars = stars;
        if (!stars.isEmpty())
            this.lines = LineGenerator.connectStarsWithKruskal(stars);
        else
            this.lines = new ArrayList<>();
        this.isFullySuccess = true;
    }

    @JsonIgnore
    public Group wrapGroup() {
        Group group = new Group();
        group.getChildren().addAll(stars);
        group.getChildren().addAll(lines);
        return group;
    }

    public Prompt getPrompt() {
        return prompt;
    }

    public void setPrompt(Prompt prompt) {
        this.prompt = prompt;
    }

    public List<WordStar> getStars() {
        return stars;
    }

    public void setStars(List<WordStar> stars) {
        this.stars = stars;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public boolean isFullySuccess() {
        return isFullySuccess;
    }

    public void setFullySuccess(boolean fullySuccess) {
        isFullySuccess = fullySuccess;
    }

    public static Constellation createConstellation(UserConfiguration userConfiguration, HistoriesHolder historiesHolder, TextField promptInput, double borderWidth, double borderHeight)
            throws ExecutionException, InterruptedException, TimeoutException {
        // 비동기 작업
        Constellation constellation = Async
                .supplyAsync(new AnalyzeTask(userConfiguration, promptInput))
                .thenApply(results -> { // 분석 작업 완료 시 수행

                    // 디버그 메시지
                    System.out.println("[Debug] 프롬프트 분석 작업 완료 후 엔티티 FXAT 시각화 작업 시작");

                    List<WordStar> stars = results.getSecondResult().stream()
                            .map(entity -> new WordStar(entity, borderWidth, borderHeight))
                            .toList();

                    // 작업 완료 시 프롬프트 입력 상자 비우기
                    final Constellation temp = new Constellation(results.getFirstResult(), stars);
                    promptInput.setText("");
                    return temp;
                }).get(userConfiguration.getCreateConstellationTimeoutMilli(), TimeUnit.MILLISECONDS);

        // 역사 데이터 삽입 후 직렬화
        historiesHolder.addData(constellation);
        HistoriesHolder.serialize(historiesHolder);

        return constellation;
    }
}
