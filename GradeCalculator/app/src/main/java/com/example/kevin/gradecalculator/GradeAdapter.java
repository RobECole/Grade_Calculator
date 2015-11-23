package com.example.kevin.gradecalculator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 100484144 on 11/22/2015.
 */
public class GradeAdapter extends BaseAdapter {
    private Context context;
    private List<Grade> data;

    public GradeAdapter(Context context, List<Grade> data) {
        this.data = data;
        this.context = context;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Grade gradeToDisplay = data.get(position);

        Log.d("GradeAdapter", "Course:");
        Log.d("GradeAdapter", "  Name:   "+ gradeToDisplay.getName());
        Log.d("GradeAdapter", "  Type:  "+ gradeToDisplay.getType());
        Log.d("GradeAdapter", "  Mark:  "+ gradeToDisplay.getMark());

        if (convertView == null) {
            // create the layout
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_grade_item, parent, false);
        }

        // populate the views with the data from story
        TextView lblName = (TextView)convertView.findViewById(R.id.lbl_GradeName);
        lblName.setText("Name: " + gradeToDisplay.getName());

        TextView lblType = (TextView)convertView.findViewById(R.id.lbl_GradeMark);
        lblType.setText("Type: " + gradeToDisplay.getType());

        TextView lblMark = (TextView)convertView.findViewById(R.id.lbl_GradeMark);
        lblMark.setText("Mark: " +  gradeToDisplay.getMark());

        return convertView;
    }
}
