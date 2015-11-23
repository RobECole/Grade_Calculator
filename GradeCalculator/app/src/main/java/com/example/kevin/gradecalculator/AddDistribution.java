package com.example.kevin.gradecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddDistribution extends AppCompatActivity {

    List<String> distributionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_distribution);
        distributionList = (List<String>)getIntent().getSerializableExtra("list");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_distribution, menu);
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

    public void addDistribution(View view) {
        Spinner type = (Spinner)findViewById(R.id.distributionType);
        String disType = (String)type.getSelectedItem();
        EditText weight = (EditText)findViewById(R.id.weight);

        String temp = "";
        int currMax = 0;
        int value = 0;
        for(String distribution : distributionList){
            if(distribution.contains(disType)){
                temp = distribution.replace(disType,"") + "";
                if(temp.equals("")){
                    value = 0;
                }else{
                    value= Integer.parseInt(temp);
                }
                if(value > currMax){
                    currMax = value;
                }
            }
        }

        Intent results = new Intent();
        results.putExtra("type", disType+ currMax);
        results.putExtra("weight", weight.getText().toString());

        setResult(RESULT_OK,results);
        finish();
    }
}
