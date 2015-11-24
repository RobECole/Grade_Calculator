package com.example.kevin.gradecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;

public class ShowCourses extends AppCompatActivity {
    public static int  ADD_COURSE_REQUEST = 33;
    public static int  RMV_COURSE_REQUEST = 34;
    public static int SHOW_GRADES_REQUEST = 35;
    public CourseAdapter adapter;
    ListView lv;
    Semester select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);

        select = (Semester)getIntent().getSerializableExtra("semester");
        try{

            lv = (ListView)findViewById(R.id.listView);
            adapter = new CourseAdapter(this,select.getCourses());
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Course val = select.getCourses().get(Integer.parseInt("" + id));
                    Intent intent = new Intent(ShowCourses.this, ShowGrades.class);
                    intent.putExtra("course", val);
                    startActivityForResult(intent, SHOW_GRADES_REQUEST );

                }
            });

        }catch (NullPointerException ignored){
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_return) {
            Intent results = new Intent();
            results.putExtra("semester", select);
            setResult(RESULT_OK,results);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent resultIntent) {
        super.onActivityResult(requestCode, responseCode, resultIntent);
        String response = "";
        if (responseCode == RESULT_OK) {
            if(requestCode == ADD_COURSE_REQUEST){
                Course c = MainActivity.dbHelper.createCourse(resultIntent.getStringExtra("coursename"));
                //Toast.makeText(getApplicationContext(), c.getName(), Toast.LENGTH_SHORT).show();
                select.addCourse(c);
                response = "Add Course";
                Toast.makeText(getApplicationContext(), "Successful: " + response, Toast.LENGTH_SHORT).show();
            }else if(requestCode == RMV_COURSE_REQUEST){
                Course s = (Course)resultIntent.getSerializableExtra("course");
                for( Course c: select.getCourses()){
                    if (c.getName().equals(s.getName())) {
                        select.removeCourse(c);
                        MainActivity.dbHelper.deleteCourseById(c.getId());
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                response = "Remove Course";
                Toast.makeText(getApplicationContext(), "Successful: " + response, Toast.LENGTH_SHORT).show();
            }
            lv = (ListView)findViewById(R.id.listView);
            adapter = new CourseAdapter(this,select.getCourses());
            lv.setAdapter(adapter);
        }else{
            if(requestCode == ADD_COURSE_REQUEST){
                response = "Add Course";
            }else if(requestCode == RMV_COURSE_REQUEST){
                response = "Remove Course";
            }else if(requestCode == SHOW_GRADES_REQUEST){
                response = "Show Grades";
            }
            Toast.makeText(getApplicationContext(), "Failed to: " + response, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent results = new Intent();
        results.putExtra("semester", select);
        setResult(RESULT_OK,results);
        finish();
    }

    public void rmvCourse(View view) {
        Intent intent = new Intent(this, RemoveCourse.class);
        intent.putExtra("list", (Serializable) select.getCourses());
        startActivityForResult(intent, RMV_COURSE_REQUEST);
    }

    public void addCourse(View view) {
        Intent intent = new Intent(this, AddCourse.class);
        startActivityForResult(intent, ADD_COURSE_REQUEST);
    }

}
