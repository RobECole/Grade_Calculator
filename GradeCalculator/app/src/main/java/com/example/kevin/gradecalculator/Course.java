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
    //grades is list of grades for the course
    //categoryDistribution is the map of the category type to percentage
    private List<Grade> grades;
    private Map<String,Integer> categoryDistribution;
    //constructors
    public Course(){
        grades = new ArrayList<>();
        categoryDistribution = new HashMap<>();
    }
    //Getters
    public List<Grade> getGrades(){
        return grades;
    }
    public Map<String, Integer> getCategoryDistribution(){
        return categoryDistribution;
    }
    //Setters
    public void setGrades(ArrayList<Grade> grades){
        this.grades = grades;
    }
    public void setCategoryDistribution(HashMap<String, Integer> categoryDistribution){
        this.categoryDistribution = categoryDistribution;
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
}
