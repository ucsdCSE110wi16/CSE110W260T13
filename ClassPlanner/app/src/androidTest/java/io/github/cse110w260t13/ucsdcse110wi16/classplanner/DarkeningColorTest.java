package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

import junit.framework.*;
import org.junit.Test;
import org.junit.Ignore;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.util.DarkeningColor;

import static org.junit.Assert.assertEquals;

/**
 * Created by nick on 2/1/16.
 */
public class DarkeningColorTest extends TestCase {

    private DarkeningColor darkeningColor;

    // assigning value to darkeningColor
    protected void setUp(){
        darkeningColor = new DarkeningColor(0, 0, 0, 5);
    }

    @Test
    public void testDarkenColor() {
        
    }

}
