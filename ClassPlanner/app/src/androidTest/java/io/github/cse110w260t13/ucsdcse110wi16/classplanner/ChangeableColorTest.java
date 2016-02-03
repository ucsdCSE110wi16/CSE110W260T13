package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

import android.util.Log;

import junit.framework.*;
import org.junit.Test;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.util.ChangeableColor;

/**
 * Created by nick on 2/1/16.
 */
public class ChangeableColorTest extends TestCase {

    private String logTag = "DarkeningColorTest";
    private ChangeableColor darkeningColor;

    // assigning value to darkeningColor
    protected void setUp(){
        darkeningColor = new ChangeableColor(0, 0, 0, 5);
    }

    @Test
    public void testDarkenColorAtAllMin() {
        darkeningColor.setRGB(0, 0, 0);
        assertEquals(darkeningColor, darkeningColor.darkenColorByDeltaPercent());

        Log.d(logTag, "darken all min: " + darkeningColor);
    }

    @Test
    public void testLightenColorAtAllMax() {
        darkeningColor.setRGB(255, 255, 255);
        assertEquals(darkeningColor, darkeningColor.lightenColorByDeltaPercent());

        Log.d(logTag, "lighten all max: " + darkeningColor);
    }

    @Test
     public void testLightenColorByFivePercent() {
        String expected = "#5ec6e8 5";

        darkeningColor.setRGB(81, 185, 219);
        darkeningColor.setDeltaPercent(5);
        assertEquals(expected, darkeningColor.lightenColorByDeltaPercent().toString());

        Log.d(logTag, "lightened color by five percent: " + darkeningColor.lightenColorByDeltaPercent());
    }

    @Test
    public void testDarkenColorByFivePercent() {
        String expected = "#44acce 5";

        darkeningColor.setRGB(81, 185, 219);
        darkeningColor.setDeltaPercent(5);
        assertEquals(expected, darkeningColor.darkenColorByDeltaPercent().toString());

        Log.d(logTag, "lightened color by five percent: " + darkeningColor.darkenColorByDeltaPercent());
    }

    @Test
    public void testLightenColorByTenPercent() {
        String expected = "#6bd3f5 10";

        darkeningColor.setRGB(81, 185, 219);
        darkeningColor.setDeltaPercent(10);
        assertEquals(expected, darkeningColor.lightenColorByDeltaPercent().toString());

        Log.d(logTag, "lightened color by ten percent: " + darkeningColor.lightenColorByDeltaPercent());
    }

    @Test
    public void testDarkenColorByTenPercent() {
        String expected = "#379fc1 10";

        darkeningColor.setRGB(81, 185, 219);
        darkeningColor.setDeltaPercent(10);
        assertEquals(expected, darkeningColor.darkenColorByDeltaPercent().toString());

        Log.d(logTag, "lightened color by ten percent: " + darkeningColor.darkenColorByDeltaPercent());
    }

    @Test
    public void testInverseColorByPercent() {

        for(int i = 0; i > 256; i++) {

            String expected = "#51b9db " + i;

            darkeningColor.setRGB(81, 185, 219);
            darkeningColor.setDeltaPercent(i);

            assertEquals(
                    expected,
                    darkeningColor
                            .darkenColorByDeltaPercent()
                            .lightenColorByDeltaPercent()
                            .toString()
            );

            Log.d(
                    logTag,
                    "original and doubly inverted: "
                            + expected
                            + ", "
                            + darkeningColor
                            .darkenColorByDeltaPercent()
                            .lightenColorByDeltaPercent()
            );

        }
    }

}
