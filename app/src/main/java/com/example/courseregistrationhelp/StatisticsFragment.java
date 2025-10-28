package com.example.courseregistrationhelp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ListView courseListView;
    private StatisticsCourseListAdapter adapter;
    private List<Course> courseList;
    //common
    public static int totalCredit = 0;
    public static TextView credit;

    private ArrayAdapter rankAdapter;
    private Spinner rankSpinner;
    private ListView rankListView;
    private RankListAdapter rankListAdapter;
    private List<Course> rankList;

    //ㄱ생성자
    @Override
    public void onActivityCreated(Bundle b){
        super.onActivityCreated(b);
        courseListView = (ListView) getActivity().findViewById(R.id.courseListView);
        courseList = new ArrayList<Course>();
        adapter = new StatisticsCourseListAdapter(getContext().getApplicationContext(), courseList, this);
        courseListView.setAdapter(adapter);
        new BackgroundTask().execute();
        totalCredit = 0;
        credit = (TextView) getView().findViewById(R.id.totalCredit);
        rankSpinner = (Spinner) getView().findViewById(R.id.rankSpinner);
        rankAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.rank, R.layout.spinner_item);
        rankSpinner.setAdapter(rankAdapter);
        rankSpinner.setPopupBackgroundResource(R.color.colorPrimary); //클릭시 배경색 조정
        rankListView = (ListView) getView().findViewById(R.id.rankListView);
        rankList = new ArrayList<Course>();
        rankListAdapter = new RankListAdapter(getContext().getApplicationContext(), rankList, this);
        rankListView.setAdapter(rankListAdapter);
        new ByEntire().execute();

        rankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(rankSpinner.getSelectedItem().equals("전체 인기순")){
                rankList.clear();
                new ByEntire().execute();
                }else if(rankSpinner.getSelectedItem().equals("전공학과 인기순")){

                }else if(rankSpinner.getSelectedItem().equals("남자 인기순")){
                rankList.clear();
                new ByMale().execute();
                }else if(rankSpinner.getSelectedItem().equals("여자 인기순")){
                rankList.clear();
                new ByFemale().execute();
                }else if(rankSpinner.getSelectedItem().equals("전공 인기순")){

                }else if(rankSpinner.getSelectedItem().equals("교양 인기순")){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //---->
    class ByFemale extends AsyncTask<Void, Void, String> {

        String target; // 접속할 주소

        @Override
        protected void onPreExecute() {
            try {
                target = "http://seq0914.dothome.co.kr/ByFemale.php";
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
                // 실제로 서버에서 받은 응답 내용 로그 찍기
                Log.d("KK->ScheduleFragment", "Server response: " + result);

                JSONObject jsonObject = new JSONObject(result);
                //response에 각각의 공지사항들이 있음.
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                int count = 0;
                int courseID;
                String courseGrade;
                String courseTitle;
                int courseCredit;
                String courseProfessor;
                int courseDivide;
                int coursePersonnel;

                String courseTime;

                while (count < jsonArray.length()) {
                    //카운트에 맞는 것 가져옴!
                    JSONObject object = jsonArray.getJSONObject(count);
                    //해당값 가져옴!
                    courseID = object.getInt("courseID");
                    courseGrade = object.getString("courseGrade");
                    courseTitle = object.getString("courseTitle");
                    courseDivide = object.getInt("courseDivide");
                    courseProfessor = object.getString("courseProfessor");
                    courseCredit = object.getInt("courseCredit");
                    courseDivide = object.getInt("courseDivide");
                    coursePersonnel = object.getInt("coursePersonnel");
                    courseTime = object.getString("courseTime");

                    rankList.add(new Course( courseID,  courseGrade,  courseTitle,  courseCredit,  courseDivide,  coursePersonnel, courseTime,  courseProfessor));
                    count++;
                } //end while
                rankListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }       //
    }
    //--->
    class ByMale extends AsyncTask<Void, Void, String> {

        String target; // 접속할 주소

        @Override
        protected void onPreExecute() {
            try {
                target = "http://seq0914.dothome.co.kr/ByMale.php";
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
                // 실제로 서버에서 받은 응답 내용 로그 찍기
                Log.d("KK->ScheduleFragment", "Server response: " + result);

                JSONObject jsonObject = new JSONObject(result);
                //response에 각각의 공지사항들이 있음.
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                int count = 0;
                int courseID;
                String courseGrade;
                String courseTitle;
                int courseCredit;
                String courseProfessor;
                int courseDivide;
                int coursePersonnel;

                String courseTime;

                while (count < jsonArray.length()) {
                    //카운트에 맞는 것 가져옴!
                    JSONObject object = jsonArray.getJSONObject(count);
                    //해당값 가져옴!
                    courseID = object.getInt("courseID");
                    courseGrade = object.getString("courseGrade");
                    courseTitle = object.getString("courseTitle");
                    courseDivide = object.getInt("courseDivide");
                    courseProfessor = object.getString("courseProfessor");
                    courseCredit = object.getInt("courseCredit");
                    courseDivide = object.getInt("courseDivide");
                    coursePersonnel = object.getInt("coursePersonnel");
                    courseTime = object.getString("courseTime");

                    rankList.add(new Course( courseID,  courseGrade,  courseTitle,  courseCredit,  courseDivide,  coursePersonnel, courseTime,  courseProfessor));
                    count++;
                } //end while
                rankListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }       //
    }
    //-->
    class ByEntire extends AsyncTask<Void, Void, String> {

        String target; // 접속할 주소

        @Override
        protected void onPreExecute() {
            try {
                target = "http://seq0914.dothome.co.kr/ByEntire.php";
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
                // 실제로 서버에서 받은 응답 내용 로그 찍기
                Log.d("KK->ScheduleFragment", "Server response: " + result);

                JSONObject jsonObject = new JSONObject(result);
                //response에 각각의 공지사항들이 있음.
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                int count = 0;
                int courseID;
                String courseGrade;
                String courseTitle;
                int courseCredit;
                String courseProfessor;
                int courseDivide;
                int coursePersonnel;

                String courseTime;

                while (count < jsonArray.length()) {
                    //카운트에 맞는 것 가져옴!
                    JSONObject object = jsonArray.getJSONObject(count);
                    //해당값 가져옴!
                    courseID = object.getInt("courseID");
                    courseGrade = object.getString("courseGrade");
                    courseTitle = object.getString("courseTitle");
                    courseDivide = object.getInt("courseDivide");
                    courseProfessor = object.getString("courseProfessor");
                    courseCredit = object.getInt("courseCredit");
                    courseDivide = object.getInt("courseDivide");
                    coursePersonnel = object.getInt("coursePersonnel");
                    courseTime = object.getString("courseTime");

                    rankList.add(new Course( courseID,  courseGrade,  courseTitle,  courseCredit,  courseDivide,  coursePersonnel, courseTime,  courseProfessor));
                    count++;
                } //end while
                rankListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }       //
    }

    //<--
    // 2
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target; // 접속할 주소

        @Override
        protected void onPreExecute() {
            try {
                target = "http://seq0914.dothome.co.kr/StatisticsCourseList.php?studentID=" + URLEncoder.encode(MainActivity.studentID, "UTF-8");
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
                // 실제로 서버에서 받은 응답 내용 로그 찍기
                Log.d("KK->ScheduleFragment", "Server response: " + result);

                JSONObject jsonObject = new JSONObject(result);
                //response에 각각의 공지사항들이 있음.
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                int count = 0;
                int courseID;
                String courseGrade;
                String courseTitle;
                int courseDivide;
                int coursePersonnel;
                int courseRival;

                while (count < jsonArray.length()) {
                    //카운트에 맞는 것 가져옴!
                    JSONObject object = jsonArray.getJSONObject(count);
                    //해당값 가져옴!
                    courseID = object.getInt("courseID");
                    courseGrade = object.getString("courseGrade");
                    courseTitle = object.getString("courseTitle");
                    courseDivide = object.getInt("courseDivide");
                    coursePersonnel = object.getInt("coursePersonnel");
                    courseRival = object.getInt("COUNT(SCHEDULE.courseID)");
                    int courseCredit = object.getInt("courseCredit");
                    totalCredit += courseCredit;
                    courseList.add(new Course(courseID, courseGrade, courseTitle, courseDivide, coursePersonnel, courseRival, courseCredit));
                    count++;
                } //end while
                adapter.notifyDataSetChanged();
                credit.setText(totalCredit + "학점");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }       //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }
}