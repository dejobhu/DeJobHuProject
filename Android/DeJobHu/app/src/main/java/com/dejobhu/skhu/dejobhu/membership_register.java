package com.dejobhu.skhu.dejobhu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class membership_register extends AppCompatActivity {
    public GetJoson getJoson = GetJoson.getInstance();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_register);


        //----------------------Toobar Setting---------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.register_toolbar);

       // Button Appbar_My=toolbar.findViewById(R.id.register_appbar_btn_1);
//        Button Appbar_SNS=toolbar.findViewById(R.id.appbar_btn_2);
        //툴바안 글씨 색깔 설정
       // Appbar_My.setTextColor(Color.rgb(0,0,0));
//        Appbar_SNS.setTextColor(Color.rgb(0,0,0));
        //Appbar_My.setTextSize(18.5f);
//        Appbar_SNS.setTextSize(18.5f);
//        //버튼 리스너
        //view=findViewById(R.id.Add_Question_view);
//        Appbar_SNS.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainFormActivity.this,"SNS test",Toast.LENGTH_LONG).show();
//            }
//        });

        setSupportActionBar(toolbar);
        //기본 타이틀바 없애기
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //---------------------------------------------------------------------------------------------------------


        final TextView textView_ErrorNick = (TextView)findViewById(R.id.textView_ErrorNick);
        final TextView textView_ErrorEmail_ID = (TextView)findViewById(R.id.textView_ErrorEmail_ID);
        final TextView textView_ErrorPassword = (TextView)findViewById(R.id.textView_ErrorPass);
        final TextView textView_ErrorPassword2 = (TextView)findViewById(R.id.textView_ErrorPass2);
        final EditText editText_nickName = (EditText)findViewById(R.id.editText_nickName);
        final EditText editText_Email_ID = (EditText)findViewById(R.id.editText_Email_ID);
        final EditText editText_Password = (EditText)findViewById(R.id.editText_Password);
        final EditText editText_Password2 = (EditText)findViewById(R.id.editText_Password2);
        Button button_signUp = (Button)findViewById(R.id.button_signUp);
        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Nickname = editText_nickName.getText().toString();
                boolean isRightInput = true;
                if(isEmptyOrWhiteSpace(Nickname)) {
                    textView_ErrorNick.setVisibility(View.VISIBLE);
                    isRightInput = false;
                }
                else {
                    textView_ErrorNick.setVisibility(View.INVISIBLE);
                    if(Nickname.length() < 2 || Nickname.length() > 10) {
                        editText_nickName.setError("2-10자로 입력해주세요");
                        isRightInput = false;
                    }
                }

                final String Email_ID = editText_Email_ID.getText().toString();
                if(isEmptyOrWhiteSpace(Email_ID)) {
                    textView_ErrorEmail_ID.setVisibility(View.VISIBLE);
                    isRightInput = false;
                }
                else {
                    if(Email_ID.contains("@"))
                        textView_ErrorEmail_ID.setVisibility(View.INVISIBLE);
                    else {
                        editText_Email_ID.setError("이메일 형식으로 입력해주세요");
                        isRightInput = false;
                    }
                        //@를 포함했는지 안했는지 추가해야됨
                }
                String Password = editText_Password.getText().toString();
                if(isEmptyOrWhiteSpace(Password)) {
                    textView_ErrorPassword.setVisibility(View.VISIBLE);
                    isRightInput = false;
                }
                else {
                    textView_ErrorPassword.setVisibility(View.INVISIBLE);
                    if(Password.length() < 8 || Password.length() > 20) {
                        editText_Password.setError("8-20자로 입력해주세요");
                        isRightInput = false;
                    }
                }

                final String Password2 = editText_Password2.getText().toString();
                if(isEmptyOrWhiteSpace(Password2)) {
                    textView_ErrorPassword2.setVisibility(View.VISIBLE);
                    isRightInput = false;
                }
                else {
                    textView_ErrorPassword2.setVisibility(View.INVISIBLE);
                    if(!Password.equals(Password2)) {
                        editText_Password2.setError("비밀번호와 같지 않습니다");
                        isRightInput = false;
                    }
                }
                if(isRightInput){
                    new Thread(){
                        @Override
                        public void run() {
                            getJoson.requestWebServer("api/userStore", callback, Nickname, Email_ID, Password2);

                        }
                    }.start();
                }
            }
        });


    }

    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();

            try {
                Log.d("GOOD", s);
                JSONObject jsonObject = new JSONObject(s);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    static boolean isEmptyOrWhiteSpace(String s){
        if (s == null) return true;
        return s.trim().length() == 0;
    }
}
