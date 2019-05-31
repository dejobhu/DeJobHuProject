package com.dejobhu.skhu.dejobhu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ResetPassword extends AppCompatActivity {
    EditText newPWD1;
    EditText newPWD2;
    Button submit;
    GetJoson getJoson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        //TODO:        이메일 인증 완료 되면 이 액티비티로 넘어옴.
        //        새 비밀번호 입력, 새 비밀번호 다시 입력이 필요하고 제출 버튼도 필요.
        //        확인을 누르면 해당 email에 해당하는 비밀번호가 재해싱되어 등록됨.


        submit = (Button)findViewById(R.id.submitButton);

        //제출버튼 온클릭 리스너 설정
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이메일 인증 끝나고 넘어온 이메일을 받음.
                Intent intent = getIntent();
                //이메일
                String email = intent.getStringExtra("email");

                //버튼 눌렀을 때 edittext에서 값 불러읽음.
                newPWD1 = (EditText)findViewById(R.id.newPWDInput1);
                newPWD2 = (EditText)findViewById(R.id.newPWDInput2);
                String pwd1 = newPWD1.getText().toString();
                String pwd2 = newPWD2.getText().toString();
                //첫 번째 패스워드와 두 번째 패스워드의 유효성을 검사한다.
                if(checkValidPass(pwd1) && checkValidPass(pwd2)){
                    //패스워드가 서로 다르면
                    if(!pwd1.equals(pwd2)){
                        newPWD2.setError("두 비밀번호가 다릅니다.");
                    }
                    else{
                        getJoson.requestWebServer("api/user/resetPass", resPassCallback, email, pwd1);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "패스워드 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public boolean checkValidPass(String pwd){
        //패스워드의 길이가 8 이상 20 이하이면
     if(pwd.length()>=8 && pwd.length() <= 20) {
         //트루반환
         if(isEmptyOrWhiteSpace(pwd))
             return false;
         return true;
     }
//     길이가 맞지 않으면 false반환.
     else return false;
    }

    static boolean isEmptyOrWhiteSpace(String s){
        if (s == null) return true;
        return s.trim().length() == 0;
    }

    private Callback resPassCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();
            try {
                JSONObject jsonObject = new JSONObject(s);
                Log.d("패스워드 재설정", "여기까진옴");

                    if(jsonObject.getString("result").equals("1000")){
                        ResetPassword.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "비밀번호 재설정 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        ResetPassword.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "비밀번호 재 설정 성공!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(getApplicationContext(), activity_login.class);
                        startActivity(intent);
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
