package com.dejobhu.skhu.dejobhu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class EmailAuthActivity extends AppCompatActivity {

    EditText insertPass;
    EditText timeText;
    Timer timer;
    boolean isOnceClicked = false;
    int authPass;
    int count;
    GetJoson getJoson = GetJoson.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_auth);

        Intent intent = getIntent(); // membership_register로부터 넘어온 인텐트 값 수신
        final String passedEmail = intent.getStringExtra("email");
//        Log.d("넘어온 이메일 : ", passedEmail);
        final Button authButton = (Button)findViewById(R.id.authButton);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isOnceClicked) {
                    timeGoes(300);
                    isOnceClicked = true;
                    Random random = new Random();
                    authPass = random.nextInt(900000) + 100000;
                    Log.d("인증번호 : " , "" + authPass);
                    //int 형을 String으로 바꾸어주어야 하므로.
                    final String paramPass = Integer.toString(authPass);
                    new Thread(){
                        @Override
                        public void run() {
                            getJoson.requestWebServer("mail", mailCallback, passedEmail, paramPass);
                        }
                    };
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
                    Intent intent = new Intent();
                    intent.putExtra("Success", "true");
                    setResult(RESULT_OK, intent);
                    finish();
                }


            }
        });

    }
    private Callback mailCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            EmailAuthActivity.this.runOnUiThread(new Runnable() {
                String s = response.body().string();
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if(jsonObject.getString("result").equals("2000")){
                            EmailAuthActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "성공적으로 메일을 전송했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            EmailAuthActivity.this.runOnUiThread(new Runnable() {
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
                EmailAuthActivity.this.runOnUiThread(new Runnable() {
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
