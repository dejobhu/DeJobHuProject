package com.dejobhu.skhu.dejobhu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejobhu.skhu.dejobhu.login.SaveSharedPreference;

public class MyPage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        ((RelativeLayout)findViewById(R.id.pwd_rl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingPassword.class);
                startActivity(intent);

            }
        });

        ((RelativeLayout)findViewById(R.id.email_rl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ModifyName.class);
                startActivity(intent);
            }
        });
        ((RelativeLayout)findViewById(R.id.Logout_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreference.clearUserEmail(MyPage.this);
                Intent i = new Intent(getApplicationContext(), activity_login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        ((RelativeLayout)findViewById(R.id.secession)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                정말로 탈퇴하시겠습니까 ? 다이얼로그
//                if 예{
//                requestweb , email
//                  }
//
            }
        });




    }
}
