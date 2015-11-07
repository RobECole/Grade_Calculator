package com.example.kevin.gradecalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kevin on 11/7/2015.
 * Course keeps track of the courses for the user. Each semester will have a list of courses.
 * Courses will store the list of grades and a map of category type to percentage
 */
public class Course {
    //instance variables
    //name is the name of the course
    //grades is list of grades for the course
    //categoryDistribution is the map of the category type to percentage
    //mark is the total current mark in the course after applying distribution
    private String name;
    private List<Grade> grades;
    private Map<String,Integer> categoryDistribution;
    private float mark;
    //constructors
    public Course(){
        this.grades = new ArrayList<>();
        this.categoryDistribution = new HashMap<>();
        this.name = "";
        this.mark = 0;
    }
    public Course(ArrayList<Grade> grades,
                  HashMap<String, Integer> categoryDistribution,
                  String name,
                  int mark){
        this.grades = grades;
        this.categoryDistribution = categoryDistribution;
        this.name = name;
        this.mark = mark;
    }
    public Course(
                  String name,
                  int mark){
        this.grades = new ArrayList<>();
        this.categoryDistribution = new HashMap<>();
        this.name = name;
        this.mark = mark;
    }
    public Course(
            String name){
        this.grades = new ArrayList<>();
        this.categoryDistribution = new HashMap<>();
        this.name = name;
        this.mark = 0;
    }
    //Getters
    public List<Grade> getGrades(){
        return grades;
    }
    public Map<String, Integer> getCategoryDistribution(){
        return categoryDistribution;
    }
    public String getName(){
        return name;
    }
    public float getMark(){
        return mark;
    }
    //Setters
    public void setGrades(ArrayList<Grade> grades){
        this.grades = grades;
    }
    public void setCategoryDistribution(HashMap<String, Integer> categoryDistribution){
        this.categoryDistribution = categoryDistribution;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setMark(int mark){
        this.mark = mark;
    }
    //Adders
    public void addGrade(Grade grade){
        this.grades.add(grade);
    }
    public void addCategoryDistribution(String category, int distribution){
        this.categoryDistribution.put(category, distribution);
    }
    //Deleters
    public void deleteGrade(Grade grade){
        int counter = 0;
        for(Grade currGrade : grades){
            if(currGrade.getMark() == grade.getMark()
                    && currGrade.getName().equals(grade.getName())
                    && currGrade.getType().equals(grade.getType())){
                grades.remove(counter);
                break;
            }
            counter++;
        }
    }
    public void deleteCategory(String category){
        if(this.categoryDistribution.containsKey(category)){
            int distribution = this.categoryDistribution.get(category);
            this.categoryDistribution.remove(category);
            addCategoryDistribution(category,distribution);
        }
    }
    //Updaters
    //TODO updateGrade

    //TODO updateCategory

    //TODO updateDistribution

    //TODO updateMark

}
