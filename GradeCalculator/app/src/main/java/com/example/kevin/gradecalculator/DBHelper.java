package com.example.kevin.gradecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kevin on 11/7/2015.
 * This is for storing the course information into a database to retrieve on start up and to
 * update and save when the user leaves the application
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_FILENAME = "contacts.db";
    public static final String TABLE_COURSES = "Courses";
    public static final String TABLE_GRADES = "Grades";
    public static final String TABLE_COURSE_DISTRIBUTION = "Course_Distribution";

    // create table statements
    public static final String CREATE_TABLE_COURSES = "CREATE TABLE " + TABLE_COURSES + "(" +
            "  _id integer primary key autoincrement, " +
            "  courseName text not null," +
            "  courseMark real not null" +
            ")";
    public static final String CREATE_TABLE_GRADES = "CREATE TABLE " + TABLE_GRADES + "(" +
            "  _id integer primary key autoincrement, " +
            "  gradeName text not null," +
            "  gradeType text not null," +
            "  gradeMark real not null," +
            "  courseID integer not null" +
            ")";
    public static final String CREATE_TABLE_COURSE_DISTRIBUTION = "CREATE TABLE " + TABLE_COURSE_DISTRIBUTION + "(" +
            "  _id integer primary key autoincrement, " +
            "  categoryName text not null," +
            "  distribution integer not null," +
            "  courseID integer not null," +
            ")";

    //drop table statements
    public static final String DROP_COURSES = "DROP TABLE " + TABLE_COURSES;

    public static final String DROP_GRADES = "DROP TABLE " + TABLE_GRADES;

    public static final String DROP_COURSE_DISTRIBUTION = "DROP TABLE " + TABLE_COURSE_DISTRIBUTION;

    //constructor
    public DBHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
    }

    //onCreate
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
    //TODO EVERYTHING BELOW THIS!!!
   /* public Course createCourse(String name) {
        // create the object
        Course contact = new Course(name);

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // insert the data into the database
        ContentValues values = new ContentValues();
        values.put("firstName", contact.getFirstName());
        values.put("lastName", contact.getLastName());
        values.put("email", contact.getEmail());
        values.put("phone", contact.getPhone());
        long id = database.insert(TABLE_NAME, null, values);

        // assign the Id of the new database row as the Id of the object
        contact.setId(id);

        return contact;
    }

    public Contact getContact(long id) {
        Contact contact = null;

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // retrieve the contact from the database
        String[] columns = new String[] { "firstName", "lastName", "email", "phone" };
        Cursor cursor = database.query(TABLE_NAME, columns, "_id = ?", new String[] { "" + id }, "", "", "");
        if (cursor.getCount() >= 1) {
            cursor.moveToFirst();
            String firstName = cursor.getString(0);
            String lastName = cursor.getString(1);
            String email = cursor.getString(2);
            String phone = cursor.getString(3);
            contact = new Contact(firstName, lastName, email, phone);
            contact.setId(id);
        }

        Log.i("DatabaseAccess", "getContact(" + id + "):  contact: " + contact);

        return contact;
    }

    public ArrayList<Contact> getAllContacts() {
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
