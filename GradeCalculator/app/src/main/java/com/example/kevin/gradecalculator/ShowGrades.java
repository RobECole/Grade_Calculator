package com.example.kevin.gradecalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
    private ListView lv;
    private Course select;
    private int currGradeId = 0;

    /*
    Main activity on the grade level. calls add remove grade and distribution. displays grade and
    current mark in the course at the top.
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_grades);

        select = (Course)getIntent().getSerializableExtra("course");

        try{
           // Toast.makeText(getApplicationContext(),select.getGrades().toString(), Toast.LENGTH_SHORT).show();
            lv = (ListView)findViewById(R.id.listView);
            adapter = new GradeAdapter(this, select.getGrades());
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    currGradeId = Integer.parseInt("" + id);
                    Grade val = select.getGrades().get(Integer.parseInt("" + id));
                    //Toast.makeText(getApplicationContext(), "TEST!", Toast.LENGTH_SHORT).show();
                    boolean validDistribution = false;
                    List<String> distributions = select.getDistributionNames();
                    if (distributions.contains(val.getType()) == true) {
                        validDistribution = true;
                        final String type = val.getType();
                        List<String> modifiedDis = new ArrayList<String>() {{
                            add(type);
                        }};
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


        }catch (NullPointerException ignored){
        }

        updateMark();
    }
    @Override
    public void onStart(){
        super.onStart();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_return) {
            Intent results = new Intent();
            results.putExtra("course", select);
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
                float dist = Float.parseFloat(resultIntent.getStringExtra("weight"));
                Map<String,Float> categoryDistribution = MainActivity.dbHelper.createDistribution(type, dist, (int) select.getId());
                select.addCategoryDistribution(categoryDistribution);
                response = "Add Distribution";
            }else if(requestCode == RMV_DISTRIBUTION_REQUEST){
                //TODO manage deletion of distribution type
                String type = resultIntent.getStringExtra("distribution");
                boolean success = MainActivity.dbHelper.deleteDistribution(select.getId(), type);
                select.deleteCategory(type);
                response = "Remove Distribution";
            }else if(requestCode == EDIT_GRADES_REQUEST){
                Grade val = select.getGrades().get(Integer.parseInt("" + currGradeId));
                val.setName(resultIntent.getStringExtra("name"));
                val.setMark(Float.parseFloat(resultIntent.getStringExtra("mark")));
                val.setType( resultIntent.getStringExtra("type"));
                MainActivity.dbHelper.updateGrade(val);
                for (Grade s : select.getGrades()) {
                    if (s.getName().equals(val.getName())) {
                        select.deleteGrade(val);
                        break;
                    }
                }
                select.addGrade(val);
                response = "Update Grade";
            }
            Toast.makeText(getApplicationContext(), "Successful: " + response, Toast.LENGTH_SHORT).show();
            lv = (ListView)findViewById(R.id.listView);
            adapter = new GradeAdapter(this,select.getGrades());
            lv.setAdapter(adapter);
        }else{
            if(requestCode == ADD_GRADE_REQUEST){
                response = "Add Grade";
            }else if(requestCode == RMV_GRADE_REQUEST){
                response = "Remove Grade";
            }else if(requestCode == ADD_DISTRIBUTION_REQUEST){
                response = "Add Distribution";
            }else if(requestCode == RMV_DISTRIBUTION_REQUEST){
                response = "Remove Distribution";
            }else if(requestCode == EDIT_GRADES_REQUEST){
                response = "Update Grade";
            }
            Toast.makeText(getApplicationContext(), "Failed to: " + response, Toast.LENGTH_SHORT).show();
        }
        updateMark();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Any unsaved data without going back to the main menu will be lost. Do you want to continue?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent results = new Intent();
                        results.putExtra("course", select);
                        setResult(RESULT_OK, results);
                        finish();
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void addGrade(View view) {
        Intent intent = new Intent(this, AddGrade.class);
        intent.putExtra("list", (Serializable) select.getDistributionNames());
        startActivityForResult(intent, ADD_GRADE_REQUEST);
    }

    public void rmvGrade(View view) {
        Intent intent = new Intent(this, RemoveGrade.class);
        intent.putExtra("list", (Serializable)select.getGrades());
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
        float mark = 0;
        //Get each distribution
        for (Map.Entry<String, Float> entry : distributions.entrySet())
        {
            String category = entry.getKey();
            float distribution = entry.getValue();
            float sumEachCategory = 0;
            int numMarks = 0;
            //sum all grades for a distribution
            for(Grade grade : grades){
                if(grade.getType().equals(category)){
                    sumEachCategory = sumEachCategory + grade.getMark();
                    numMarks ++;
                    Log.d("Add Grade-Distribution","Sum: " + sumEachCategory + " Mark: " + grade.getMark() + " Num: " + numMarks);
                }
            }
            if(numMarks > 0) {
                float total = sumEachCategory / numMarks;
                float distributedTotal = total * (distribution/100);
                mark += distributedTotal;
                Log.d("Add Distribution-Total","Total: " + mark + " Distributed: " + distributedTotal + " Even: " + total);
            }
        }
        TextView courseName = (TextView) findViewById(R.id.lbl_CourseName);
        TextView courseMark = (TextView) findViewById(R.id.lbl_CourseMark);
        courseName.setText(select.getName());
        courseMark.setText("" + mark);
        Course course = new Course(select.getName(), mark);
        course.setId(select.getId());
        MainActivity.dbHelper.updateCourse(course);
        select.setMark(mark);
    }
}
