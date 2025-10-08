package com.quant.quantara.external.controller;

import com.quant.quantara.external.config.Configuration;
import com.quant.quantara.external.config.HistoriesHolder;
import com.quant.quantara.external.config.UserConfiguration;
import com.quant.quantara.external.ui.component.ConstellationPane;
import com.quant.quantara.external.ui.component.NextPrevBtnBorder;
import com.quant.quantara.external.ui.component.PromptBoxControl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    public static final Color MISC_COLOR = Color.web("#FFFFE6");

    private UserConfiguration userConfiguration;
    private HistoriesHolder historiesHolder;

    @FXML
    public StackPane rootStackPane;

    @FXML
    public Group topMenu;

    @FXML
    public NextPrevBtnBorder slideButtons;

    @FXML
    public ConstellationPane selectedPane;

    @FXML
    public TextField promptInput;

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 히스토리 / 유저 구성 로드
        this.historiesHolder = HistoriesHolder.deserialize();
        this.userConfiguration = Configuration.getUserConfiguration();
        loadConstellationFromHistory();

        // 이벤트 추가
        addEvents();
        // 배경에 별 추가
        addBackgroundStars();
        // 상단 메뉴 버튼 추가
        setUpTopMenu();
    }

    /**
     * 좌측 상단의 메뉴를 세팅합니다.
     */
    private void setUpTopMenu() {
        topMenu.getChildren().add(new PromptBoxControl(promptInput));
        topMenu.getChildren().add(StatisticsController.menuIconSetup(selectedPane, historiesHolder));
    }

    private void addEvents() {
        // 프롬프트 박스 이벤트
        promptInput.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (promptInput.getText().isEmpty() || promptInput.getText().isBlank())
                    return;
                // 별자리 생성
                selectedPane.addNewConstellationAndUpdatePane(userConfiguration, historiesHolder, promptInput, slideButtons,
                        new StatisticsController());
            } else if (event.getCode().equals(KeyCode.LEFT)) {
                slideAndUpdate(false);
            } else if (event.getCode().equals(KeyCode.RIGHT)) {
                slideAndUpdate(true);
            }
        });

        // 클릭 슬라이드 이벤트
        slideButtons.getPreviousButton().setOnMouseClicked(event -> slideAndUpdate(false));
        slideButtons.getNextButton().setOnMouseClicked(event -> slideAndUpdate(true));
    }

    /**
     * {@link HistoriesHolder}에 저장된 별자리 목록을 통해 만들어진 별자리를 로드하고
     * UI에 배치합니다.
     * <p>만약 저장된 별자리가 없다면 아무것도 하지 않습니다.
     */
    private void loadConstellationFromHistory() {
        // 별자리 기록이 없는 경우 아무것도 안 해도 됌
        if (historiesHolder.getHistories().isEmpty()) {
            selectedPane.firstLoad(false);
            // 프롬프트 박스는 FXML로 인해 기본적으로 올라와 있어서 별도의 박스 애니메이션 필요 X
            return;
        }
        // 별자리 기록이 있는 경우 먼저 프롬프트 박스 내리기
        PromptBoxControl.playPromptBoxAnimation(promptInput, true);
        // 별자리 데이터들 로드(해당 판에 자식 노드로 추가)
        historiesHolder.getHistories().forEach((key, value) -> {
            Group group = new Group();
            group.getChildren().addAll(value.getStars());
            group.getChildren().addAll(value.getLines());
            selectedPane.getChildren().add(group);
        });
        // 가시성 업데이트
        selectedPane.firstLoad(true);
        selectedPane.updateSlideButtons(slideButtons);
    }

    private void slideAndUpdate(boolean next) {
        if (next)
            selectedPane.next();
        else
            selectedPane.previous();
        selectedPane.updateSlideButtons(slideButtons);
    }

    /**
     * 배경에 장식용 별을 낮은 빈도로 무작위 배치하기 위한 메소드입니다.
     */
    private void addBackgroundStars() {
        Group backgroundStars = new Group();
        Random random = new Random();
        int starCount = 50; // 낮은 빈도를 위해 별 개수 제한 (조정 가능)

        // 루트 StackPane의 크기 기준으로 배치
        double width = rootStackPane.getPrefWidth(); // FXML 또는 코드로 설정된 크기
        double height = rootStackPane.getPrefHeight();

        // prefWidth/Height가 설정되지 않은 경우 기본값 사용
        if (width <= 0) width = 800; // 예시 기본값
        if (height <= 0) height = 600;

        for (int i = 0; i < starCount; i++) {
            // 무작위 좌표 생성
            double x = random.nextDouble() * width;
            double y = random.nextDouble() * height;

            // 별 생성: 색상 #FFFFE6, 반지름 2px, 무작위 투명도
            Circle star = new Circle(x, y, 1);
            star.setFill(MISC_COLOR);
            star.setOpacity(0.1 + random.nextDouble() * 0.4); // 투명도 0.1 ~ 0.5

            backgroundStars.getChildren().add(star);
        }

        // 배경 별 Group을 루트 StackPane의 첫 번째 자식으로 추가 (최하위 레이어)
        rootStackPane.getChildren().addFirst(backgroundStars);
    }

}
