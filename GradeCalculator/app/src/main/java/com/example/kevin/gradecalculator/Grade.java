package com.example.kevin.gradecalculator;

/**
 * Created by Kevin on 11/7/2015.
 * Grade is the object for each grade in a course
 * This will store the mark of each grade, the name of the grade and the category type the grade
 * belongs to
 */
public class Grade {
    //instance variables
    //name for the grade name
    //type for the grade type
    //mark for the number in a percentage
    private String name;
    private String type;
    private float mark;
    //Constructors
    public Grade(){
        name = "";
        type = "";
        mark = 0;
    }
    public Grade(String name, String type, int mark){
        this.name = name;
        this.type = type;
        this.mark = mark;
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
    //toString override
    public String toString(){
        return name + ":" + type + ":" + mark;
    }
}
