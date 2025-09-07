package com.example.courseregistrationhelp;



import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest{
    final static private String URL = "http://seq0914.dothome.co.kr/StudentValidate.php";
    private Map<String, String> parameters;

    public ValidateRequest(String StudentID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("StudentID", StudentID);
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
