package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil;

import java.util.ArrayList;
import java.util.Arrays;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.Pair;

public class Scale {

    public static final boolean CUSTOM = false;
    public static final boolean DEFAULT = true;

    private ArrayList<Pair> scale;

    public Scale( boolean defaultValue ){
        scale = new ArrayList<Pair>();
        if(defaultValue) {
            scale.addAll(Arrays.asList(
                    new Pair("Homework", 10),
                    new Pair("Midterms", 25),
                    new Pair("Projects", 15),
                    new Pair("Final", 45),
                    new Pair("Participation", 5)
            ));
        }
    }

    public Scale( ArrayList<Pair> scale ){
        this.scale = scale;
    }

    public boolean add(String category, float weight){
        for( Pair entry : scale ){
            if (category.contentEquals(entry.key()))
                return false;
        }

        scale.add(new Pair(category, weight));
        return true;
    }

    public boolean deleteCategory(String category){
        for ( int i=0; i < scale.size(); i++){
            if (category.contentEquals(scale.get(i).key())) {
                scale.remove(i);
                return true;
            }
        }
        return false;
    }

    public void deleteAll(){
        scale.clear();
    }

    public String getCategory(int i) {
        try {
            return scale.get(i).key();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public float getWeight(int i){
        try {
            return scale.get(i).value();
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    public int size(){
        return scale.size();
    }
}
