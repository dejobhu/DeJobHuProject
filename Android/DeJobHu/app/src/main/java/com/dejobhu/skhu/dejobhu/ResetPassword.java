package com.dejobhu.skhu.dejobhu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPassword extends AppCompatActivity {
    EditText newPWD1;
    EditText newPWD2;
    Button submit;
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
                //버튼 눌렀을 때 edittext에서 값 불러읽음.
                newPWD1 = (EditText)findViewById(R.id.newPWDInput1);
                newPWD2 = (EditText)findViewById(R.id.newPWDInput2);
                String pwd1 = newPWD1.getText().toString();
                String pwd2 = newPWD2.getText().toString();
                if(checkValidPass(pwd1) && checkValidPass(pwd2)){

                }
                else{
                    Toast.makeText(getApplicationContext(), "패스워드")
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
}
