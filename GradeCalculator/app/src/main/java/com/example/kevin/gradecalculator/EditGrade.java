package com.example.kevin.gradecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class EditGrade extends AppCompatActivity {
    public ArrayAdapter<String> adapter;
    Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grade);

        sp = (Spinner)findViewById(R.id.gradeType);
        if(getIntent().getBooleanExtra("validType", true)) {
            TextView warning = (TextView)findViewById(R.id.warning);
            warning.setText("");
            warning.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }else{
            TextView warning = (TextView)findViewById(R.id.warning);
            warning.setText("Previous Distribution does not exist!");
            warning.setCompoundDrawablesWithIntrinsicBounds(R.drawable.skull, 0, 0, 0);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, (ArrayList<String>) getIntent().getSerializableExtra("list"));
        sp.setAdapter(adapter);

        EditText name = (EditText)findViewById(R.id.gradeName);
        EditText mark = (EditText)findViewById(R.id.gradeMark);

        String grade = getIntent().getStringExtra("grade");
        Float value = getIntent().getFloatExtra("mark", 0);

        name.setText("" + grade);
        mark.setText("" + value);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_grade, menu);
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

    public void editGrade(View view) {
        EditText name = (EditText)findViewById(R.id.gradeName);
        String line = (String)sp.getSelectedItem();
        EditText mark = (EditText)findViewById(R.id.gradeMark);

        Intent results = new Intent();
        results.putExtra("name", name.getText().toString());
        results.putExtra("type", line);
        results.putExtra("mark", mark.getText().toString());

        setResult(RESULT_OK,results);
        finish();
    }
}
