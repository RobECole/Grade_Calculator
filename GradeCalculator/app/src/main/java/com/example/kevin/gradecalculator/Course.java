package com.example.kevin.gradecalculator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kevin on 11/7/2015.
 * Course keeps track of the courses for the user. Each semester will have a list of courses.
 * Courses will store the list of grades and a map of category type to percentage
 */
public class Course implements Serializable{
    //instance variables
    //name is the name of the course
    //grades is list of grades for the course
    //categoryDistribution is the map of the category type to percentage
    //mark is the total current mark in the course after applying distribution
    private String name;
    private List<Grade> grades;
    private Map<String,Float> categoryDistribution;
    private float mark;
    private long id;
    //constructors
    public Course() {
        this.grades = new ArrayList<>();
        this.categoryDistribution = new HashMap<>();
        this.name = "";
        this.mark = 0;
    }
    public Course(ArrayList<Grade> grades,
                  HashMap<String, Float> categoryDistribution,
                  String name,
                  int mark){
        this.grades = grades;
        this.categoryDistribution = categoryDistribution;
        this.name = name;
        this.mark = mark;
    }
    public Course(
                  String name,
                  float mark){
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
    public Map<String, Float> getCategoryDistribution(){
        return categoryDistribution;
    }
    public List<String> getDistributionNames(){
        List<String> s = new ArrayList<>();
        s.addAll(this.categoryDistribution.keySet());
        return s;
    }
    public String getName(){
        return name;
    }
    public float getMark(){
        return mark;
    }
    public long getId(){
        return id;
    }
    //Setters
    public void setGrades(List<Grade> grades){
        this.grades = grades;
    }
    public void setCategoryDistribution(Map<String, Float> categoryDistribution){
        this.categoryDistribution = categoryDistribution;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setMark(float mark){
        this.mark = mark;
    }
    public void setId(long id){
        this.id = id;
    }
    //Adders
    public void addGrade(Grade grade){
        this.grades.add(grade);
    }
    public void addCategoryDistribution(String category, float distribution){
        this.categoryDistribution.put(category,distribution);
    }
    public void addCategoryDistribution(Map<String, Float> categoryDistribution){
        for (Map.Entry<String, Float> entry : categoryDistribution.entrySet())
        {
            this.categoryDistribution.put(entry.getKey(), entry.getValue());
        }
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
            this.categoryDistribution.remove(category);
        }
    }
    //Updaters
    public void updateCategoryName(String category){
        if(this.categoryDistribution.containsKey(category)){
            float distribution = this.categoryDistribution.get(category);
            this.categoryDistribution.remove(category);
            addCategoryDistribution(category, distribution);
        }
    }
    public void updateDistribution(String category, Float distribution){
        deleteCategory(category);
        addCategoryDistribution(category, distribution);
    }
    //TODO updateMark
    public void updateMark(){
        float mark = 0;
        //match type == category name
        //multiply totals in each category with distribution percentage
        //add all results to a total
        setMark(mark);
    }

    @Override
    public String toString(){
        String Course = name;
        return Course;
    }

}
