package com.quant.quantara.external.ui;

import com.quant.quantara.external.ui.component.WordStar;

/**
 * 별자리가 만들어질 때, 별과 별 사이를 잇기 위해 사용되는 클래스입니다.
 * 1번 별과 2번 별 사이의 거리를 계산할 때 필요한 도구를 객체화하기위해 필요합니다.
 *
 * @author Q. T. Felix
 * @since 1.0.0
 */
public class Edge {

    // 엔티티 래퍼 클래스에는 자신이 위치한 X, Y raw 오프셋이 정의되어 있기에 엔티티 래퍼 객체를 멤버(노드)로 사용
    private WordStar firstNode;
    private WordStar secondNode;

    private double distance;

    public Edge() {
        this.firstNode = new WordStar();
        this.secondNode = new WordStar();
        this.distance = 0;
    }

    public Edge(WordStar firstNode, WordStar secondNode, double distance) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.distance = distance;
    }

    public WordStar getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(WordStar firstNode) {
        this.firstNode = firstNode;
    }

    public WordStar getSecondNode() {
        return secondNode;
    }

    public void setSecondNode(WordStar secondNode) {
        this.secondNode = secondNode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
