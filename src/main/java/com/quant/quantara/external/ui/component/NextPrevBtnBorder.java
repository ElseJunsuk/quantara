package com.quant.quantara.external.ui.component;

import com.quant.quantara.internal.Star;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.net.URL;

public class NextPrevBtnBorder extends BorderPane {

    public static final String NEXT_ICON = "/icons/next-arrow.png";
    public static final String PREV_ICON = "/icons/prev-arrow.png";
    public static final int FIT_WIDTH = 25, FIT_HEIGHT = 25;

    public NextButton getNextButton() {
        return (NextButton) getChildren().get(1);
    }

    public PreviousButton getPreviousButton() {
        return (PreviousButton) getChildren().getFirst();
    }

    public static class NextBtnIcon extends ImageView {
        public NextBtnIcon() {
            URL url = Star.class.getResource(NEXT_ICON);
            if (url == null) {
                System.out.println("[Error] 리소스 \"" + NEXT_ICON + "\" 를 찾을 수 없습니다!");
                return;
            }
            setImage(new Image(url.toExternalForm()));
            setFitWidth(FIT_WIDTH);
            setFitHeight(FIT_HEIGHT);
            setPreserveRatio(true);

            setStyle("-fx-opacity: .4;");
        }
    }

    public static class PrevBtnIcon extends ImageView {
        public PrevBtnIcon() {
            URL url = Star.class.getResource(PREV_ICON);
            if (url == null) {
                System.out.println("[Error] 리소스 \"" + NEXT_ICON + "\" 를 찾을 수 없습니다!");
                return;
            }
            setImage(new Image(url.toExternalForm()));
            setFitWidth(FIT_WIDTH);
            setFitHeight(FIT_HEIGHT);
            setPreserveRatio(true);

            setStyle("-fx-opacity: .4;");
        }
    }
}
