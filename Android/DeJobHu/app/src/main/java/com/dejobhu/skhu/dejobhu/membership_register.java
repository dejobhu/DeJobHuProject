package com.dejobhu.skhu.dejobhu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

public class membership_register extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_register);

        TextView textView_findId = (TextView)findViewById(R.id.textView_findID);
        TextView textView_membership = (TextView)findViewById(R.id.textView_membership);
        final EditText editText_ID = (EditText)findViewById(R.id.editText_ID);
        final EditText editText_Password = (EditText)findViewById(R.id.editText_Password);
        ImageView imageView_Close = (ImageView)findViewById(R.id.imageView_Close);

        Button Button_Login = (Button)findViewById(R.id.Button_Login);
        Button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginID = editText_ID.getText().toString();
                if(isEmptyOrWhiteSpace(loginID))
                    editText_ID.setError("아이디 또는 이메일 아이디를 입력해주세요");

                String Password = editText_Password.getText().toString();
                if(isEmptyOrWhiteSpace(Password))
                    editText_Password.setError("비밀번호를 입력해주세요");
            }
        });

        imageView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

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

}
