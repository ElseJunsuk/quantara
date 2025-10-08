package com.quant.quantara.external.util;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;

public final class SimpleEffectGenerator {

    public static DropShadow dropShadow(double radius, Color color) {
        return dropShadow(radius, color, 0, 0);
    }

    public static DropShadow dropShadow(double radius, Color color, double offsetX, double offsetY) {
        return dropShadow(radius, color, offsetX, offsetY, BlurType.GAUSSIAN);
    }

    public static DropShadow dropShadow(double radius, Color color, double offsetX, double offsetY, BlurType blurType) {
        final DropShadow dropShadow = new DropShadow(radius, color);
        dropShadow.setOffsetX(offsetX);
        dropShadow.setOffsetY(offsetY);
        dropShadow.setBlurType(blurType);
        return dropShadow;
    }

    public static GaussianBlur gaussianBlur() {
        return gaussianBlur(10);
    }

    public static GaussianBlur gaussianBlur(double radius) {
        return new GaussianBlur(radius);
    }

}
