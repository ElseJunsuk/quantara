package com.quant.quantara.external.ui.component;

import com.quant.quantara.internal.Star;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;

public class PromptBoxControl extends ImageView {

    private static final String PROMPT_ICON_PATH = "/icons/menu-prompt.png";

    /**
     * 해당 멤버가 true인 경우 메뉴를 사용한 프롬프트 입력 박스 컨트롤이 불가능하며,
     * 그렇지 않으먄 제어가 가능합니다.
     */
    private static boolean enabled = false;

    public PromptBoxControl(TextField promptInput) {
        URL url = Star.class.getResource(PROMPT_ICON_PATH);
        if (url == null) {
            System.out.println("[Error] 리소스 \"" + PROMPT_ICON_PATH + "\" 를 찾을 수 없습니다!");
            return;
        }
        setImage(new Image(url.toExternalForm()));
        setFitWidth(22);
        setFitHeight(22);
        setPreserveRatio(true);

        // 클릭 시 이벤트 정의
        setOnMouseClicked(event -> playPromptBoxAnimation(promptInput, enabled));
    }

    /**
     * 프롬프트 입력 박스를 위 또는 밑으로 움직이는 애니메이션을 수행합니다.
     *
     * @param down true 시 아래로, 그렇지 않으면 위로 움직이는 애니메이션 수행
     */
    public static void playPromptBoxAnimation(TextField promptInput, boolean down) {
        // 완료 시 프롬프트 입력 박스 애니메이션
        // 이동을 위한 TranslateTransition
        TranslateTransition translate = new TranslateTransition(Duration.millis(500), promptInput);
        translate.setByY(down ? 100 : -100); // 100픽셀 이동
        translate.setCycleCount(1);
        translate.setAutoReverse(false);

        // 투명도 감소를 위한 FadeTransition
        FadeTransition fade = new FadeTransition(Duration.millis(500), promptInput);
        fade.setFromValue(down ? 1. : 0); // 초기 투명도
        fade.setToValue(down ? 0 : 1.); // 끝 투명도
        fade.setCycleCount(1);
        fade.setAutoReverse(false);

        // 프롬프트 비우기
        promptInput.setText(null);

        // 두 애니메이션 병렬 실행
        ParallelTransition parallel = new ParallelTransition(translate, fade);
        parallel.play();

        enabled = !down; // 내려가 있는 경우에만 메뉴 버튼으로 올릴 수 있음
//        parallel.setOnFinished(event -> enabled = false);
    }
}
