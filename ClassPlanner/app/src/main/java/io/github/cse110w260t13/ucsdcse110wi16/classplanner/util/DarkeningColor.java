package io.github.cse110w260t13.ucsdcse110wi16.classplanner.util;

/**
 * Created by nick on 2/1/16.
 */
public class DarkeningColor {

    private static final int RGB_COMPONENT_MAX = 255;
    private static final int RGB_COMPONENT_MIN = 0;

    private int red;
    private int blue;
    private int green;
    private int deltaPercent;

    private boolean redIsOver;
    private boolean blueIsOver;
    private boolean greenIsOver;

    private boolean redIsUnder;
    private boolean blueIsUnder;
    private boolean greenIsUnder;

    public DarkeningColor(int red, int blue, int green, int deltaPercent) {
        this.setRGB(red, blue, green);
        this.setDeltaPercent(deltaPercent);
    }

    public String getHexColor() {
        return String.format("#%02x%02x%02x", red, blue, green);
    }

    public void setRGB(int red, int blue, int green) {
        if(red < RGB_COMPONENT_MIN) {
            redIsUnder = true;
        }

        if(red > RGB_COMPONENT_MAX) {
            redIsOver = true;
        }

        if(blue < RGB_COMPONENT_MIN) {
            blueIsUnder = true;
        }

        if(blue > RGB_COMPONENT_MAX) {
            blueIsOver = true;
        }

        if(green < RGB_COMPONENT_MIN) {
            greenIsUnder = true;
        }

        if(green > RGB_COMPONENT_MAX) {
            greenIsOver = true;
        }

        if(redIsUnder || redIsOver
                || blueIsUnder || blueIsOver
                || greenIsUnder || greenIsOver) {

            this.correctOutOfRange();

        } else {

            this.red = red;
            this.blue = blue;
            this.green = green;

        }
    }

    public int getDeltaPercent() {
        return this.deltaPercent;
    }

    public void setDeltaPercent(int deltaPercent) {
        this.deltaPercent = deltaPercent;
    }

    public String brightenColorByDeltaPercent() {

        this.setRGB(this.red + (this.red * (100 - deltaPercent)),
                this.green + (this.green * (100 - deltaPercent)),
                this.blue + (this.blue * (100 - deltaPercent)));

        return this.getHexColor();
    }

    public String darkenColorByDeltaPercent() {

        this.setRGB(this.red - (this.red * (100 - deltaPercent)),
                this.green - (this.green * (100 - deltaPercent)),
                this.blue - (this.blue * (100 - deltaPercent)));

        return this.getHexColor();
    }

    private void correctOutOfRange() {

        if(redIsUnder) {
            this.red = RGB_COMPONENT_MIN;
        }

        if(redIsOver) {
            this.red = RGB_COMPONENT_MAX;
        }

        if(blueIsUnder) {
            this.blue = RGB_COMPONENT_MIN;
        }

        if(blueIsOver) {
            this.blue = RGB_COMPONENT_MAX;
        }

        if(greenIsUnder) {
            this.green = RGB_COMPONENT_MIN;
        }

        if(greenIsOver) {
            this.green = RGB_COMPONENT_MAX;
        }

    }

}
