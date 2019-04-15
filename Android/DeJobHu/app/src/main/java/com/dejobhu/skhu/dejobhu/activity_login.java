package com.dejobhu.skhu.dejobhu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class activity_login extends AppCompatActivity {
    GetJoson getJoson = GetJoson.getInstance();
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textView_findId = (TextView)findViewById(R.id.textView_findID);
        TextView textView_membership = (TextView)findViewById(R.id.textView_membership);
        final TextView textView_ErrorEmail_ID = (TextView)findViewById(R.id.textView_ErrorEmail_ID);
        final TextView textView_ErrorPassword = (TextView)findViewById(R.id.textView_ErrorPass);
        final EditText editText_Email_ID = (EditText)findViewById(R.id.editText_Email_ID);
        final EditText editText_Password = (EditText)findViewById(R.id.editText_Password);
        Button button_login = (Button)findViewById(R.id.button_login);


        textView_membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity_login.this,membership_register.class);
                startActivity(intent);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email_ID = editText_Email_ID.getText().toString();
                if(isEmptyOrWhiteSpace(Email_ID))
                    textView_ErrorEmail_ID.setVisibility(View.VISIBLE);
                else {
                    textView_ErrorEmail_ID.setVisibility(View.INVISIBLE);
                }

                final String Password = editText_Password.getText().toString();
                if(isEmptyOrWhiteSpace(Password))
                    textView_ErrorPassword.setVisibility(View.VISIBLE);
                else {
                    textView_ErrorPassword.setVisibility(View.INVISIBLE);
                }

                Log.d("버튼","test");

                new Thread() {
                    public void run() {
// 파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                        getJoson.requestWebServer("api/user", callback, Email_ID, Password);
                    }
                }.start();
                Log.d("버튼","end");
            }
        });



//        imageView_Close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                onBackPressed();
//            }
//        });

        SpannableString content1 = new SpannableString("아이디/비밀번호 찾기");
        content1.setSpan(new UnderlineSpan(), 0, textView_findId.length(), 0);
        textView_findId.setText(content1);

        SpannableString content2 = new SpannableString("회원가입");
        content2.setSpan(new UnderlineSpan(), 0, textView_membership.length(), 0);
        textView_membership.setText(content2);
    }

    static boolean isEmptyOrWhiteSpace(String s){
        if (s == null) return true;
        return s.trim().length() == 0;
    }

    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();

            try {

                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.getString("result").equals("NG")) {

                    //스레드(Main) 안에 스레드(Button 안의 URL통신을 위한 스레드)를 구현했기 때문에 액티비티 UI가 표시되지 않는다. 따라서
                    //main부에서 직접 UI를 띄워주기 위해 runOnUiThread를 사용한다.
                    activity_login.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Handle UI here
                            Toast.makeText(getApplicationContext(), " 해당하는 아이디 혹은 패스워드가 존재하지 않습니다.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
                //만약 result가 NG가 아닌 다른 것이 나온다면 화면전환을 해준다.
                //jsonObject를 이용한 사용자 정보 추출 구현해야함
                else{
                    Intent intent = new Intent(getApplicationContext(), MainFormActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}