package io.github.cse110w260t13.ucsdcse110wi16.classplanner.util;

/**
 * Created by nick on 2/1/16.
 */
public class ChangeableColor {

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

    public ChangeableColor(int red, int blue, int green, int deltaPercent) {
        this.setRGB(red, blue, green);
        this.setDeltaPercent(deltaPercent);
    }

    @Override
    public String toString() {

        return String.format("#%02x%02x%02x", this.red, this.green, this.blue)
                + " "
                + this.getDeltaPercent();

    }

    @Override
    public boolean equals(Object o) {

        if((o instanceof ChangeableColor)
                && (o.toString().equals(this.toString()))) {

            return true;

        } else {

            return false;

        }

    }

    @Override
    public int hashCode() {

        return this.toString().hashCode();

    }

    public int toInt() {
        return ((this.red&0x0ff)<<16)|((this.green&0x0ff)<<8)|(this.blue&0x0ff);
    }

    public String toHex() {
        return String.format("0xff%02x%02x%02x", this.red, this.green, this.blue);
    }

    public void setRGB(int red, int green, int blue) {
        if(red < RGB_COMPONENT_MIN) {
            redIsUnder = true;
        }

        if(red > RGB_COMPONENT_MAX) {
            redIsOver = true;
        }

        if(green < RGB_COMPONENT_MIN) {
            greenIsUnder = true;
        }

        if(green > RGB_COMPONENT_MAX) {
            greenIsOver = true;
        }

        if(blue < RGB_COMPONENT_MIN) {
            blueIsUnder = true;
        }

        if(blue > RGB_COMPONENT_MAX) {
            blueIsOver = true;
        }

        if(redIsUnder || redIsOver
                || greenIsUnder || greenIsOver
                || blueIsUnder || blueIsOver) {

            this.correctOutOfRange();

        } else {

            this.red = red;
            this.green = green;
            this.blue = blue;

        }
    }

    public int getDeltaPercent() {
        return this.deltaPercent;
    }

    public void setDeltaPercent(int deltaPercent) {
        this.deltaPercent = deltaPercent;
    }

    public ChangeableColor lightenColorByDeltaPercent() {

        this.setRGB((int)(this.red + (RGB_COMPONENT_MAX * (getDeltaPercent() / 100.0))) + 1,
                (int)(this.green + (RGB_COMPONENT_MAX * (getDeltaPercent() / 100.0))) + 1,
                (int)(this.blue + (RGB_COMPONENT_MAX * (getDeltaPercent() / 100.0))) + 1);

        return this;
    }

    public ChangeableColor darkenColorByDeltaPercent() {

        this.setRGB((int)(this.red - (RGB_COMPONENT_MAX * (getDeltaPercent() / 100.0))),
                (int)(this.green - (RGB_COMPONENT_MAX * (getDeltaPercent() / 100.0))),
                (int)(this.blue - (RGB_COMPONENT_MAX * (getDeltaPercent() / 100.0))));

        return this;
    }

    private void correctOutOfRange() {

        if(redIsUnder) {
            this.red = RGB_COMPONENT_MIN;
        }

        if(redIsOver) {
            this.red = RGB_COMPONENT_MAX;
        }

        if(greenIsUnder) {
            this.green = RGB_COMPONENT_MIN;
        }

        if(greenIsOver) {
            this.green = RGB_COMPONENT_MAX;
        }

        if(blueIsUnder) {
            this.blue = RGB_COMPONENT_MIN;
        }

        if(blueIsOver) {
            this.blue = RGB_COMPONENT_MAX;
        }

    }

}
