package com.example.courseregistrationhelp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CourseListAdapter extends BaseAdapter {
    private Context context;
    private List<Course> courseList;

    public CourseListAdapter( Context context,List<Course> courseList) {
        this.courseList = courseList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int i) {
        return courseList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.course, null);
        TextView courseGrade = (TextView) v.findViewById(R.id.courseGrade);
        TextView courseTitle = (TextView) v.findViewById(R.id.courseTitle);
        TextView courseCredit = (TextView) v.findViewById(R.id.courseCredit);
        TextView courseDivide = (TextView) v.findViewById(R.id.courseDivide);
        TextView coursePersonnel = (TextView) v.findViewById(R.id.coursePersonnel);
        TextView courseProfessor = (TextView) v.findViewById(R.id.courseProfessor);
        TextView courseTime = (TextView) v.findViewById(R.id.courseTime);

        if(courseList.get(i).getCourseGrade().equals("제한 없음") || courseList.get(i).getCourseGrade().equals("")){
            courseGrade.setText("모든 학년");
        }
        else {
            courseGrade.setText(courseList.get(i).getCourseGrade()+ "학년");
        }
        courseTitle.setText(courseList.get(i).getCourseTitle());
        courseCredit.setText(courseList.get(i).getCourseCredit()+ "학점");
        courseDivide.setText(courseList.get(i).getCourseDivide()+ "분반");

        if(courseList.get(i).getCoursePersonnel() == 0 ){
            coursePersonnel.setText("제한 없음");
        }
        else {
            coursePersonnel.setText("제한 인원: "+ courseList.get(i).getCoursePersonnel()+"명");
        }
        courseProfessor.setText(courseList.get(i).getCourseProfessor()+" 교수님");
        courseTime.setText(courseList.get(i).getCourseTime()+"");

        v.setTag(courseList.get(i).getCourseID());
        return v;
    }
}
