package com.example.kevin.gradecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.Map;

public class ShowGrades extends AppCompatActivity {
    public static int  ADD_GRADE_REQUEST = 43;
    public static int  RMV_GRADE_REQUEST = 44;
    public static int  ADD_DISTRIBUTION_REQUEST = 45;
    public static int  RMV_DISTRIBUTION_REQUEST = 46;
    public GradeAdapter adapter;
    ListView lv;
    Course select;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        select = (Course)getIntent().getSerializableExtra("course");
        try{

            lv = (ListView)findViewById(R.id.listView);
            adapter = new GradeAdapter(this, select.getGrades());
            lv.setAdapter(adapter);

        }catch (NullPointerException ignored){
        }
        setContentView(R.layout.activity_show_grades);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_grades, menu);
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
    public void onActivityResult(int requestCode, int responseCode, Intent resultIntent) {
        super.onActivityResult(requestCode, responseCode, resultIntent);

        if (responseCode == RESULT_OK) {
            if(requestCode == ADD_GRADE_REQUEST){
               //TODO manage creation of new grade
                Grade c = MainActivity.dbHelper.createGrade(resultIntent.getStringExtra("name"),
                        resultIntent.getStringExtra("type"),
                        Float.parseFloat(resultIntent.getStringExtra("mark")),
                        select.getId());
                //Toast.makeText(getApplicationContext(), c.getName(), Toast.LENGTH_SHORT).show();
                select.addGrade(c);

            }else if(requestCode == RMV_GRADE_REQUEST){
                //TODO manage removal of new grade
                Grade g = (Grade)resultIntent.getSerializableExtra("grade");
                for( Grade s: select.getGrades()){
                    if (s.getName().equals(g.getName())) {
                        select.deleteGrade(g);
                        MainActivity.dbHelper.deleteCourseById(g.getId());
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }

            }else if(requestCode == ADD_DISTRIBUTION_REQUEST){
                //TODO manage creation of distribution type
                String type = resultIntent.getStringExtra("type");
                Float dist = Float.parseFloat(resultIntent.getStringExtra("weight"));
                Map<String,Float> categoryDistribution = MainActivity.dbHelper.createDistribution(type, dist, (int) select.getId());
                select.setCategoryDistribution(categoryDistribution);

            }else if(requestCode == RMV_DISTRIBUTION_REQUEST){
                //TODO manage deletion of distribution type


            }
            lv = (ListView)findViewById(R.id.listView);
            adapter = new GradeAdapter(this,select.getGrades());
            lv.setAdapter(adapter);
        }

    }

    @Override
    public void onBackPressed() {
        Intent results = new Intent();
        results.putExtra("course", select);
        setResult(RESULT_OK,results);
        finish();
    }

    public void addGrade(View view) {
        Intent intent = new Intent(this, AddGrade.class);
        startActivityForResult(intent, ADD_GRADE_REQUEST);
    }

    public void rmvGrade(View view) {
        Intent intent = new Intent(this, RemoveGrade.class);
        startActivityForResult(intent, RMV_GRADE_REQUEST);
    }

    public void addDistribution(View view) {
        Intent intent = new Intent(this, AddDistribution.class);
        intent.putExtra("list", (Serializable)select.getDistributionNames());
        startActivityForResult(intent, ADD_DISTRIBUTION_REQUEST);
    }

    public void rmvDistribution(View view) {
        Intent intent = new Intent(this, RemoveDistribution.class);
        intent.putExtra("list", (Serializable)select.getDistributionNames());
        startActivityForResult(intent, RMV_DISTRIBUTION_REQUEST);
    }
}
