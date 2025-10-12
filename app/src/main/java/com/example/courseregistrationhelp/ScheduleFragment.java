package com.example.courseregistrationhelp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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

    //
    private TextView monday[] = new TextView[14];
    private TextView tuesday[] = new TextView[14];
    private TextView wednesday[] = new TextView[14];
    private TextView thursday[] = new TextView[14];
    private TextView friday[] = new TextView[14];
    private Schedule schedule = new Schedule();

    //해당 프레그먼트 생성될때 실행
//    @Override
//    public void onActivityCreated(Bundle b){
//        super.onActivityCreated(b);
//
//        int total = 14; // 0부터 13까지 14개
//
//
//        for (int i = 0; i < 14; i++) {
//            monday[i] = getView().findViewById(getResources().getIdentifier("monday" + i, "id", getActivity().getPackageName()));
//            tuesday[i] = getView().findViewById(getResources().getIdentifier("tuesday" + i, "id", getActivity().getPackageName()));
//            wednesday[i] = getView().findViewById(getResources().getIdentifier("wednesday" + i, "id", getActivity().getPackageName()));
//            thursday[i] = getView().findViewById(getResources().getIdentifier("thursday" + i, "id", getActivity().getPackageName()));
//            friday[i] = getView().findViewById(getResources().getIdentifier("friday" + i, "id", getActivity().getPackageName()));
//        }
//
//        new BackgroundTask().execute();
//    }// end onActivityCreated
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int total = 14;
        for (int i = 0; i < total; i++) {
            monday[i] = view.findViewById(getResources().getIdentifier("monday" + i, "id", requireActivity().getPackageName()));
            Log.d("KK->ScheduleFragment", "monday" + i + " = " + monday[i]);
            tuesday[i] = view.findViewById(getResources().getIdentifier("tuesday" + i, "id", requireActivity().getPackageName()));
            wednesday[i] = view.findViewById(getResources().getIdentifier("wednesday" + i, "id", requireActivity().getPackageName()));
            thursday[i] = view.findViewById(getResources().getIdentifier("thursday" + i, "id", requireActivity().getPackageName()));
            friday[i] = view.findViewById(getResources().getIdentifier("friday" + i, "id", requireActivity().getPackageName()));
        }

        new BackgroundTask().execute();
    }
    //
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target; // 접속할 주소

        @Override
        protected void onPreExecute() {
            try {
                target = "http://seq0914.dothome.co.kr/ScheduleList.php?studentID=" + URLEncoder.encode(MainActivity.studentID, "UTF-8");
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
                String courseProfessor;
                String courseTime;
                String courseTitle;
                int courseID;

                while (count < jsonArray.length()) {
                    //카운트에 맞는 것 가져옴!
                    JSONObject object = jsonArray.getJSONObject(count);
                    //해당값 가져옴!
                    courseID = object.getInt("courseID");
                    courseProfessor = object.getString("courseProfessor");
                    courseTime = object.getString("courseTime");
                    courseTitle = object.getString("courseTitle");

                    schedule.addSchedule(courseTime, courseTitle, courseProfessor);
                    count++;
                } //end while
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("KK->ScheduleFragment", ">>> calling schedule.setting() <<<");
            schedule.setting(monday, tuesday, wednesday, thursday, friday, getContext());

        }       //
    }
    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }
}