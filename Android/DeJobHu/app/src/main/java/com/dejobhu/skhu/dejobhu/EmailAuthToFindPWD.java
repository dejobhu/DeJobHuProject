package com.dejobhu.skhu.dejobhu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EmailAuthToFindPWD extends AppCompatActivity {
    EditText insertPass;
    EditText timeText;
    EditText inputEmail;
    Timer timer;
    boolean isOnceClicked = false;
    String email;
    int authPass;
    Thread checkMailThread;
    int count;
    GetJoson getJoson = GetJoson.getInstance();
    boolean mailCheck ;
    public static Activity _emailAuthToFindPWD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_auth_to_find_pwd);
        _emailAuthToFindPWD = EmailAuthToFindPWD.this;

//        Intent intent = getIntent(); // membership_register로부터 넘어온 인텐트 값 수신
//        final String passedEmail = intent.getStringExtra("email");   // 이메일을 직접 입력하므로
//        Log.d("넘어온 이메일 : ", passedEmail);

        final Button authButton = (Button)findViewById(R.id.authButton);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEmail = (EditText)findViewById(R.id.emailText);
                email = inputEmail.getText().toString();
                //이메일이 디비에 있어야하므로 확인하는 절차를 만든다.

//                새 스레드를 추가하면 안됨(확인 하고 가입시켜야하니까.... 그럼 다 바꿔야되지 않나? 확인하는 작업이니까 그런건가
                checkMailThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getJoson.requestWebServer("api/userValidByEmail", mailCheckCallback, email);
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                checkMailThread.start();

//                메일이 존재하는지 확인하는 스레드를 끝내고 다음 작업을 처리하기 위해.
                try {
                    checkMailThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("현재 mailCheck 상태 onClick", Boolean.toString(mailCheck));

                if(!mailCheck) {
                    showMessage("해당하는 메일을 확인할 수 없습니다.");
                }
                else {
                    if (inputEmail != null) {
                        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {   // 이메일이 유효성검사를 통과하는지 확인.

                            if (!isOnceClicked) {


                                timeGoes(300);
                                isOnceClicked = true;
                                Random random = new Random();
                                authPass = random.nextInt(900000) + 100000;
                                final String paramPass = Integer.toString(authPass);
                                Log.d("인증번호 : ", "" + authPass);
                                new Thread() {
                                    public void run() {
                                        getJoson.requestWebServer("mail", mailCallback, email, paramPass);
                                    }
                                }.start();


//                    getJoson.requestWebServer("api/sendMail", mailCallback, passedEmail, authPass);
//                    TODO: 이메일을 PHP단에 전송하여 메일 인증 시스템 구현하기.
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "올바른 이메일을 입력해 주세요.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "이메일을 입력 안하셨습니다.", Toast.LENGTH_LONG).show();
                    }
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
                    intent.putExtra("email", email);
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
    public void showMessage(String message){
        AlertDialog alertDialog;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("알림");
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton("예", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("YES", "예 버튼이 눌렸습니다.");
            }
        });

        alertDialog = alertBuilder.create();
        alertDialog.show();
    }
    //메일이 존재하는지를 확인하기 위한 콜백함수
    private Callback mailCheckCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();

            try {
                JSONObject jsonObject = new JSONObject(s);
                //해당하는 이메일이 존재한다면
                if(jsonObject.getString("result").equals("2000")){
                    mailCheck = true;
                }
                else{
                    mailCheck = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("현재 mailCheck Callback ", Boolean.toString(mailCheck));

        }
    };

//    메일을 보내기 위한 콜백함수
    private Callback mailCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("실패", "실패함");
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            EmailAuthToFindPWD.this.runOnUiThread(new Runnable() {
                String s = response.body().string();

                @Override
                public void run() {
                    Log.d("String 값은", s);
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if(jsonObject.getString("result").equals("2000")){
                            EmailAuthToFindPWD.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "성공적으로 메일을 전송했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            EmailAuthToFindPWD.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "메일 전송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

}
