package com.quant.quantara.internal;

import com.quant.quantara.external.ui.component.WordStar;
import javafx.scene.paint.Color;

/**
 * 별자리에 소속되는 별 객체를 생성하기 위한 인터페이스입니다.
 * {@code FXUI} 객체로 사용하기 위한 별의 위치 오프셋과 해당 별이
 * 긍정적 별인지, 부정적 별인지에 대한 메소드를 제공합니다.
 *
 * @author Q. T. Felix
 * @since 1.0.0
 */
public interface Star {

    Color POSITIVE_COLOR = Color.web("#64C9F5");
    Color NEGATIVE_COLOR = Color.web("#F43182");

    GCEntityWrapper getEntity();

    void setEntity(GCEntityWrapper entity);

    /**
     * UI 상에 보여지기 전에 객체에 저장하는 X좌표값입니다.
     * 별의 크기는 {@code 24x24}이기 때문에 이 값에
     * {@code -12}된 값이 UI에 보여집니다.
     * <p>이 값은 {@link WordStar} 객체의 인스턴스를 생성할 때
     * 랜덤 값으로 정의됩니다.
     *
     * @return 실수 X좌표값
     */
    double getRawX();

    /**
     * UI 상에 보여지기 전에 객체에 저장하는 X좌표값을 설정합니다.
     *
     * @param x 실수 X좌표값
     */
    void setRawX(double x);

    /**
     * UI 상에 보여지기 전에 객체에 저장하는 Y좌표값입니다.
     * 별의 크기는 {@code 24x24}이기 때문에 이 값에
     * {@code -12}된 값이 UI에 보여집니다.
     * <p>이 값은 {@link WordStar} 객체의 인스턴스를 생성할 때
     * 랜덤 값으로 정의됩니다.
     *
     * @return 실수 Y좌표값
     */
    double getRawY();

    /**
     * UI 상에 보여지기 전에 객체에 저장하는 Y좌표값을 설정합니다.
     *
     * @param y 실수 Y좌표값
     */
    void setRawY(double y);

}
