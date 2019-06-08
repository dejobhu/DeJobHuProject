package com.dejobhu.skhu.dejobhu.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dejobhu.skhu.dejobhu.MainFormActivity;
import com.dejobhu.skhu.dejobhu.R;
import com.dejobhu.skhu.dejobhu.Singleton.Userinfo;
import com.dejobhu.skhu.dejobhu.activity_login;

// 맨 처음 실행했을 때 레이아웃을 이걸로 두어 자동로그인의 기능을 수행한다.

public class FirstAuthActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_auth);

//        만약 SaveSharedPreference 객체의 getUserName의 길이가 0이면, 즉 저장된 내용이 없다면 (Context객체는 왜 넣는건지? 이 액티비티에만 저장되는건가)
        if (SaveSharedPreference.getUserEmail(FirstAuthActivity.this).length() == 0) {
//            call Login Activity
//            로그인 액티비티로 진행됨
            intent = new Intent(FirstAuthActivity.this, activity_login.class);
            startActivity(intent);
            this.finish();
        } else {
//            Call Next Activity
//            자동로그인이라면, 바로 로그인이되도록.

            intent = new Intent(FirstAuthActivity.this, MainFormActivity.class);
            intent.putExtra("SSP_EMAIL", SaveSharedPreference.getUserEmail(this).toString());
//            Log.d("로그", "자동로그인입니다." + SaveSharedPreference.getUserEmail(this).toString()); 잘 옴
//            todo:내일까지 이 인텐트 제대로 전달받는지, 전달받았다면 userinfo로 적절히 전달되는지 확인할것.
            startActivity(intent);
            this.finish();
        }
    }
}
