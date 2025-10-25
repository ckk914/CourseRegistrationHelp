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

public class CourseListAdapter extends BaseAdapter {
    private Context context;
    private List<Course> courseList;
    private Fragment parent;
    private String studentID = MainActivity.studentID;
    private Schedule schedule = new Schedule();
    private List<Integer> courseIDList;
    public static int totalCredit = 0;
    private static int MAX_CREDIT = 24;

    public CourseListAdapter( Context context,List<Course> courseList, Fragment parent) {
        this.courseList = courseList;
        this.context = context;
        this.parent = parent;
        schedule = new Schedule();
        courseIDList = new ArrayList<Integer>();
        new BackgroundTask().execute();
        totalCredit = 0;
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
        if(courseList.get(i).getCourseProfessor().equals("")) {
            courseProfessor.setText("개인 연구");
        }
        else{
            courseProfessor.setText(courseList.get(i).getCourseProfessor()+" 교수님");
        }

        courseTime.setText(courseList.get(i).getCourseTime()+"");

        v.setTag(courseList.get(i).getCourseID());

        Button addButton = (Button) v.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            boolean validate = false;
            validate = schedule.validate(courseList.get(i).getCourseTime());
            if(!alreadyIn(courseIDList, courseList.get(i).getCourseID())){
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                AlertDialog dialog = builder.setMessage("이미 추가한 강의입니다.")
                        .setPositiveButton("다시 시도", null)
                        .create();
                dialog.show();
            }
            else if(totalCredit + courseList.get(i).getCourseCredit() > MAX_CREDIT){
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                AlertDialog dialog = builder.setMessage(MAX_CREDIT+"학점을 초과할 수 없습니다.")
                        .setPositiveButton("다시 시도", null)
                        .create();
                dialog.show();
            }
            else if(validate == false){
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                AlertDialog dialog = builder.setMessage("시간표가 중복됩니다.")
                        .setPositiveButton("다시 시도", null)
                        .create();
                dialog.show();
            }
            else{
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("DEBUG", "강의추가 서버 응답: " + s);
                        try {
                            JSONObject jsonResponse = new JSONObject(s);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                                AlertDialog dialog = builder.setMessage("강의가 추가되었습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                courseIDList.add(courseList.get(i).getCourseID());
                                schedule.addSchedule(courseList.get(i).getCourseTime());
                                totalCredit += courseList.get(i).getCourseCredit();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                                AlertDialog dialog = builder.setMessage("강의 추가 실패했습니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                Log.d("DEBUG", "강의추가 실패");
                            }
                        } catch (Exception e) {
                            Log.e("DEBUG", "강의추가 JSON 파싱 오류: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DEBUG", "강의 추가 오류: " + error.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                        AlertDialog dialog = builder.setMessage("강의 추가 실패했습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                    }
                };
                Log.d("kk-DEBUG", "studentID: " + studentID);
                Log.d("kk-DEBUG", "courseID: " + courseList.get(i).getCourseID());
                AddRequest addRequest = new AddRequest(studentID, courseList.get(i).getCourseID()+"",responseListener);
                RequestQueue queue = Volley.newRequestQueue(parent.getContext());
                queue.add(addRequest);
                //
            }
            }
        });

        return v;
    }
    //
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target; // 접속할 주소

        @Override
        protected void onPreExecute() {
            try {
                target = "http://seq0914.dothome.co.kr/ScheduleList.php?studentID=" + URLEncoder.encode(studentID, "UTF-8");
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();

                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");   //읽여서 한줄씩 추
                }
                //사용 해제
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        //해당 결과 처리용
        @Override
        public void onPostExecute(String result) {
            try {

                Log.d("KK_DEBUG_CourseListAdapter", "Server response: " + result);

                JSONObject jsonObject = new JSONObject(result);
                //response에 각각의 공지사항들이 있음.
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                int count = 0;
                String courseProfessor;
                String courseTime;
                int courseID;
                totalCredit = 0;

                while (count < jsonArray.length()) {
                    //카운트에 맞는 것 가져옴!
                    JSONObject object = jsonArray.getJSONObject(count);
                    //해당값 가져옴!
                    courseID = object.getInt("courseID");
                    courseProfessor = object.getString("courseProfessor");
                    courseTime = object.getString("courseTime");
                    totalCredit += object.getInt("courseCredit");
                    courseIDList.add(courseID); //리스트에 추가
                    schedule.addSchedule(courseTime);
                    count++;
                } //end while
            } catch (Exception e) {
                e.printStackTrace();
            }

        }       //
    }
    //
    public boolean alreadyIn(List<Integer> courseIDList, int item){
        for(int i = 0; i < courseIDList.size(); i++){
            if(courseIDList.get(i) == item){
                return false;
            }
        }
        return true;
    }

}
