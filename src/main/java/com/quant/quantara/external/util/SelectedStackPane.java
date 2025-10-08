package com.quant.quantara.external.util;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class SelectedStackPane extends StackPane {

    private int currentIndex;

    public void firstLoad() {
        setCurrentIndex(0); // 재시작 시 항상 첫 번째 별자리만 보이도록
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
        for (int i = 0; i < getChildren().size(); i++) {
            getChildren().get(i).setVisible(i == getCurrentIndex());
            // 디버그 메시지
            System.out.println(i + ": " + getChildren().get(i).isVisible());
        }
    }

}
