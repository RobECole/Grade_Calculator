package com.example.kevin.gradecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class AddSemester extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_semester);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_semester, menu);
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
        EditText year = (EditText)findViewById(R.id.yearIn);
        EditText term = (EditText)findViewById(R.id.termIn);
        int yearText = Integer.parseInt(year.getText().toString());
        String termText = term.getText().toString();
        Intent results = new Intent();
        for(Semester c : MainActivity.semesterList){
            if(c.getTerm().equals(termText) && c.getYear()==yearText){
                setResult(RESULT_CANCELED, results);
                finish();
            }
        }
        results.putExtra("Year", year.getText().toString());
        results.putExtra("Term", term.getText().toString());
        setResult(RESULT_OK,results);
        finish();
    }
}
