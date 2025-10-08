package com.quant.quantara.external.ui.component;

import com.quant.quantara.QuantaraBootstrap;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.util.Duration;

import java.io.IOException;

public class WordStarHover {

    private WordStar wordStar;

    private Popup informationPopup;

    private PauseTransition hoverDelay;

    public WordStarHover(WordStar wordStar) {
        this.wordStar = wordStar;
    }

    public void initLogic() {
        this.hoverDelay = new PauseTransition(Duration.millis(300));
        this.hoverDelay.setOnFinished(event -> {
            // 마우스 위치는 이벤트에서 동적으로 얻기 위해 별도로 처리
        });

        this.wordStar.setOnMouseEntered(event -> {
//            this.hoverDelay.setOnFinished(e -> showinformationPopup(event.getScreenX(), event.getScreenY()));
//            this.hoverDelay.playFromStart();
            showinformationPopup(event.getScreenX(), event.getScreenY());
        });

        this.wordStar.setOnMouseExited(event -> {
            hoverDelay.stop();
            if (this.informationPopup != null && this.informationPopup.isShowing()) {
                this.informationPopup.hide();
            }
        });
    }

    // 다음 스텝에서 구현
    public void showinformationPopup(double mouseX, double mouseY) {
        this.informationPopup = new Popup();
        try {
            FXMLLoader loader = new FXMLLoader(QuantaraBootstrap.class.getResource("information-popup.fxml"));
            VBox popupContent = loader.load();

            // 데이터 바인딩 (Controller가 없으므로 직접 설정)
            Label wordName = (Label) popupContent.lookup("#wordName");
            wordName.setText("단어: " + wordStar.getEntity().getName());

            Label wordType = (Label) popupContent.lookup("#wordType");
            wordType.setText("유형: " + wordStar.getEntity().getType());

            Label emotionScore = (Label) popupContent.lookup("#emotionScore");
            emotionScore.setText("감정 점수: " + wordStar.getEntity().getSentimentScore());

            Label emotionMagnitude = (Label) popupContent.lookup("#emotionMagnitude");
            emotionMagnitude.setText("감정 크기: " + wordStar.getEntity().getSentimentMagnitude());

            Label sentenceVisibility = (Label) popupContent.lookup("#sentenceVisibility");
            sentenceVisibility.setText("가시성: " + wordStar.getEntity().getSalienceScore());

            informationPopup.getContent().add(popupContent);
            informationPopup.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_LEFT);

        } catch (IOException e) {
            throw new RuntimeException("단어 정보 팝업을 보여주는 중 예외가 발생했습니다!", e);
        }

        // 팝업 크기
        double popupWidth = informationPopup.getWidth() > 0 ? informationPopup.getWidth() : 200; // FXML의 prefWidth
        double popupHeight = informationPopup.getHeight() > 0 ? informationPopup.getHeight() : 150; // FXML의 prefHeight

        // 창 크기
        double windowWidth = wordStar.getScene().getWindow().getWidth();
        double windowHeight = wordStar.getScene().getWindow().getHeight();
        double windowX = wordStar.getScene().getWindow().getX();
        double windowY = wordStar.getScene().getWindow().getY();

        // 팝업 기본 위치: 마우스 커서 기준 약간 오른쪽 아래 (10px offset)
        double popupX = mouseX + 10;
        double popupY = mouseY + 10;

        // 화면 경계 체크 및 조정
        if (popupX + popupWidth > windowX + windowWidth) {
            popupX = mouseX - popupWidth - 10; // 마우스 왼쪽으로 이동
        }
        if (popupX < windowX) {
            popupX = windowX + 5; // 창 좌측 경계
        }

        if (popupY + popupHeight > windowY + windowHeight) {
            popupY = mouseY - popupHeight - 10; // 마우스 위쪽으로 이동
        }
        if (popupY < windowY) {
            popupY = windowY + 5; // 창 상단 경계
        }

        // 팝업 표시
        informationPopup.show(wordStar.getScene().getWindow(), popupX, popupY);
    }
}
