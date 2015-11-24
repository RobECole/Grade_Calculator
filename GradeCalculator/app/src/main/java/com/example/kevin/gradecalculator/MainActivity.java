package com.example.kevin.gradecalculator;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    public static DBHelper dbHelper;
    public static int  GET_LICENSE_REQUEST = 2;
    public static int  ADD_SEMESTER_REQUEST = 23;
    public static int  RMV_SEMESTER_REQUEST = 24;
    public static int SHOW_COURSE_REQUEST = 3;
    private String filename = "semesters.txt";
    private String licenseUrl = "https://www.gnu.org/licenses/gpl.txt";
    public static List<Semester> semesterList = new ArrayList<>();
    public static SemesterAdapter adapter;
    private ListView lv;
    MediaPlayer mediaPlayer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);

        lv = (ListView)findViewById(R.id.listView);
        adapter = new SemesterAdapter(this, semesterList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Semester val = semesterList.get(Integer.parseInt("" + id));
                Intent intent = new Intent(MainActivity.this, ShowCourses.class);
                intent.putExtra("semester", val);
                startActivityForResult(intent, SHOW_COURSE_REQUEST );

            }
        });

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSemester(view);
            }
        });

        FloatingActionButton fabRmv = (FloatingActionButton) findViewById(R.id.rmv);
        fabRmv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rmvSemester(view);
            }
        });

        initializeMusic();

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
        if(semesterList.isEmpty()) {
            try {
                semesterList = readFromFile();
            } catch (Exception e) {
                Intent intent = new Intent(this, AddSemester.class);
                startActivityForResult(intent, ADD_SEMESTER_REQUEST);
            }
        }
        adapter = new SemesterAdapter(this, semesterList);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (mediaPlayer == null) {
            initializeMusic();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (!(mediaPlayer == null)) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (!(mediaPlayer == null)) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (!(mediaPlayer == null)) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
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
        if (id == R.id.action_license) {
            Intent intent = new Intent(this, ShowLicenseActivity.class);
            intent.putExtra("licenseUrl", licenseUrl);
            startActivityForResult(intent, GET_LICENSE_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent resultIntent) {
        super.onActivityResult(requestCode, responseCode, resultIntent);

        if (responseCode == RESULT_OK) {
           if(requestCode == ADD_SEMESTER_REQUEST){
               Semester s = new Semester(resultIntent.getStringExtra("Term"), Integer.parseInt(resultIntent.getStringExtra("Year")));
               semesterList.add(s);
               Toast.makeText(getApplicationContext(),"Add semester successful", Toast.LENGTH_SHORT).show();
           }else if(requestCode == RMV_SEMESTER_REQUEST){
               Semester s = (Semester)resultIntent.getSerializableExtra("semester");
               for( Semester c: semesterList){
                   if (c.getTerm().equals(s.getTerm()) && c.getYear()==s.getYear()) {
                       semesterList.remove(c);
                       adapter.notifyDataSetChanged();
                       break;
                   }
               }
               Toast.makeText(getApplicationContext(),"Remove semester successful", Toast.LENGTH_SHORT).show();
           }else if(requestCode == SHOW_COURSE_REQUEST){
               Semester s = (Semester)resultIntent.getSerializableExtra("semester");
               for( Semester c: semesterList){
                   if (c.getTerm().equals(s.getTerm()) && c.getYear()==s.getYear()) {
                       semesterList.remove(c);
                       break;
                   }
               }
               semesterList.add(s);
               adapter.notifyDataSetChanged();
           }

        }else if(responseCode == RESULT_CANCELED){
            String error = "";
            if(requestCode == ADD_SEMESTER_REQUEST){
                error = "Add Semester";
            }else if(requestCode == RMV_SEMESTER_REQUEST){
                error = "Remove Semester";
            }else if(requestCode == SHOW_COURSE_REQUEST){
                error = "Show Courses";
            }
            Toast.makeText(getApplicationContext(),"Failed To: " + error, Toast.LENGTH_SHORT).show();
        }
        adapter = new SemesterAdapter(this, semesterList);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        writeToFile(semesterList);
    }

    public void addSemester(View view) {
        Intent intent = new Intent(this, AddSemester.class);
        startActivityForResult(intent, ADD_SEMESTER_REQUEST);
    }

    public void rmvSemester(View view) {
        Intent intent = new Intent(this, RemoveSemester.class);
        intent.putExtra("list", (Serializable) semesterList);
        startActivityForResult(intent, RMV_SEMESTER_REQUEST);
    }

    //write semesters to text file
    public void writeToFile(List<Semester> semesters){
        //sort semesters based on year and then term
        Collections.sort(semesters, new Comparator<Semester>() {
            public int compare(Semester s1, Semester s2) {
                int c;
                c = Integer.valueOf(s1.getYear()).compareTo(s2.getYear());
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
            inputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return semesters;
    }

    public void initializeMusic(){
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.bensoundclearday);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }catch(Exception e){}
    }

}
