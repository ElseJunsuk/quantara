package com.quant.quantara.external.ui;

import com.quant.quantara.external.util.SimpleEffectGenerator;
import com.quant.quantara.external.ui.component.WordStar;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 별자리를 구성할 때 별과 별 사이를 잇는 모든 라인을 담는 클래스입니다.
 * <p>{@code Kruskal} 알고리즘을 사용하여 별을 잇습니다.
 *
 * @author Q. T. Felix
 * @since 1.0.0
 */
public final class LineGenerator {

    private static final Color LINE_COLOR = Color.web("#D0FCFF");

    /**
     * 별과 별 사이를 {@code Kruskal} 알고리즘을 사용하여 잇습니다.
     *
     * @return 이어진 모든 {@link Line}
     */
    public static List<Line> connectStarsWithKruskal(final List<WordStar> stars) {
        int size = stars.size();
        List<Edge> edges = createPossibleEdges(stars);

        // Union-Find 객체 초기화
        UnionFind unionFind = new UnionFind(size);

        // 반환을 위한 빈 라인 리스트 생성
        List<Line> lines = new ArrayList<>();

        // Kruskal 알고리즘 적용
        for (Edge edge : edges) {
            int root1 = unionFind.find(stars.indexOf(edge.getFirstNode()));
            int root2 = unionFind.find(stars.indexOf(edge.getSecondNode()));
            if (root1 != root2) {
                unionFind.union(root1, root2);
                Line line = new Line(
                        edge.getFirstNode().getRawX(), edge.getFirstNode().getRawY(),
                        edge.getSecondNode().getRawX(), edge.getSecondNode().getRawY()
                );
                line.setStroke(LINE_COLOR);
                line.setOpacity(.5);
                line.setStrokeWidth(1.0);
                line.setEffect(SimpleEffectGenerator.dropShadow(15, LINE_COLOR));

                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * 별 끼리의 모든 가능한 간선(edge)을 생성하는 메소드입니다.
     *
     * @param stars 별 리스트
     * @return 모든 가능한 간선 {@link Edge} 객체
     */
    public static List<Edge> createPossibleEdges(final List<WordStar> stars) {
        List<Edge> edges = new ArrayList<>();

        // 별 끼리의 모든 가능한 간선(edge) 생성
        for (int i = 0; i < stars.size(); i++) {
            for (int j = i + 1; j < stars.size(); j++) {
                double distance = euclideanDistanceFromWordStar(stars.get(i), stars.get(j));
                edges.add(new Edge(stars.get(i), stars.get(j), distance));
            }
        }

        // 거리 기준 정렬
        edges.sort(Comparator.comparingDouble(Edge::getDistance));
        return edges;
    }

    /**
     * 두 별 사이의 유클리드 거리(Euclidean distance)를 {@link WordStar} 객체로 계산합니다.
     *
     * @param star1 첫번째 별
     * @param star2 두번째 별
     * @return 해당 별의 유클리드 거리
     */
    public static double euclideanDistanceFromWordStar(WordStar star1, WordStar star2) {
        return euclideanDistanceFromLoc(star1.getRawX(), star1.getRawY(), star2.getRawX(), star2.getRawY());
    }

    /**
     * 두 별 사이의 유클리드 거리(Euclidean distance)를 실수(double)값으로 계산합니다.
     *
     * @param x1 첫번째 별의 X 좌표
     * @param y1 첫번째 별의 Y 좌표
     * @param x2 두번째 별의 X 좌표
     * @param y2 두번째 별의 Y 좌표
     * @return 해당 별의 유클리드 거리
     */
    public static double euclideanDistanceFromLoc(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

}
