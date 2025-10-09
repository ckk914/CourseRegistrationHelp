package com.example.courseregistrationhelp;



import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddRequest extends StringRequest{
    final static private String URL = "http://seq0914.dothome.co.kr/CourseAdd.php";
    private Map<String, String> parameters;

    public AddRequest(String studentID, String courseID , Response.Listener<String> listener){
    super(Method.POST, URL, listener, null);

    parameters = new HashMap<>();
    parameters.put("studentID", studentID);
    parameters.put("courseID", courseID);

    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
