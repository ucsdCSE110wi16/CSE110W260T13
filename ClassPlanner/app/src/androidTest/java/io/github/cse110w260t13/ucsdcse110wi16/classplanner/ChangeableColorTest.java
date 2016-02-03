package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

import android.util.Log;

import junit.framework.*;
import org.junit.Test;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.util.ChangeableColor;

/**
 * Created by nick on 2/1/16.
 */
public class ChangeableColorTest extends TestCase {

    private String logTag = "ChangeableColorTest";
    private ChangeableColor changeableColor;

    // assigning value to darkeningColor
    protected void setUp(){
        changeableColor = new ChangeableColor(0, 0, 0, 5);
    }

    @Test
    public void testToInt() {
        int expected = 5355995;
        changeableColor.setRGB(81, 185, 219);
        assertEquals(expected, changeableColor.toInt());
        Log.d(logTag, "Integer representation of color: " + changeableColor.toInt());
    }

    @Test
    public void testToHex() {
        String expected = "ff51b9db";
        changeableColor.setRGB(81, 185, 219);
        assertEquals(expected, changeableColor.toHex());
        Log.d(logTag, "Integer representation of color: " + changeableColor.toInt());
    }

    @Test
    public void testDarkenColorAtAllMin() {
        changeableColor.setRGB(0, 0, 0);
        assertEquals(changeableColor, changeableColor.darkenColorByDeltaPercent());

        Log.d(logTag, "darken all min: " + changeableColor);
    }

    @Test
    public void testLightenColorAtAllMax() {
        changeableColor.setRGB(255, 255, 255);
        assertEquals(changeableColor, changeableColor.lightenColorByDeltaPercent());

        Log.d(logTag, "lighten all max: " + changeableColor);
    }

    @Test
     public void testLightenColorByFivePercent() {
        String expected = "#5ec6e8 5";

        changeableColor.setRGB(81, 185, 219);
        changeableColor.setDeltaPercent(5);
        assertEquals(expected, changeableColor.lightenColorByDeltaPercent().toString());

        Log.d(logTag, "lightened color by five percent: " + changeableColor.lightenColorByDeltaPercent());
    }

    @Test
    public void testDarkenColorByFivePercent() {
        String expected = "#44acce 5";

        changeableColor.setRGB(81, 185, 219);
        changeableColor.setDeltaPercent(5);
        assertEquals(expected, changeableColor.darkenColorByDeltaPercent().toString());

        Log.d(logTag, "lightened color by five percent: " + changeableColor.darkenColorByDeltaPercent());
    }

    @Test
    public void testLightenColorByTenPercent() {
        String expected = "#6bd3f5 10";

        changeableColor.setRGB(81, 185, 219);
        changeableColor.setDeltaPercent(10);
        assertEquals(expected, changeableColor.lightenColorByDeltaPercent().toString());

        Log.d(logTag, "lightened color by ten percent: " + changeableColor.lightenColorByDeltaPercent());
    }

    @Test
    public void testDarkenColorByTenPercent() {
        String expected = "#379fc1 10";

        changeableColor.setRGB(81, 185, 219);
        changeableColor.setDeltaPercent(10);
        assertEquals(expected, changeableColor.darkenColorByDeltaPercent().toString());

        Log.d(logTag, "lightened color by ten percent: " + changeableColor.darkenColorByDeltaPercent());
    }

    @Test
    public void testInverseColorByPercent() {

        for(int i = 0; i > 256; i++) {

            String expected = "#51b9db " + i;

            changeableColor.setRGB(81, 185, 219);
            changeableColor.setDeltaPercent(i);

            assertEquals(
                    expected,
                    changeableColor
                            .darkenColorByDeltaPercent()
                            .lightenColorByDeltaPercent()
                            .toString()
            );

            Log.d(
                    logTag,
                    "original and doubly inverted: "
                            + expected
                            + ", "
                            + changeableColor
                            .darkenColorByDeltaPercent()
                            .lightenColorByDeltaPercent()
            );

        }

    }

}
