package com.example.kevin.gradecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class RemoveGrade extends AppCompatActivity {
    public ArrayAdapter<Course> adapter;
    Spinner sp;

    /*
    Class that renders remove grade activity. Returns grade to be removed and results ok
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_grade);

        sp = (Spinner)findViewById(R.id.spinner);
        adapter = new ArrayAdapter<Course>(this,android.R.layout.simple_spinner_item, (ArrayList<Course>)getIntent().getSerializableExtra("list"));
        sp.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remove_grade, menu);
        return true;
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

    @Override
    public void onBackPressed() {
        //cancels activity, return result canceled
        Intent results = new Intent();
        setResult(RESULT_CANCELED,results);
        finish();
    }

    public void deleteGrade(View view) {
        //parses user input from spinner, packs intent, returns result ok
        Grade line = (Grade)sp.getSelectedItem();
        Intent results = new Intent();
        results.putExtra("grade", line);
        setResult(RESULT_OK,results);
        finish();
    }
}
