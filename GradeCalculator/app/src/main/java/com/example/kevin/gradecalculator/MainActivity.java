package com.example.kevin.gradecalculator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    public DBHelper dbHelper;
    private String filename = "semesters.txt";
    private List<Semester> semesterList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onStart(){
        super.onStart();
        semesterList = readFromFile();
    }

    @Override
    public void onStop(){
        super.onStop();
        writeToFile(semesterList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addSemester(View view) {
        Intent intent = new Intent(this, addSemester.class);
        startActivityForResult(intent, 23);
    }

    public void rmvSemester(View view) {
        Intent intent = new Intent(this, removeSemester.class);
        startActivityForResult(intent, 23);
    }


    //write semesters to text file
    public void writeToFile(List<Semester> semesters){
        //sort semesters based on year and then term
        Collections.sort(semesters, new Comparator<Semester>() {
            public int compare(Semester s1, Semester s2) {
                int c;
                c = Integer.valueOf(s1.getYear()).compareTo(Integer.valueOf(s2.getYear()));
                if (c == 0)
                    c = s1.getTerm().compareTo(s2.getTerm());
                return c;
            }
        });
        //write semesters to text file
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
            for(int i = 0; i < semesters.size(); i++) {
                Semester semester = semesters.get(i);
                outputStreamWriter.write(semester.toString());
            }
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    //read semesters from text file
    public List<Semester> readFromFile(){
        List<Semester> semesters = new ArrayList<>();
        try {
            InputStream inputStream = openFileInput(filename);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                while ((receiveString = bufferedReader.readLine()) != null) {
                    Scanner scanner = new Scanner(receiveString);
                    int year = scanner.nextInt();
                    String term = scanner.next();
                    Semester semester = new Semester(term, year);
                    while(scanner.hasNext()){
                        int courseId = scanner.nextInt();
                        Course course = dbHelper.getCourseById(courseId);
                        semester.addCourse(course);
                    }
                    semesters.add(semester);
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return semesters;
    }
}
