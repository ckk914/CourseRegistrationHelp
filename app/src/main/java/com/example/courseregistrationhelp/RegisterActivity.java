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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner spinner;
    private String studentID;
    private String studentPassword;
    private String studentGender = ""; // 초기값을 빈 문자열로 설정
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

        // 초기 성별 값 안전하게 처리
        int genderGroupID = genderGroup.getCheckedRadioButtonId();
        if (genderGroupID != -1) {
            RadioButton selectedGender = (RadioButton) findViewById(genderGroupID);
            if (selectedGender != null) {
                studentGender = selectedGender.getText().toString();
            }
        }

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull RadioGroup group, int checkedId) {
                RadioButton genderButton = (RadioButton) findViewById(checkedId);
                if (genderButton != null) {
                    studentGender = genderButton.getText().toString();
                    Log.d("DEBUG", "성별 선택됨: " + studentGender);
                }
            }
        });

        //회원 중복 체크 버튼
        final Button validateButton = (Button) findViewById(R.id.validateButton);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentID = idText.getText().toString().trim(); // trim() 추가
                Log.d("DEBUG", "중복체크 버튼 클릭 - studentID: [" + studentID + "]");

                if (validate) {
                    Log.d("DEBUG", "이미 검증된 상태");
                    return;
                }

                if (studentID.equals("")) {
                    Log.d("DEBUG", "아이디가 비어있음");
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    AlertDialog dialog = builder.setMessage("아이디를 입력하세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                Log.d("DEBUG", "ValidateRequest 생성 및 전송");

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("DEBUG", "서버 응답 받음: " + s);
                        try {
                            JSONObject jsonResponse = new JSONObject(s);
                            boolean success = jsonResponse.getBoolean("success");
                            Log.d("DEBUG", "파싱 결과 - success: " + success);

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
                                Log.d("DEBUG", "검증 완료 처리됨");
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                AlertDialog dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                Log.d("DEBUG", "사용 불가능한 아이디");
                            }
                        } catch (Exception e) {
                            Log.e("DEBUG", "JSON 파싱 오류: " + e.getMessage());
                            e.printStackTrace();

                            // 파싱 오류 시 사용자에게 알림
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            AlertDialog dialog = builder.setMessage("서버 응답 오류가 발생했습니다.")
                                    .setPositiveButton("확인", null)
                                    .create();
                            dialog.show();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DEBUG", "네트워크 오류 발생");
                        Log.e("DEBUG", "오류 메시지: " + error.getMessage());

                        if (error.networkResponse != null) {
                            Log.e("DEBUG", "HTTP 상태 코드: " + error.networkResponse.statusCode);
                            Log.e("DEBUG", "응답 데이터: " + new String(error.networkResponse.data));
                        }

                        // 네트워크 오류 시 사용자에게 알림
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        AlertDialog dialog = builder.setMessage("네트워크 연결을 확인해주세요.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                    }
                };

                ValidateRequest validateRequest = new ValidateRequest(studentID, responseListener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);

                Log.d("DEBUG", "요청이 큐에 추가됨");
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentID = idText.getText().toString().trim();
                studentPassword = passwordText.getText().toString().trim();
                studentEmail = emailText.getText().toString().trim();
                studentMajor = spinner.getSelectedItem().toString();

                Log.d("DEBUG", "회원가입 시도");
                Log.d("DEBUG", "ID: " + studentID);
                Log.d("DEBUG", "Password: " + (studentPassword.isEmpty() ? "비어있음" : "입력됨"));
                Log.d("DEBUG", "Email: " + studentEmail);
                Log.d("DEBUG", "Major: " + studentMajor);
                Log.d("DEBUG", "Gender: " + studentGender);
                Log.d("DEBUG", "Validate: " + validate);

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
                    Log.d("DEBUG", "빈 칸이 있어서 회원가입 실패");
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("DEBUG", "회원가입 서버 응답: " + s);
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
                                Log.d("DEBUG", "회원가입 성공");
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                AlertDialog dialog = builder.setMessage("회원 등록에 실패했습니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                Log.d("DEBUG", "회원가입 실패");
                            }
                        } catch (Exception e) {
                            Log.e("DEBUG", "회원가입 JSON 파싱 오류: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DEBUG", "회원가입 네트워크 오류: " + error.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        AlertDialog dialog = builder.setMessage("네트워크 오류로 회원가입에 실패했습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(studentID, studentPassword, studentGender, studentMajor, studentEmail, responseListener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);

                Log.d("DEBUG", "회원가입 요청 전송됨");
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