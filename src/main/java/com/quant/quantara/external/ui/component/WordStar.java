package com.quant.quantara.external.ui.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.quant.quantara.external.controller.ViewController;
import com.quant.quantara.external.util.SimpleEffectGenerator;
import com.quant.quantara.internal.GCEntityWrapper;
import com.quant.quantara.internal.Star;
import com.quant.quantara.internal.serializer.WordStarDeserializer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;
import java.util.Random;

/**
 * 사용자로부터 전달받은 프롬프트는 한 문장으로 이루어져 있으며,
 * 해당 문장을 이루는 모든 단어는 이 객체로 표현되고 프로젝트 내에서 별이라고 부릅니다.
 * 컨트롤러 {@link ViewController}를 통해 이 객체를 UI에 배치합니다.
 *
 * @author Q. T. Felix
 * @since 1.0.0
 */
@JsonDeserialize(using = WordStarDeserializer.class)
public class WordStar extends ImageView implements Star {

    private static final String STAR_ICON_PATH = "/icons/four_pointed_star.png";

    private GCEntityWrapper entity;

    private double rawX;

    private double rawY;

    public WordStar() {
        this.entity = new GCEntityWrapper();
        this.rawX = 0;
        this.rawY = 0;
    }

    /**
     * 임의 좌표값을 전달하여 객체를 생성합니다.
     *
     * @param entity 감정 분석 결과를 담은 엔티티
     * @param rawX   임의의 X 좌표값
     * @param rawY   임의의 Y 좌표값
     * @return 임의 값이 할당된 {@link WordStar}
     */
    public static WordStar of(GCEntityWrapper entity, double rawX, double rawY) {
        WordStar star = new WordStar();
        star.rawX = rawX;
        star.rawY = rawY;
        star.entity = entity;
        return star;
    }

    /**
     * 객체의 생성자입니다. 해당 생성자는 내부적으로 랜덤 위치값과
     * 아이콘, 크기를 할당합니다. 말인 즉슨 생성할 때 마다 이러한 랜덤 연산이
     * 수행되기 때문에 만들어진 별을 호출하려는 경우 역직렬화를 이용해야 합니다.
     * <p><strong>주의하세요! </strong> 해당 생성자의 파라미터는 엔티티와
     * 별 그룹이 생성되는 그룹의 최대 너비/높이를 받습니다. 임의 좌표값을 전달하려면
     * {@link #WordStar()} 또는 {@link #of(GCEntityWrapper, double, double)}
     * 메소드를 사용하세요.
     *
     * @param entity       감정 분석 결과를 담은 엔티티
     * @param borderWidth  별 그룹의 최대 너비
     * @param borderHeight 별 그룸의 최대 높이
     */
    public WordStar(GCEntityWrapper entity, double borderWidth, double borderHeight) {
        this.entity = entity;
        randomPosition(borderWidth, borderHeight);
        iconSetup();
    }

    @JsonIgnore
    private void randomPosition(double borderWidth, double borderHeight) {
        final Random random = new Random();

        double halfW = borderWidth / 2,
                halfH = borderHeight / 2;
        double rawX = random.nextDouble() * borderWidth - halfW, // 보더 범위 지정: -halfW ~ halfW
                rawY = entity.isPositive()
                        ? random.nextDouble() * halfH - halfH // 긍정: 상단(-halfH ~ 0)
                        : random.nextDouble() * halfH; // 부정: 하단(0 ~ halfH)

        this.rawX = rawX;
        this.rawY = rawY;

        // 디버그 메시지
        System.out.println("[Debug] WordStar(\"" + entity.getName() + "\") OffsetX / OffsetY: " + rawX + " / " + rawY);
    }

    /**
     * 별의 아이콘과 이벤트를 설정합니다.
     *
     * @return 할당된 후의 {@link WordStar}
     */
    @JsonIgnore
    public WordStar iconSetup() {
        Objects.requireNonNull(entity, "entity가 null입니다!");
        URL starIconPath = WordStar.class.getResource(STAR_ICON_PATH);
        if (starIconPath == null) {
            System.out.println("[Error] 리소스 \"" + STAR_ICON_PATH + "\" 를 찾을 수 없습니다!");
            return null;
        }
        Image starIconImage = new Image(starIconPath.toExternalForm());
        setImage(starIconImage);
        setPreserveRatio(true);
        // 명시적 크기 설정
        setFitWidth(24);
        setFitHeight(24);
        // UI 좌표값 = 명시된 아이콘 크기 / 2
        setX(rawX - 12);
        setY(rawY - 12);
        setTranslateZ(100);
        // 그림자 이펙트 설정
        setEffect(SimpleEffectGenerator.dropShadow(
                15, entity.isPositive() ? POSITIVE_COLOR : NEGATIVE_COLOR
        ));

        // 호버 이벤트(감정 분석 결과 보여짐)
        new WordStarHover(this).initLogic();

        return this;
    }

    @Override
    public GCEntityWrapper getEntity() {
        return entity;
    }

    @Override
    public void setEntity(GCEntityWrapper entity) {
        this.entity = entity;
    }

    @Override
    public double getRawX() {
        return rawX;
    }

    @Override
    public void setRawX(double rawX) {
        this.rawX = rawX;
    }

    @Override
    public double getRawY() {
        return rawY;
    }

    @Override
    public void setRawY(double rawY) {
        this.rawY = rawY;
    }

    @Override
    public String toString() {
        return "WordStar{" +
                "entity=" + entity +
                ", rawX=" + rawX +
                ", rawY=" + rawY +
                '}';
    }
}
