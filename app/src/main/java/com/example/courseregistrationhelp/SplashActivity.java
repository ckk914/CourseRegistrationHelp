// SplashActivity.java
package com.example.courseregistrationhelp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 스플래시 화면 노출 시간 (2초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 스플래시 레이아웃 설정
        setContentView(R.layout.activity_splash);

        // 2초 후에 메인 액티비티로 이동
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish(); // 스플래시 액티비티 종료
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
