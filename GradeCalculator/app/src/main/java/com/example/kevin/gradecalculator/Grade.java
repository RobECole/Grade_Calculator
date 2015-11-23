package com.example.kevin.gradecalculator;

import java.io.Serializable;

/**
 * Created by Kevin on 11/7/2015.
 * Grade is the object for each grade in a course
 * This will store the mark of each grade, the name of the grade and the category type the grade
 * belongs to
 */
public class Grade implements Serializable {
    //instance variables
    //id is for database reference
    //name for the grade name
    //type for the grade type
    //mark for the number in a percentage
    private long id;
    private String name;
    private String type;
    private float mark;
    private long courseId;
    //Constructor
    public Grade(String name, String type, float mark, long courseId){
        this.name = name;
        this.type = type;
        this.mark = mark;
        this.courseId = courseId;
    }
    //Getters
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public float getMark(){
        return mark;
    }
    public long getId(){
        return id;
    }
    public long getCourseId(){
        return courseId;
    }
    //Setters
    public void setName(String name){
        this.name = name;
    }
    public void setType(String type){
        this.type =type;
    }
    public void setMark(int mark){
        this.mark = mark;
    }
    public void setId(long id){
        this.id = id;
    }
    public void setCourseId(long courseId){
        this.courseId = courseId;
    }
    //toString override
    public String toString(){
        return name + ":" + type + ":" + mark;
    }
}
