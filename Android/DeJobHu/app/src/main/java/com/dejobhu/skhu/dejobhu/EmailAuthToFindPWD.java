package com.dejobhu.skhu.dejobhu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class EmailAuthToFindPWD extends AppCompatActivity {
    EditText insertPass;
    EditText timeText;
    EditText inputEmail;
    Timer timer;
    boolean isOnceClicked = false;
    int authPass;
    int count;
    GetJoson getJoson = GetJoson.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_auth_to_find_pwd);


//        Intent intent = getIntent(); // membership_register로부터 넘어온 인텐트 값 수신
//        final String passedEmail = intent.getStringExtra("email");   // 이메일을 직접 입력하므로
//        Log.d("넘어온 이메일 : ", passedEmail);

        final Button authButton = (Button)findViewById(R.id.authButton);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEmail = (EditText)findViewById(R.id.emailText);
                String email = inputEmail.getText().toString();
                if(inputEmail != null) {
                    if(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {   // 이메일이 유효성검사를 통과하는지 확인.
                        if (!isOnceClicked) {
                            timeGoes(300);
                            isOnceClicked = true;
                            Random random = new Random();
                            authPass = random.nextInt(900000) + 100000;
                            Log.d("인증번호 : ", "" + authPass);

//                    getJoson.requestWebServer("api/sendMail", mailCallback, passedEmail, authPass);
//                    TODO: 이메일을 PHP단에 전송하여 메일 인증 시스템 구현하기.
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "올바른 이메일을 입력해 주세요.", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "이메일을 입력 안하셨습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
        timeText = (EditText)findViewById(R.id.timer);
        insertPass = (EditText)findViewById(R.id.insertPass);

        Button sendButton = (Button)findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String randPass = authPass + "";
                String userPass = insertPass.getText().toString();
                if(count <= 0) {
                    Toast.makeText(getApplicationContext(), "시간이 초과되었습니다. 다시 메일을 인증해주세요.", Toast.LENGTH_LONG).show();
                    isOnceClicked = false;
                    return;
                }
                if(userPass.equals(randPass)){
                    Intent intent = new Intent(getApplicationContext(), ResetPassword.class);
                    intent.putExtra("isEmailAuthed?", "true");
                    startActivity(intent);
                }


            }
        });

    }




    public String expressTime(int time){
        int minute = time / 60;
        int second = time % 60;
        return minute + "분 " + second + "초";
    }

    public void timeGoes(int time){
        count = time;
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                if(count == 0) {
                    timer.cancel();
                    return;
                }
                EmailAuthToFindPWD.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeText.setText(expressTime(count));
                    }
                });
                count--;

            }
        };

        timer= new Timer();
        timer.schedule(tt, 0, 1000);

    }
}
