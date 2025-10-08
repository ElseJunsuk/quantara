package com.quant.quantara.external.ui.component;

import com.quant.quantara.external.config.HistoriesHolder;
import com.quant.quantara.external.config.UserConfiguration;
import com.quant.quantara.external.controller.StatisticsController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ConstellationPane extends StackPane {

    private int currentIndex;

    public void addNewConstellationAndUpdatePane(UserConfiguration userConfiguration, HistoriesHolder historiesHolder, TextField promptInput, NextPrevBtnBorder slideButtons, StatisticsController statisticsComp) {
        Platform.runLater(() -> {
            try {
                promptInput.setDisable(true);
                final Constellation constellation = Constellation.createConstellation(userConfiguration, historiesHolder, promptInput, getMaxWidth(), getMaxHeight());
                getChildren().add(constellation.wrapGroup());
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException("별자리 생성 도중 예외가 발생했습니다!", e);
            } catch (TimeoutException e) {
                throw new RuntimeException("설정된 최대 시간 '" + userConfiguration.getCreateConstellationTimeoutMilli() + "ms' 내에 별자리를 생성하지 못했습니다!", e);
            }

            setCurrentIndex(getLastChildIndex());
            last();
            updateSlideButtons(slideButtons);
            PromptBoxControl.playPromptBoxAnimation(promptInput, true);
        });
    }

    public void firstLoad(boolean existHistory) {
        setCurrentIndex(0); // 재시작 시 항상 첫 번째 별자리만 보이도록
        if (existHistory)
            updateAllChildren();
    }

    public Node next() {
        final int next = currentIndex + 1;
        if (next >= getChildren().size())
            return getChildren().getLast();
        Node result = getChildren().get(next);
        setCurrentIndex(next);
        updateAllChildren();
        return result;
    }

    public Node previous() {
        final int previous = currentIndex - 1;
        if (previous < 0)
            return getChildren().getFirst();
        Node result = getChildren().get(previous);
        setCurrentIndex(previous);
        updateAllChildren();
        return result;
    }

    /**
     * 마지막 별자리 페이지로 이동합니다.
     *
     * @return 마지막 별자리 노드
     */
    public Node last() {
        Node result = getChildren().getLast();
        setCurrentIndex(getLastChildIndex());
        updateAllChildren();
        return result;
    }

    public boolean hasNext() {
        return getCurrentIndex() + 1 < getChildren().size();
    }

    public boolean hasPrevious() {
        return getCurrentIndex() > 0;
    }

    /**
     * 사용자가 현재 보고있는 별자리 인덱스를 반환합니다.
     *
     * @return 사용자가 현재 보고있는 별자리 인덱스
     */
    public int getCurrentIndex() {
        return (currentIndex < 0 || currentIndex >= getChildren().size()) ? 0 : currentIndex;
    }

    public int getLastChildIndex() {
        return getChildren().size() - 1;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    /**
     * {@link #getCurrentIndex()} 메소드를 통해 현재 설정된 노드만
     * 보이도록 설정합니다.
     */
    public void updateAllChildren() {
        System.out.println("[Debug] 현재 보고있는 별자리와 페이지 로깅 (총 " + getChildren().size() + "개): ");
        for (int i = 0; i < getChildren().size(); i++) {
            getChildren().get(i).setVisible(i == getCurrentIndex());
            // 디버그 메시지
            System.out.println("    " + i + "번: " + (getChildren().get(i).isVisible() ? "활성화" : "비활성화"));
        }
    }

    /**
     * 좌/우측 벽에 붙어있는 슬라이드 버튼을 업데이트합니다.
     */
    public void updateSlideButtons(NextPrevBtnBorder slideButtons) {
        slideButtons.getNextButton().setVisible(hasNext());
        slideButtons.getPreviousButton().setVisible(hasPrevious());
    }

}
