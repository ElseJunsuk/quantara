package com.quant.quantara.external.controller;

import com.quant.quantara.QuantaraBootstrap;
import com.quant.quantara.external.config.HistoriesHolder;
import com.quant.quantara.external.ui.component.ConstellationPane;
import com.quant.quantara.internal.Star;
import com.quant.quantara.internal.quantum.EntityQubitInitialize;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.redfx.strangefx.render.RenderEngine;

import java.io.IOException;
import java.net.URL;

public class StatisticsController {

    private static final String PROMPT_ICON_PATH = "/icons/menu-statistics.png";

    @FXML
    private BorderPane rootBorderPane;

    @FXML
    private Label promptText;

    @FXML
    private Label avgSentimentScoreLabel;

    @FXML
    private Label avgSentimentMagnitudeLabel;

    @FXML
    private Label quantumProbabilityLabel;

    @FXML
    private BorderPane quantumCircuitBorder;

    public static StatisticsController controller() {
        try {
            FXMLLoader loader = new FXMLLoader(QuantaraBootstrap.class.getResource("statistics-popup.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 500);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Statistics");
            stage.show();
            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("단어 정보 팝업을 보여주는 중 예외가 발생했습니다!", e);
        }
    }

    public static void setUpStatisticsPanel(String prompt, float avgSentimentScore, float avgSentimentMagnitude) {
        StatisticsController controller = controller(); // FXML 로더가 생성한 컨트롤러 가져오기

        controller.getPromptText().setText(prompt.trim());
        controller.getAvgSentimentScoreLabel().setText(avgSentimentScore + "");
        controller.getAvgSentimentMagnitudeLabel().setText(avgSentimentMagnitude + "");
        EntityQubitInitialize qubit = new EntityQubitInitialize(avgSentimentScore)
                .normalize()
                .simulate();
        double probability = qubit.successProbability();
        controller.getQuantumProbabilityLabel().setText(probability * 100 + "%");

        // TODO: 양자역학적 시각화는 StrangeFX의 사용벙에 따라 조금씩 바꾸어나갈 예정
        final RenderEngine engine = qubit.fxRenderEngine();
        controller.getQuantumCircuitBorder().setCenter(engine);
//        engine.animate();
    }

    public static ImageView menuIconSetup(ConstellationPane selectedPane, HistoriesHolder historiesHolder) {
        final URL url = Star.class.getResource(PROMPT_ICON_PATH);
        if (url == null) {
            System.out.println("[Error] 리소스 \"" + PROMPT_ICON_PATH + "\" 를 찾을 수 없습니다!");
            return null;
        }
        Image image = new Image(url.toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setX(-30);
        imageView.setFitWidth(22);
        imageView.setFitHeight(22);
        imageView.setPreserveRatio(true);

        // 클릭 시 이벤트 정의
        imageView.setOnMouseClicked(event -> historiesHolder.getHistories().forEach((id, constellation) -> {
            if (id == selectedPane.getCurrentIndex()) {
                setUpStatisticsPanel(constellation.getPrompt().getInput(),
                        (float) constellation.getStars().stream().mapToDouble(star -> star.getEntity().getSentimentScore()).average().orElse(0D),
                        (float) constellation.getStars().stream().mapToDouble(star -> star.getEntity().getSentimentMagnitude()).average().orElse(0D)
                );
            }
        }));

        return imageView;
    }

    public BorderPane getRootBorderPane() {
        return rootBorderPane;
    }

    public void setRootBorderPane(BorderPane rootBorderPane) {
        this.rootBorderPane = rootBorderPane;
    }

    public Label getPromptText() {
        return promptText;
    }

    public void setPromptText(Label promptText) {
        this.promptText = promptText;
    }

    public Label getAvgSentimentScoreLabel() {
        return avgSentimentScoreLabel;
    }

    public void setAvgSentimentScoreLabel(Label avgSentimentScoreLabel) {
        this.avgSentimentScoreLabel = avgSentimentScoreLabel;
    }

    public Label getAvgSentimentMagnitudeLabel() {
        return avgSentimentMagnitudeLabel;
    }

    public void setAvgSentimentMagnitudeLabel(Label avgSentimentMagnitudeLabel) {
        this.avgSentimentMagnitudeLabel = avgSentimentMagnitudeLabel;
    }

    public Label getQuantumProbabilityLabel() {
        return quantumProbabilityLabel;
    }

    public void setQuantumProbabilityLabel(Label quantumProbabilityLabel) {
        this.quantumProbabilityLabel = quantumProbabilityLabel;
    }

    public BorderPane getQuantumCircuitBorder() {
        return quantumCircuitBorder;
    }

    public void setQuantumCircuitBorder(BorderPane quantumCircuitBorder) {
        this.quantumCircuitBorder = quantumCircuitBorder;
    }
}
