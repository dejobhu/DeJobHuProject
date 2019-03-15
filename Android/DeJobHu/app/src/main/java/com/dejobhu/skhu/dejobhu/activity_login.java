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

public class activity_login extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textView_findId = (TextView)findViewById(R.id.textView_findID);
        TextView textView_membership = (TextView)findViewById(R.id.textView_membership);
        final TextView textView_ErrorEmail_ID = (TextView)findViewById(R.id.textView_ErrorEmail_ID);
        final TextView textView_ErrorPassword = (TextView)findViewById(R.id.textView_ErrorPass);
        final EditText editText_Email_ID = (EditText)findViewById(R.id.editText_Email_ID);
        final EditText editText_Password = (EditText)findViewById(R.id.editText_Password);
        ImageView imageView_Close = (ImageView)findViewById(R.id.imageView_Close);
        Button button_login = (Button)findViewById(R.id.button_login);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email_ID = editText_Email_ID.getText().toString();
                if(isEmptyOrWhiteSpace(Email_ID))
                    textView_ErrorEmail_ID.setVisibility(View.VISIBLE);
                else {
                    textView_ErrorEmail_ID.setVisibility(View.INVISIBLE);
                    return;
                }

                String Password = editText_Password.getText().toString();
                if(isEmptyOrWhiteSpace(Password))
                    textView_ErrorPassword.setVisibility(View.VISIBLE);
                else {
                    textView_ErrorPassword.setVisibility(View.INVISIBLE);
                    return;
                }

                Intent intent=new Intent(activity_login.this,MainFormActivity.class);
                startActivity(intent);
                finish();
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

}