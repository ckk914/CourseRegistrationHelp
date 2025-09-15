package com.example.courseregistrationhelp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    final static private String URL = "http://seq0914.dothome.co.kr/StudentLogin.php";
    private Map<String, String> parameters;

    public LoginRequest(String StudentID, String StudentPassword, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("StudentID", StudentID);
        parameters.put("StudentPassword", StudentPassword);

    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
