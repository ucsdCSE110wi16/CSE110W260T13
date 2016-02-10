package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.CaldroidUtil;

/**
 * Created by nick on 2/1/16.
 */
public class ChangeableColor {

    // Maximum and minimum values for colors in RGB
    private static final int RGB_COMPONENT_MAX = 255;
    private static final int RGB_COMPONENT_MIN = 0;

    // Private data members representing the state of the object
    private int red;
    private int blue;
    private int green;
    private int deltaPercent;

    // For out of range checking
    private boolean redIsOver;
    private boolean blueIsOver;
    private boolean greenIsOver;
    private boolean redIsUnder;
    private boolean blueIsUnder;
    private boolean greenIsUnder;

    /**
     * ctor
     *
     * @param red the red component of the color
     * @param blue the blue component of the color
     * @param green the green component of the color
     * @param deltaPercent the percent by which the color will change
     */
    public ChangeableColor(int red, int blue, int green, int deltaPercent) {
        this.setRGB(red, blue, green);
        this.setDeltaPercent(deltaPercent);
    }

    /**
     * Generates a String represenation of the object
     *
     * @return the String representation
     */
    @Override
    public String toString() {

        return String.format("#%02x%02x%02x", this.red, this.green, this.blue)
                + " "
                + this.getDeltaPercent();

    }

    /**
     * Compares value equality
     *
     * @param o another ChangeableColor object
     * @return true iff the two are by-value equal
     */
    @Override
    public boolean equals(Object o) {

        if((o instanceof ChangeableColor)
                && (o.toString().equals(this.toString()))) {

            return true;

        } else {

            return false;

        }

    }

    /**
     * Generates the hashCode of the ChangeableColor
     *
     * @return the hashcode int
     */
    @Override
    public int hashCode() {

        return this.toString().hashCode();

    }

    /**
     * Generates integer representation of the color
     *
     * @return the integer representation
     */
    public int toInt() {
        return ((this.red&0x0ff)<<16)|((this.green&0x0ff)<<8)|(this.blue&0x0ff);
    }

    /**
     * Converts the ChangeableColor to hex color
     *
     * @return String hex representation of the object of the form 0xffrrggbb
     */
    public String toHex() {
        return String.format("0xff%02x%02x%02x", this.red, this.green, this.blue);
    }

    /**
     * Sets the RGB color of the object
     *
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     */
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

    /**
     * Gets the percentage for which colors will be lightened or darkened
     *
     * @return the percentage
     */
    public int getDeltaPercent() {
        return this.deltaPercent;
    }

    /**
     * Sets the percentage for which colors will be lightened or darkened
     *
     * @param deltaPercent the percentage between [0, 100]
     */
    public void setDeltaPercent(int deltaPercent) {
        this.deltaPercent = deltaPercent;
    }

    /**
     * Lightens the ChangeableColor by the previously set percentage
     *
     * @return the lightened ChangeableColor
     */
    public ChangeableColor lightenColorByDeltaPercent() {

        this.setRGB((int)(this.red + (RGB_COMPONENT_MAX * (getDeltaPercent() / 100.0))) + 1,
                (int)(this.green + (RGB_COMPONENT_MAX * (getDeltaPercent() / 100.0))) + 1,
                (int)(this.blue + (RGB_COMPONENT_MAX * (getDeltaPercent() / 100.0))) + 1);

        return this;
    }

    /**
     * Darkens the ChangeableColor by the previously set percentage
     *
     * @return the darkened ChangeableColor
     */
    public ChangeableColor darkenColorByDeltaPercent() {

        this.setRGB((int)(this.red - (RGB_COMPONENT_MAX * (getDeltaPercent() / 100.0))),
                (int)(this.green - (RGB_COMPONENT_MAX * (getDeltaPercent() / 100.0))),
                (int)(this.blue - (RGB_COMPONENT_MAX * (getDeltaPercent() / 100.0))));

        return this;
    }

    /**
     * Corrects values that are out of the normal [0, 255] color range
     */
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
