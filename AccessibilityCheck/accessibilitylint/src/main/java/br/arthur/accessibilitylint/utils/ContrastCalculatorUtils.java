package br.arthur.accessibilitylint.utils;

import java.awt.Color;

/**
 * Created by arthu on 17/01/2019.
 */

public class ContrastCalculatorUtils {

    private static final double RED_FACTOR = 0.2126;
    /* */
    private static final double GREEN_FACTOR = 0.7152;
    /* */
    private static final double BLUE_FACTOR = 0.0722;
    /* */
    private static final double CONTRAST_FACTOR = 0.05;
    /* */
    private static final double RGB_MAX_VALUE = 255;
    /* */
    private static final double RSGB_FACTOR = 0.03928;
    /* */
    private static final double LUMINANCE_INF = 12.92;
    /* */
    private static final double LUMINANCE_SUP_CONST = 0.055;
    /* */
    private static final double LUMINANCE_SUP_CONST2 = 1.055;
    /* */
    private static final double LUMINANCE_EXP = 2.4;
    /* */
    private static final int ROUND_VALUE = 100000;

    public static double getConstrastRatio(String textColor, String backgroundColor) {
        Color fgColor = Color.decode(textColor);
        Color bgColor = Color.decode(backgroundColor);
        return getConstrastRatio(fgColor, bgColor);
    }

    /**
     * This method computes the contrast ratio between 2 colors. It needs to
     * determine which one is lighter first.
     *
     * @param fgColor
     * @param bgColor
     * @return the contrast ratio between the 2 colors
     */
    public static double getConstrastRatio(final Color fgColor, final Color bgColor) {
        double fgLuminosity = getLuminosity(fgColor);
        double bgLuminosity = getLuminosity(bgColor);
        if (fgLuminosity > bgLuminosity) {
            return computeContrast(fgLuminosity, bgLuminosity);
        } else {
            return computeContrast(bgLuminosity, fgLuminosity);
        }
    }


    /**
     *
     * @param lighter
     * @param darker
     * @return
     */
    private static double computeContrast(double lighter, double darker) {
        return (Double.valueOf(((lighter + CONTRAST_FACTOR) / (darker + CONTRAST_FACTOR))));
    }

    /**
     *
     * @param color
     * @return
     */
    public static double getLuminosity(Color color) {
        double luminosity =
                getComposantValue(color.getRed()) * RED_FACTOR
                        + getComposantValue(color.getGreen()) * GREEN_FACTOR
                        + getComposantValue(color.getBlue()) * BLUE_FACTOR;

        return luminosity;
    }

    /**
     *
     * @param composant
     * @return
     */
    private static double getComposantValue(double composant) {
        double rsgb = composant / RGB_MAX_VALUE;
        if (rsgb <= RSGB_FACTOR) {
            return rsgb / LUMINANCE_INF;
        } else {
            return Math.pow(((rsgb + LUMINANCE_SUP_CONST) / LUMINANCE_SUP_CONST2), LUMINANCE_EXP);
        }
    }
}
