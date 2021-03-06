package com.dejobhu.skhu.dejobhu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    String strNickname, strProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvNickname = findViewById(R.id.tvNickname);
        TextView tvProfile = findViewById(R.id.tvProfile);

        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");
        Log.d("프로필사진", strProfile);
        tvNickname.setText(strNickname);
        tvProfile.setText(strProfile);


        Button login_button = (Button)findViewById(R.id.button_gologin);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_login.class);
                startActivity(intent);
            }
        });

        Button signUp_button = (Button)findViewById(R.id.button_signUp);
        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, membership_register.class);
                startActivity(intent);
            }
        });
    }
}
