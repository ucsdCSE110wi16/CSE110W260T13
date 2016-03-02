package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages.GradeScale;


public class Category {
    public String catID;
    public String courseName;
    public String category;
    public int weight;

    public Category(String id, String name, String category, int weight){
        this.catID = id;
        this.courseName = name;
        this.category = category;
        this.weight = weight;
    }

    public boolean equals(Category object){
        return catID.contentEquals(object.catID) &&
                courseName.contentEquals(object.courseName) &&
                category.contentEquals(object.category) &&
                weight==object.weight;
    }

    public boolean isNew(){
        return catID.isEmpty();
    }
}
