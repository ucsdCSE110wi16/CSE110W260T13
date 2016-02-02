package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

import android.util.Log;

import junit.framework.*;
import org.junit.Test;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.util.DarkeningColor;

/**
 * Created by nick on 2/1/16.
 */
public class DarkeningColorTest extends TestCase {

    private String logTag = "DarkeningColorTest";
    private DarkeningColor darkeningColor;

    // assigning value to darkeningColor
    protected void setUp(){
        darkeningColor = new DarkeningColor(0, 0, 0, 5);
    }

    @Test
    public void testDarkenColorAtAllMin() {
        darkeningColor.setRGB(0, 0, 0);
        assertEquals(darkeningColor.getHexColor(), darkeningColor.darkenColorByDeltaPercent());

        Log.d(logTag, "darken all min: " + darkeningColor.getHexColor());
    }

    @Test
    public void testLightenColorAtAllMax() {
        darkeningColor.setRGB(255, 255, 255);
        assertEquals(darkeningColor.getHexColor(), darkeningColor.lightenColorByDeltaPercent());

        Log.d(logTag, "lighten all max: " + darkeningColor.getHexColor());
    }

    @Test
     public void testLightenColorByFivePercent() {
        String expected = "#5ec6e8";

        darkeningColor.setRGB(81, 185, 219);
        darkeningColor.setDeltaPercent(5);
        assertEquals(expected, darkeningColor.lightenColorByDeltaPercent());

        Log.d(logTag, "lightened color by five percent: " + darkeningColor.lightenColorByDeltaPercent());
    }

    @Test
    public void testDarkenColorByFivePercent() {
        String expected = "#44acce";

        darkeningColor.setRGB(81, 185, 219);
        darkeningColor.setDeltaPercent(5);
        assertEquals(expected, darkeningColor.darkenColorByDeltaPercent());

        Log.d(logTag, "lightened color by five percent: " + darkeningColor.darkenColorByDeltaPercent());
    }

    @Test
    public void testLightenColorByTenPercent() {
        String expected = "#6bd3f5";

        darkeningColor.setRGB(81, 185, 219);
        darkeningColor.setDeltaPercent(10);
        assertEquals(expected, darkeningColor.lightenColorByDeltaPercent());

        Log.d(logTag, "lightened color by ten percent: " + darkeningColor.lightenColorByDeltaPercent());
    }

    @Test
    public void testDarkenColorByTenPercent() {
        String expected = "#379fc1";

        darkeningColor.setRGB(81, 185, 219);
        darkeningColor.setDeltaPercent(10);
        assertEquals(expected, darkeningColor.darkenColorByDeltaPercent());

        Log.d(logTag, "lightened color by ten percent: " + darkeningColor.darkenColorByDeltaPercent());
    }

}
