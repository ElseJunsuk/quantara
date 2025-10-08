package com.quant.quantara.internal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.Sentiment;
import org.redfx.strange.Complex;

/**
 * {@code Google Cloud API}의 객체 {@link Entity}를 래핑하기 위한 클래스입니다.
 * 크게 개별 항목의 이름과 유형, 감정 분석, 가시도로 나뉩니다.
 * <p>해당 클래스는 엔티티 객체로의 역직렬화를 목적으로 사용하지 않고,
 * 별자리 기록을 보관하기 위해 사용됩니다.
 *
 * @author Q. T. Felix
 * @since 1.0.0
 */
public class GCEntityWrapper {

    private String name;
    private String type;
    private float sentimentScore;
    private float sentimentMagnitude;
    private float salienceScore;

    public GCEntityWrapper() {
    }

    public GCEntityWrapper(final Entity entity) {
        this(entity.getName(), entity.getType(), entity.getSentiment(), entity.getSalience());
    }

    public GCEntityWrapper(String name, Entity.Type type, Sentiment sentiment, float salienceScore) {
        this.name = name;
        this.type = type.name();
        this.sentimentScore = sentiment.getScore();
        this.sentimentMagnitude = sentiment.getMagnitude();
        this.salienceScore = salienceScore;
    }

    public GCEntityWrapper(String name, String type, float sentimentScore, float sentimentMagnitude, float salienceScore) {
        this.name = name;
        this.type = type;
        this.sentimentScore = sentimentScore;
        this.sentimentMagnitude = sentimentMagnitude;
        this.salienceScore = salienceScore;
    }

    /**
     * 감정 점수({@link Sentiment#getScore()})가 0 이상인지 체크합니다.
     * 0을 명확히 표현하기 위해 {@link Complex#ZERO}의 실수를 불러옵니다.
     *
     * @return 감정 점수가 0 이상인 경우 true, 그렇지 않으면 false
     */
    @JsonIgnore
    public boolean isPositive() {
        return this.sentimentScore > Complex.ZERO.r;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(float sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public float getSentimentMagnitude() {
        return sentimentMagnitude;
    }

    public void setSentimentMagnitude(float sentimentMagnitude) {
        this.sentimentMagnitude = sentimentMagnitude;
    }

    public float getSalienceScore() {
        return salienceScore;
    }

    public void setSalienceScore(float salienceScore) {
        this.salienceScore = salienceScore;
    }

    @Override
    public String toString() {
        return "\nEntity: " + name + "\n" +
                "Type: " + type + "\n" +
                "Sentiment Score: " + sentimentScore + "\n" +
                "Sentiment Magnitude: " + sentimentMagnitude + "\n" +
                "Salience Score: " + salienceScore + "\n";
    }

}
