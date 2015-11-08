package com.example.kevin.gradecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
            "  distribution integer not null," +
            "  courseID integer not null," +
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
    public Course createDistrbution(String category, int distribution, String courseName) {
        // create the object
        Course course = getCourse(courseName);

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // insert the data into the database
        ContentValues values = new ContentValues();
        values.put("categoryName", category);
        values.put("distribution", distribution);
        values.put("courseID", course.getId());
        database.insert(TABLE_COURSE_DISTRIBUTION, null, values);

        return course;
    }
    //Getters
    public Course getCourse(String name) {
        Course course = null;

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // retrieve the contact from the database
        String[] columns = new String[] { "id", "courseName", "courseMark"};
        Cursor cursor = database.query(TABLE_COURSES, columns, "id = ?", new String[]{"" + name}, "", "", "");
        if (cursor.getCount() >= 1) {
            cursor.moveToFirst();
            long id = Long.parseLong(cursor.getString(0));
            String courseName = cursor.getString(1);
            float courseMark = Float.parseFloat(cursor.getString(2));
            course = new Course(courseName, courseMark);
            course.setId(id);
        }

        Log.i("DatabaseAccess", "getCourse(" + name + "):  course: " + course);
        cursor.close();
        return course;
    }
    public Grade getGrade(String name) {
        Grade grade = null;

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // retrieve the contact from the database
        String[] columns = new String[] { "id", "courseName", "courseMark"};
        Cursor cursor = database.query(TABLE_GRADES, columns, "id = ?", new String[]{"" + name}, "", "", "");
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

        Log.i("DatabaseAccess", "getGrade(" + name + "):  grade: " + grade);
        cursor.close();
        return grade;
    }

    //TODO EVERYTHING BELOW THIS!!!
    /* public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // retrieve the contact from the database
        String[] columns = new String[] { "_id", "firstName", "lastName", "email", "phone" };
        Cursor cursor = database.query(TABLE_NAME, columns, "", new String[]{}, "", "", "");
        cursor.moveToFirst();
        do {
            // collect the contact data, and place it into a contact object
            long id = Long.parseLong(cursor.getString(0));
            String firstName = cursor.getString(1);
            String lastName = cursor.getString(2);
            String email = cursor.getString(3);
            String phone = cursor.getString(4);
            Contact contact = new Contact(firstName, lastName, email, phone);
            contact.setId(id);

            // add the current contact to the list
            contacts.add(contact);

            // advance to the next row in the results
            cursor.moveToNext();
        } while (!cursor.isAfterLast());

        Log.i("DatabaseAccess", "getAllContacts():  num: " + contacts.size());

        return contacts;
    }

    public boolean updateContact(Contact contact) {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // update the data in the database
        ContentValues values = new ContentValues();
        values.put("firstName", contact.getFirstName());
        values.put("lastName", contact.getLastName());
        values.put("email", contact.getEmail());
        values.put("phone", contact.getPhone());
        int numRowsAffected = database.update(TABLE_NAME, values, "_id = ?", new String[] { "" + contact.getId() });

        Log.i("DatabaseAccess", "updateContact(" + contact + "):  numRowsAffected: " + numRowsAffected);

        // verify that the contact was updated successfully
        return (numRowsAffected == 1);
    }

    public boolean deleteContact(long id) {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // delete the contact
        int numRowsAffected = database.delete(TABLE_NAME, "_id = ?", new String[] { "" + id });

        Log.i("DatabaseAccess", "deleteContact(" + id + "):  numRowsAffected: " + numRowsAffected);

        // verify that the contact was deleted successfully
        return (numRowsAffected == 1);
    }

    public void deleteAllContacts() {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // delete the contact
        int numRowsAffected = database.delete(TABLE_NAME, "", new String[] {});

        Log.i("DatabaseAccess", "deleteAllContacts():  numRowsAffected: " + numRowsAffected);
    }*/
}
