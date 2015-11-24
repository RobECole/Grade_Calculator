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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ShowGrades extends AppCompatActivity {
    public static int  ADD_GRADE_REQUEST = 43;
    public static int  RMV_GRADE_REQUEST = 44;
    public static int  ADD_DISTRIBUTION_REQUEST = 45;
    public static int  RMV_DISTRIBUTION_REQUEST = 46;
    public static int  EDIT_GRADES_REQUEST = 47;

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
        updateMark();
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
        String response = "";
        if (responseCode == RESULT_OK) {
            if(requestCode == ADD_GRADE_REQUEST){
               //TODO manage creation of new grade
                Grade c = MainActivity.dbHelper.createGrade(resultIntent.getStringExtra("name"),
                        resultIntent.getStringExtra("type"),
                        Float.parseFloat(resultIntent.getStringExtra("mark")),
                        select.getId());
                select.addGrade(c);
                response = "Add Grade";
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
                response = "Remove Grade";
            }else if(requestCode == ADD_DISTRIBUTION_REQUEST){
                //TODO manage creation of distribution type
                String type = resultIntent.getStringExtra("type");
                Float dist = Float.parseFloat(resultIntent.getStringExtra("weight"));
                Map<String,Float> categoryDistribution = MainActivity.dbHelper.createDistribution(type, dist, (int) select.getId());
                select.addCategoryDistribution(categoryDistribution);
                response = "Add Distribution";
            }else if(requestCode == RMV_DISTRIBUTION_REQUEST){
                //TODO manage deletion of distribution type
                String type = resultIntent.getStringExtra("distribution");
                boolean success = MainActivity.dbHelper.deleteDistribution(select.getId(), type);
                select.deleteCategory(type);
                response = "Remove Distribution";
            }
            Toast.makeText(getApplicationContext(), "Successful: " + response, Toast.LENGTH_SHORT).show();
            lv = (ListView)findViewById(R.id.listView);
            adapter = new GradeAdapter(this,select.getGrades());
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Grade val = select.getGrades().get(Integer.parseInt("" + id));
                    //Toast.makeText(getApplicationContext(), "TEST!", Toast.LENGTH_SHORT).show();
                    boolean validDistribution = false;
                    List<String> distributions = select.getDistributionNames();
                    if(distributions.contains(val.getType())==true){
                        validDistribution = true;
                        List<String> modifiedDis = Arrays.asList(val.getType());
                        distributions.remove(val.getType());
                        modifiedDis.addAll(distributions);
                        distributions = modifiedDis;
                    }
                    Intent intent = new Intent(ShowGrades.this, EditGrade.class);
                    intent.putExtra("grade", val.getName());
                    intent.putExtra("mark", val.getMark());
                    intent.putExtra("validType", validDistribution);
                    intent.putExtra("list", (Serializable) distributions);
                    startActivityForResult(intent, EDIT_GRADES_REQUEST);

                }
            });
        }else{
            if(requestCode == ADD_GRADE_REQUEST){
                response = "Add Grade";
            }else if(requestCode == RMV_GRADE_REQUEST){
                response = "Remove Grade";
            }else if(requestCode == ADD_DISTRIBUTION_REQUEST){
                response = "Add Distribution";
            }else if(requestCode == RMV_DISTRIBUTION_REQUEST){
                response = "Remove Distribution";
            }
            Toast.makeText(getApplicationContext(), "Failed to: " + response, Toast.LENGTH_SHORT).show();
        }
        updateMark();
    }

    @Override
    public void onBackPressed() {
        Intent results = new Intent();
        results.putExtra("course", select);
        setResult(RESULT_OK, results);
        finish();
    }

    public void addGrade(View view) {
        Intent intent = new Intent(this, AddGrade.class);
        intent.putExtra("list", (Serializable) select.getDistributionNames());
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

    public void updateMark(){
        Map<String, Float> distributions = select.getCategoryDistribution();
        List<Grade> grades = select.getGrades();
        Float mark = 0f;
        //Get each distribution
        for (Map.Entry<String, Float> entry : distributions.entrySet())
        {
            String category = entry.getKey();
            Float distribution = entry.getValue();
            Float sumEachCategory = 0f;
            int numMarks = 0;
            //sum all grades for a distribution
            for(Grade grade : grades){
                if(grade.getType().equals(distribution)){
                    sumEachCategory += grade.getMark();
                    numMarks ++;
                }
            }
            Float total = sumEachCategory/numMarks;
            Float distributedTotal = total*distribution;
            mark += distributedTotal;
        }
    }
}
