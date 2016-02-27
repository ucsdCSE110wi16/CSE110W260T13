package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

public class Pair {
    private final String key;
    private final float value;

    public Pair(String aKey, float aValue)
    {
        key = aKey;
        value = aValue;
    }

    public String key()   { return key; }
    public float value() { return value; }
}
