package com.example.kevin.gradecalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 11/7/2015.
 * Data for semester will be stored in text files for each semester using their term and year as
 * the unique file name
 */
public class Semester {
    //instance variables
    //year stores the year of that semester
    //term stores the term (Fall/Winter/Summer)
    //courses stores the list of courses for that term (ids and names)
    private int year;
    private String term;
    private List<Course> courses;
    //Constructor
    public Semester(String term, int year){
        this.term = term;
        this.year = year;
    }
    //Getters
    public String getTerm(){
        return term;
    }
    public int getYear(){
        return year;
    }
    public List<Course> getCourses(){
        return courses;
    }
    //Setters
    public void setTerm(String term){
        this.term = term;
    }
    public void setYear(int year){
        this.year = year;
    }
    public void setCourses(ArrayList<Course> courses){
        this.courses = courses;
    }
    //Adders
    public void addCourse(Course course){
        courses.add(course);
    }
    //Deleters
    public void removeCourse(Course course){
        int counter = 0;
        for(Course currCourse : courses){
            if(currCourse.getName().equals(course.getName())){
                courses.remove(counter);
                break;
            }
            counter++;
        }
    }
}
