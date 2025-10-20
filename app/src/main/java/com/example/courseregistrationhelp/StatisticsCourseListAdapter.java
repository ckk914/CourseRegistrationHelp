package com.example.courseregistrationhelp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class StatisticsCourseListAdapter extends BaseAdapter {
    private Context context;
    private List<Course> courseList;
    private Fragment parent;
    private String studentID = MainActivity.studentID;


    public StatisticsCourseListAdapter(Context context, List<Course> courseList, Fragment parent) {
        this.courseList = courseList;
        this.context = context;
        this.parent = parent;
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
    public View getView(final int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.statistics, null);
        TextView courseGrade = (TextView) v.findViewById(R.id.courseGrade);
        TextView courseTitle = (TextView) v.findViewById(R.id.courseTitle);
        TextView courseDivide = (TextView) v.findViewById(R.id.courseDivide);
        TextView coursePersonnel = (TextView) v.findViewById(R.id.coursePersonnel);
        TextView courseRate = (TextView) v.findViewById(R.id.courseRate);

        if(courseList.get(i).getCourseGrade().equals("제한 없음") || courseList.get(i).getCourseGrade().equals("")){
            courseGrade.setText("모든 학년");
        }
        else {
            courseGrade.setText(courseList.get(i).getCourseGrade()+ "학년");
        }
        courseTitle.setText(courseList.get(i).getCourseTitle());

        courseDivide.setText(courseList.get(i).getCourseDivide()+ "분반");

        if(courseList.get(i).getCoursePersonnel() == 0 ){
            coursePersonnel.setText("제한 없음");
            courseRate.setText("");
        }
        else {
            coursePersonnel.setText("신청 인원: " + courseList.get(i).getCourseRival() + " / "+ courseList.get(i).getCoursePersonnel()+"명");
            int rate = (int) (((double) courseList.get(i).getCourseRival() * 100 /  courseList.get(i).getCoursePersonnel()) + 0.5) ;
            courseRate.setText("경쟁률 : " + rate + "% ");
            if(rate <20){
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorSafe));
            }
            else if(rate <= 50){
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorPrimary));
            }
            else if(rate <= 100){
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorDanger));
            }
            else if(rate <= 150){
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorWarning));
            }
            else{
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorRed));
            }
        }
        v.setTag(courseList.get(i).getCourseID());
        return v;
    }
    //

}
