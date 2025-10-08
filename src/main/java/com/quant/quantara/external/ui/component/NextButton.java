package com.quant.quantara.external.ui.component;

import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class NextButton extends Button {

    public NextButton() {
        setOnMouseEntered(event -> {
            FadeTransition fade = new FadeTransition(Duration.millis(100), this);

            fade.setFromValue(.4);
            fade.setToValue(.8);

            fade.play();
        });
        setOnMouseExited(event -> {
            FadeTransition fade = new FadeTransition(Duration.millis(100), this);

            fade.setFromValue(.8);
            fade.setToValue(.4);

            fade.play();
        });

        setGraphic(new NextPrevBtnBorder.NextBtnIcon());
    }

}
