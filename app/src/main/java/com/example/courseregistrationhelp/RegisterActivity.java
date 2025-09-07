package com.example.courseregistrationhelp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner spinner;
    private String studentID;
    private String studentPassword;
    private String studentGender;
    private String studentMajor;
    private String studentEmail;
    private AlertDialog alertDialog;
    private boolean validate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        spinner = (Spinner) findViewById(R.id.majorSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.major, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final EditText emailText = (EditText) findViewById(R.id.emailText);

        RadioGroup genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        int genderGroupID = genderGroup.getCheckedRadioButtonId();
        studentGender = ((RadioButton) findViewById(genderGroupID)).getText().toString();

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(@NonNull RadioGroup group, int checkedId) {
                RadioButton genderButton = (RadioButton) findViewById(checkedId);
                studentGender = genderButton.getText().toString();
            }
        });

        //회원 중복 체크 버튼
        final Button validateButton = (Button) findViewById(R.id.validateButton);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentID = idText.getText().toString();
                if (validate) {
                    return;
                }
                if (studentID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    AlertDialog dialog = builder.setMessage("아이디를 입력하세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonResponse = new JSONObject(s);
                            boolean success = jsonResponse.getBoolean("success");
                            Log.d("check", "success: " + success);
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                AlertDialog dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false);
                                validate = true;
                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                AlertDialog dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(studentID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentID = idText.getText().toString();
                studentPassword = passwordText.getText().toString();
                studentEmail = emailText.getText().toString();
                studentMajor = spinner.getSelectedItem().toString();

                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    AlertDialog dialog = builder.setMessage("먼저 중복 체크를 해주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                if (studentID.equals("") || studentPassword.equals("") || studentEmail.equals("") || studentMajor.equals("") || studentGender.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    AlertDialog dialog = builder.setMessage("빈 칸 없이 입력하세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
//
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonResponse = new JSONObject(s);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                AlertDialog dialog = builder.setMessage("회원 등록에 성공했습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                finish();  //회원등록 창 닫음
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                AlertDialog dialog = builder.setMessage("회원 등록에 실패했습니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(studentID, studentPassword, studentGender, studentMajor, studentEmail, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
//
            }
        });
    }

    //창 꺼질때 실행됨.
    @Override
    protected void onStop() {
        super.onStop();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

}

