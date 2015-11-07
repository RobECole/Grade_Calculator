package com.example.kevin.gradecalculator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kevin on 11/7/2015.
 * CourseAdapter is an adapter to display the general course information on the screen
 */
public class CourseAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Course> data;

    public CourseAdapter(Context context, ArrayList<Course> data) {
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
        Course courseToDisplay = data.get(position);

        Log.d("CourseAdapter", "Course:");
        Log.d("CourseAdapter", "  Name:   "+ courseToDisplay.getName());
        Log.d("CourseAdapter", "  Mark:  "+courseToDisplay.getMark());

        if (convertView == null) {
            // create the layout
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_course_item, parent, false);
        }

        // populate the views with the data from story
        TextView lblTitle = (TextView)convertView.findViewById(R.id.lbl_CourseName);
        lblTitle.setText(courseToDisplay.getName());

        TextView lblAuthor = (TextView)convertView.findViewById(R.id.lbl_CourseMark);
        lblAuthor.setText(courseToDisplay.getMark());

        return convertView;
    }
}
