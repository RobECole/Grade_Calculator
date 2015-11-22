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
import java.util.Scanner;

public class RemoveSemester extends AppCompatActivity {

    public ArrayAdapter<Semester> adapter;
    Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_semester);

        sp = (Spinner)findViewById(R.id.spinner);
        adapter = new ArrayAdapter<Semester>(this,android.R.layout.simple_spinner_item, (ArrayList<Semester>)getIntent().getSerializableExtra("list"));
        sp.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remove_semester, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    public void deleteSemester(View view) {
        Semester line = (Semester)sp.getSelectedItem();
        Intent results = new Intent();
        results.putExtra("semester", line);

        setResult(RESULT_OK,results);
        finish();

    }
}
