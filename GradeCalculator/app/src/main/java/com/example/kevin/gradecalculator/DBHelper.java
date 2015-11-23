package com.example.kevin.gradecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kevin on 11/7/2015.
 * This is for storing the course information into a database to retrieve on start up and to
 * update and save when the user leaves the application
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_FILENAME = "courses.db";
    public static final String TABLE_COURSES = "Courses";
    public static final String TABLE_GRADES = "Grades";
    public static final String TABLE_COURSE_DISTRIBUTION = "Course_Distribution";

    // create table statements
    public static final String CREATE_TABLE_COURSES = "CREATE TABLE " + TABLE_COURSES + "(" +
            "  id integer primary key, " +
            "  courseName text not null," +
            "  courseMark real not null" +
            ")";
    public static final String CREATE_TABLE_GRADES = "CREATE TABLE " + TABLE_GRADES + "(" +
            "  id integer primary key, " +
            "  gradeName text not null," +
            "  gradeType text not null," +
            "  gradeMark real not null," +
            "  courseID integer not null" +
            ")";
    public static final String CREATE_TABLE_COURSE_DISTRIBUTION = "CREATE TABLE " + TABLE_COURSE_DISTRIBUTION + "(" +
            "  categoryName text not null," +
            "  distribution real not null," +
            "  courseID integer not null" +
            ")";

    //drop table statements
    public static final String DROP_COURSES = "DROP TABLE IF EXISTS " + TABLE_COURSES;

    public static final String DROP_GRADES = "DROP TABLE IF EXISTS " + TABLE_GRADES;

    public static final String DROP_COURSE_DISTRIBUTION = "DROP TABLE IF EXISTS " + TABLE_COURSE_DISTRIBUTION;

    //constructor
    public DBHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_COURSES);
        database.execSQL(CREATE_TABLE_GRADES);
        database.execSQL(CREATE_TABLE_COURSE_DISTRIBUTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // the implementation below is adequate for the first version
        // however, if we change our table at all, we'd need to execute code to move the data
        // to the new table structure, then delete the old tables (renaming the new ones)

        // the current version destroys all existing data
        database.execSQL(DROP_COURSES);
        database.execSQL(DROP_GRADES);
        database.execSQL(DROP_COURSE_DISTRIBUTION);
        database.execSQL(CREATE_TABLE_GRADES);
        database.execSQL(CREATE_TABLE_GRADES);
        database.execSQL(CREATE_TABLE_COURSE_DISTRIBUTION);
    }
    //Creators
    public Course createCourse(String name) {
        // create the object
        Course course = new Course(name);

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // insert the data into the database
        ContentValues values = new ContentValues();
        values.put("courseName", course.getName());
        values.put("courseMark", course.getMark());
        long id = database.insert(TABLE_COURSES, null, values);

        // assign the Id of the new database row as the Id of the object
       course.setId(id);

        return course;
    }
    public Grade createGrade(String name, String type, float mark, long courseId) {
        // create the object
        Grade grade = new Grade(name, type, mark, courseId);

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // insert the data into the database
        ContentValues values = new ContentValues();
        values.put("gradeName", grade.getName());
        values.put("gradeType", grade.getType());
        values.put("gradeMark", grade.getMark());
        values.put("courseID", courseId);
        long id = database.insert(TABLE_GRADES, null, values);

        // assign the Id of the new database row as the Id of the object
        grade.setId(id);

        return grade;
    }
    public  Map<String,Float> createDistribution(String category, float distribution, int id) {
        // create the object
        Map<String,Float> distributionMap = new HashMap<>();
        distributionMap.put(category,distribution);

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // insert the data into the database
        ContentValues values = new ContentValues();
        values.put("categoryName", category);
        values.put("distribution", distribution);
        values.put("courseID", id);
        database.insert(TABLE_COURSE_DISTRIBUTION, null, values);

        return distributionMap;
    }
    //Getters
    //Get course by course id (id is unique)
    public Course getCourseById(int courseId) {
        Course course = null;

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // retrieve the contact from the database
        String[] columns = new String[] { "id", "courseName", "courseMark"};
        Cursor cursor = database.query(TABLE_COURSES, columns, "id = ?", new String[]{"" + courseId}, "", "", "");
        if (cursor.getCount() >= 1) {
            cursor.moveToFirst();
            long id = Long.parseLong(cursor.getString(0));
            String courseName = cursor.getString(1);
            float courseMark = Float.parseFloat(cursor.getString(2));
            course = new Course(courseName, courseMark);
            course.setId(id);
        }

        Log.i("DatabaseAccess", "getCourse(" + courseId + "):  course: " + course);
        cursor.close();
        return course;
    }
    //Get grade by grade id (id is unique)
    public Grade getGrade(int gradeId) {
        Grade grade = null;

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // retrieve the contact from the database
        String[] columns = new String[] { "id", "gradeName", "gradeType", "gradeMark", "courseID"};
        Cursor cursor = database.query(TABLE_GRADES, columns, "id = ?", new String[]{"" + gradeId}, "", "", "");
        if (cursor.getCount() >= 1) {
            cursor.moveToFirst();
            long id = Long.parseLong(cursor.getString(0));
            String gradeName = cursor.getString(1);
            String gradeType = cursor.getString(2);
            float gradeMark = Float.parseFloat(cursor.getString(3));
            long courseID = Long.parseLong(cursor.getString(4));
            grade = new Grade(gradeName,gradeType, gradeMark, courseID);
            grade.setId(id);
        }

        Log.i("DatabaseAccess", "getGrade(" + gradeId + "):  grade: " + grade);
        cursor.close();
        return grade;
    }
    //Get all grades for one course id
    public List<Grade> getGradesByCourseId(int courseId) {
        List<Grade> grades = new ArrayList<>();

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // retrieve the contact from the database
        String[] columns = new String[] { "id", "gradeName", "gradeType", "gradeMark", "courseID"};
        Cursor cursor = database.query(TABLE_GRADES, columns, "courseID = ?", new String[]{"" + courseId}, "", "", "");
        if (cursor.getCount() >= 1) {
            cursor.moveToFirst();
            do {
                // collect the contact data, and place it into a contact object
                long id = Long.parseLong(cursor.getString(0));
                String gradeName = cursor.getString(1);
                String gradeType = cursor.getString(2);
                float gradeMark = Float.parseFloat(cursor.getString(3));
                long courseID = Long.parseLong(cursor.getString(4));

                Grade grade = new Grade(gradeName,gradeType, gradeMark, courseID);
                grade.setId(id);
                //add distribution to map
                grades.add(grade);

                // advance to the next row in the results
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        Log.i("DatabaseAccess", "getGradesByCourseId(" + courseId + ")");
        cursor.close();
        return grades;
    }
    //Gets all distribution with 1 course id
    public Map<String,Float> getDistribution(int courseId) {

        Map<String,Float> courseDistribution = new HashMap<>();

        Course course = null;

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // retrieve the contact from the database
        String[] columns = new String[] { "categoryName", "distribution", "courseID"};
        Cursor cursor = database.query(TABLE_COURSE_DISTRIBUTION, columns, "courseID = ?", new String[]{"" + courseId}, "", "", "");
        if (cursor.getCount() >= 1) {
            cursor.moveToFirst();
            do {
                // collect the contact data, and place it into a contact object
                String category = cursor.getString(0);
                float distribution = Float.parseFloat(cursor.getString(1));
                //add distribution to map
                courseDistribution.put(category, distribution);

                // advance to the next row in the results
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        Log.i("DatabaseAccess", "getDistribution(" + courseId + "):  course: " + course);
        cursor.close();
        return courseDistribution;
    }
    //Deleters
    public boolean deleteCourseById(long id) {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // delete the contact
        int numRowsAffected = database.delete(TABLE_COURSES, "id = ?", new String[] { "" + id });

        Log.i("DatabaseAccess", "deleteContact(" + id + "):  numRowsAffected: " + numRowsAffected);

        // verify that the contact was deleted successfully
        return (numRowsAffected == 1);
    }
    public boolean deleteGradeById(int id) {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // delete the contact
        int numRowsAffected = database.delete(TABLE_GRADES, "id = ?", new String[] { "" + id });

        Log.i("DatabaseAccess", "deleteContact(" + id + "):  numRowsAffected: " + numRowsAffected);

        // verify that the contact was deleted successfully
        return (numRowsAffected == 1);
    }
    public void deleteAllCourses() {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // delete the contact
        int numRowsAffected = database.delete(TABLE_COURSES, "", new String[] {});

        Log.i("DatabaseAccess", "deleteAllContacts():  numRowsAffected: " + numRowsAffected);
    }
    public void deleteAllGrades() {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // delete the contact
        int numRowsAffected = database.delete(TABLE_GRADES, "", new String[] {});

        Log.i("DatabaseAccess", "deleteAllContacts():  numRowsAffected: " + numRowsAffected);
    }
    //Updaters
    public boolean updateCourse(Course course) {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // update the data in the database
        ContentValues values = new ContentValues();
        values.put("courseName", course.getName());
        values.put("courseMark", course.getMark());
        int numRowsAffected = database.update(TABLE_COURSES, values, "id = ?", new String[]{"" + course.getId()});

        Log.i("DatabaseAccess", "updateCourse(" + course + "):  numRowsAffected: " + numRowsAffected);

        // verify that the contact was updated successfully
        return (numRowsAffected == 1);
    }
    public boolean updateGrade(Grade grade) {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // update the data in the database
        ContentValues values = new ContentValues();
        values.put("gradeName", grade.getName());
        values.put("gradeType", grade.getType());
        values.put("gradeMark", grade.getMark());
        values.put("courseID",  grade.getCourseId());
        int numRowsAffected = database.update(TABLE_COURSES, values, "id = ?", new String[] { "" + grade.getId() });

        Log.i("DatabaseAccess", "updateGrade(" + grade + "):  numRowsAffected: " + numRowsAffected);

        // verify that the contact was updated successfully
        return (numRowsAffected == 1);
    }
    public boolean updateDistribution(String category, float distribution, int courseID) {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // update the data in the database
        ContentValues values = new ContentValues();
        values.put("categoryName", category);
        values.put("distribution", distribution);
        values.put("courseID", courseID);
        int numRowsAffected = database.update(TABLE_COURSE_DISTRIBUTION, values, "courseID = ? AND categoryName = ?", new String[] { "" + courseID, "" + category });

        Log.i("DatabaseAccess", "updateDistribution(" + category + " : " + distribution + "):  numRowsAffected: " + numRowsAffected);

        // verify that the contact was updated successfully
        return (numRowsAffected == 1);
    }
}
