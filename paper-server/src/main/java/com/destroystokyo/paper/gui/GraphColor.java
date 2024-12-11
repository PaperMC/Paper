package com.destroystokyo.paper.gui;

import java.awt.Color;

public class GraphColor {
    private static final Color[] colorLine = new Color[101];
    private static final Color[] colorFill = new Color[101];

    static {
        for (int i = 0; i < 101; i++) {
            Color color = createColor(i);
            colorLine[i] = new Color(color.getRed() / 2, color.getGreen() / 2, color.getBlue() / 2, 255);
            colorFill[i] = new Color(colorLine[i].getRed(), colorLine[i].getGreen(), colorLine[i].getBlue(), 125);
        }
    }

    public static Color getLineColor(int percent) {
        return colorLine[percent];
    }

    public static Color getFillColor(int percent) {
        return colorFill[percent];
    }

    private static Color createColor(int percent) {
        if (percent <= 50) {
            return new Color(0X00FF00);
        }

        int value = 510 - (int) (Math.min(Math.max(0, ((percent - 50) / 50F)), 1) * 510);

        int red, green;
        if (value < 255) {
            red = 255;
            green = (int) (Math.sqrt(value) * 16);
        } else {
            green = 255;
            value = value - 255;
            red = 255 - (value * value / 255);
        }

        return new Color(red, green, 0);
    }
}
