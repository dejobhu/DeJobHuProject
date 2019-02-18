package com.dejobhu.skhu.dejobhu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

public class membership_register extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_register);

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
                String Nickname = editText_nickName.getText().toString();
                if(isEmptyOrWhiteSpace(Nickname))
                    textView_ErrorNick.setVisibility(View.VISIBLE);
                else {
                    textView_ErrorNick.setVisibility(View.INVISIBLE);
                    if(Nickname.length() < 2 || Nickname.length() > 10)
                        editText_nickName.setError("2-10자로 입력해주세요");
                }

                String Email_ID = editText_Email_ID.getText().toString();
                if(isEmptyOrWhiteSpace(Email_ID))
                    textView_ErrorEmail_ID.setVisibility(View.VISIBLE);
                else {
                    if(Email_ID.contains("@"))
                        textView_ErrorEmail_ID.setVisibility(View.INVISIBLE);
                    else
                        editText_Email_ID.setError("이메일 형식으로 입력해주세요");
                    //@를 포함했는지 안했는지 추가해야됨
                }
                String Password = editText_Password.getText().toString();
                if(isEmptyOrWhiteSpace(Password))
                    textView_ErrorPassword.setVisibility(View.VISIBLE);
                else {
                    textView_ErrorPassword.setVisibility(View.INVISIBLE);
                    if(Password.length() < 8 || Password.length() > 20)
                        editText_Password.setError("8-20자로 입력해주세요");
                }

                String Password2 = editText_Password2.getText().toString();
                if(isEmptyOrWhiteSpace(Password2))
                    textView_ErrorPassword2.setVisibility(View.VISIBLE);
                else {
                    textView_ErrorPassword2.setVisibility(View.INVISIBLE);
                    if(Password != Password2)
                        editText_Password2.setError("비밀번호와 같지 않습니다");
                }

            }
        });


        ImageView imageView_Close = (ImageView)findViewById(R.id.imageView_Close);
        imageView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    static boolean isEmptyOrWhiteSpace(String s){
        if (s == null) return true;
        return s.trim().length() == 0;
    }
}
