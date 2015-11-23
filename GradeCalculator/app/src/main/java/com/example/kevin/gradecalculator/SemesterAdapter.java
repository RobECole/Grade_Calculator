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
public class SemesterAdapter extends BaseAdapter  {
        private Context context;
        private List<Semester> data;

        public SemesterAdapter(Context context, List<Semester> data) {
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
        Semester semesterToDisplay = data.get(position);

        Log.d("CourseAdapter", "Course:");
        Log.d("CourseAdapter", "  Year:   "+ semesterToDisplay.getYear());
        Log.d("CourseAdapter", "  Term:  " + semesterToDisplay.getTerm());

        if (convertView == null) {
            // create the layout
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_semester_item, parent, false);
        }

        // populate the views with the data from story
        TextView lblYear = (TextView)convertView.findViewById(R.id.lbl_SemesterYear);
        lblYear.setText("Year: " + semesterToDisplay.getYear());

        TextView lblTerm = (TextView)convertView.findViewById(R.id.lbl_SemesterTerm);
        lblTerm.setText("Term: " + semesterToDisplay.getTerm());

        return convertView;
    }
}
