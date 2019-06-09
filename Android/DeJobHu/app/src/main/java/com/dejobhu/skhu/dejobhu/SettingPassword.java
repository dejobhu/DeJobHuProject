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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;
import com.dejobhu.skhu.dejobhu.Singleton.Userinfo;
import com.dejobhu.skhu.dejobhu.login.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SettingPassword extends AppCompatActivity {
    EditText newPWD1;
    EditText newPWD2;
    Button submit;
    Thread cmpThread;
    boolean isPassed;
    boolean checkOriginPass;
    GetJoson getJoson = GetJoson.getInstance();
    boolean passSame;
    //    비밀번호 비교 requestWebServer에 대한 콜백함수
    private Callback cmpPassCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();
            try {
                JSONObject jsonObject = new JSONObject(s);
//                만약 비밀번호가 서로 같다면
                if (jsonObject.getString("result").equals("2000")) {
                    passSame = true;

                }
//                비밀번호가 서로 다르면
                else {
                    passSame = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
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

                if (jsonObject.getString("result").equals("1000")) {
                    SettingPassword.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "비밀번호 재설정 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    SettingPassword.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "비밀번호 재 설정 성공!, 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                        }

                    });
                    SaveSharedPreference.clearUserEmail(SettingPassword.this);
                    Intent i = new Intent(getApplicationContext(), activity_login.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    static boolean isEmptyOrWhiteSpace(String s) {
        if (s == null) return true;
        return s.trim().length() == 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_reset_password);


        submit = (Button) findViewById(R.id.submitButton);

        //제출버튼 온클릭 리스너 설정
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPassed = false;
                checkOriginPass = false;
                final String email = Userinfo.shared.getEmail();
                final String originPass = ((EditText) findViewById(R.id.originPass)).getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getJoson.requestWebServer("api/user", new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String s = response.body().string();
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    if (jsonObject.getString("result").equals("1000")) {
                                        SettingPassword.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showMessage("기존 비밀번호가 틀립니다.");
                                            }
                                        });
                                    } else {
                                        checkOriginPass = true;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                isPassed = true;

                            }
                        }, email, originPass);
                    }
                }).start();
                while (isPassed == false) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //버튼 눌렀을 때 edittext에서 값 불러읽음.
                newPWD1 = (EditText) findViewById(R.id.newPWDInput1);
                newPWD2 = (EditText) findViewById(R.id.newPWDInput2);
                final String pwd1 = newPWD1.getText().toString();
                String pwd2 = newPWD2.getText().toString();
                //첫 번째 패스워드와 두 번째 패스워드의 유효성을 검사한다.
                if (checkValidPass(pwd1) && checkValidPass(pwd2)) {
                    //패스워드가 서로 다르면
                    if (!pwd1.equals(pwd2)) {
                        newPWD2.setError("두 비밀번호가 다릅니다.");
                    }
//                    패스워드가 서로 같다면
                    else {
                        if (checkOriginPass == false)
                            return;
//                        먼저, 입력한 패스워드가 서버에 등록되어있는 패스워드와 같다면 바꿀 필요가 없으므로, 다른 비밀번호를 입력하라고 띄운다.
                        cmpThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getJoson.requestWebServer("api/user/comparePass", cmpPassCallback, email, pwd1);
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        cmpThread.start();

//                        서버에 등록되어있는 비밀번호와 비교하는 작업이 다 끝나고 나서 그 다음 작업이 진행되게
                        try {
                            cmpThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (passSame) {
                            showMessage("기존과 다른 비밀번호를 입력해 주세요.");
                        } else {
                            Log.d("Point", "여기");
                            new Thread() {
                                public void run() {
                                    Log.d("Point", "여기3");
                                    getJoson.requestWebServer("api/user/resetPass", resPassCallback, email, pwd1);
                                }
                            }.start();
                            Log.d("Point", "여기2");

                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "패스워드 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public boolean checkValidPass(String pwd) {
        //패스워드의 길이가 8 이상 20 이하이면
        if (pwd.length() >= 8 && pwd.length() <= 20) {
            //트루반환
            if (isEmptyOrWhiteSpace(pwd))
                return false;
            return true;
        }
//     길이가 맞지 않으면 false반환.
        else return false;
    }

    public void showMessage(String message) {
        AlertDialog alertDialog;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("알림");
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("YES", "예 버튼이 눌렸습니다.");
            }
        });

        alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
