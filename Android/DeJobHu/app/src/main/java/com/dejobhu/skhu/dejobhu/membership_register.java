package com.dejobhu.skhu.dejobhu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

public class membership_register extends AppCompatActivity {
    TextView textView_findId = (TextView)findViewById(R.id.textView_findID);
    TextView textView_membership = (TextView)findViewById(R.id.textView_membership);
    EditText editText_ID = (EditText)findViewById(R.id.editText_ID);
    EditText editText_Password = (EditText)findViewById(R.id.editText_Password);
    Button Button_Login = (Button)findViewById(R.id.Button_Login);
    ImageView imageView_Close = (ImageView)findViewById(R.id.imageView_Close);

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_register);

        SpannableString content1 = new SpannableString("아이디/비밀번호 찾기");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        textView_findId.setText(content1);

        SpannableString content2 = new SpannableString("회원가입");
        content2.setSpan(new UnderlineSpan(), 0, textView_membership.length(), 0);
        textView_membership.setText(content2);
    }

}
