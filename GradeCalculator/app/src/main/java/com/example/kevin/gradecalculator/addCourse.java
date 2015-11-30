package com.example.kevin.gradecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class AddCourse extends AppCompatActivity {

    /*
    Class that renders add course activity. Returns course name and results ok.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent results = new Intent();
        setResult(RESULT_CANCELED,results);
        finish();
    }

    public void addDb(View view) {
        //parse input and pack intent. return to main
        EditText name = (EditText)findViewById(R.id.courseName);

        Intent results = new Intent();
        results.putExtra("coursename", name.getText().toString());
        setResult(RESULT_OK,results);
        finish();
    }
}
