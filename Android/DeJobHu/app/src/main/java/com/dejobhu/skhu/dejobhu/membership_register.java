package com.dejobhu.skhu.dejobhu;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class membership_register extends AppCompatActivity {
    public GetJoson getJoson = GetJoson.getInstance();
    boolean isValidTest = false;
    boolean isValidTestEmail = false;
    boolean isAuthEmail = false;
    private static final int authAct = 101;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_register);


        //----------------------Toobar Setting---------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.register_toolbar);

       // Button Appbar_My=toolbar.findViewById(R.id.register_appbar_btn_1);
//        Button Appbar_SNS=toolbar.findViewById(R.id.appbar_btn_2);
        //툴바안 글씨 색깔 설정
       // Appbar_My.setTextColor(Color.rgb(0,0,0));
//        Appbar_SNS.setTextColor(Color.rgb(0,0,0));
        //Appbar_My.setTextSize(18.5f);
//        Appbar_SNS.setTextSize(18.5f);
//        //버튼 리스너
        //view=findViewById(R.id.Add_Question_view);
//        Appbar_SNS.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainFormActivity.this,"SNS test",Toast.LENGTH_LONG).show();
//            }
//        });

        setSupportActionBar(toolbar);
        //기본 타이틀바 없애기
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //---------------------------------------------------------------------------------------------------------


        final TextView textView_ErrorNick = (TextView)findViewById(R.id.textView_ErrorNick);
        final TextView textView_ErrorEmail_ID = (TextView)findViewById(R.id.textView_ErrorEmail_ID);
        final TextView textView_ErrorPassword = (TextView)findViewById(R.id.textView_ErrorPass);
        final TextView textView_ErrorPassword2 = (TextView)findViewById(R.id.textView_ErrorPass2);
        final EditText editText_nickName = (EditText)findViewById(R.id.editText_nickName);
        final EditText editText_Email_ID = (EditText)findViewById(R.id.editText_Email_ID);
        final EditText editText_Password = (EditText)findViewById(R.id.editText_Password);
        final EditText editText_Password2 = (EditText)findViewById(R.id.editText_Password2);

        Button button_valid = (Button)findViewById(R.id.button_valid);
        Button button_valid_email = (Button)findViewById(R.id.button_valid_email);
        Button button_signUp = (Button)findViewById(R.id.button_signUp);

        //중간에 입력값이 바뀌면 중복체크를 다시해야 하기 때문임
        editText_Email_ID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isValidTestEmail = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //중간에 입력값이 바뀌면 중복체크를 다시해야 하기 때문임
        editText_nickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isValidTest = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //닉네임 중복체크버튼 구현
        button_valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nickName = editText_nickName.getText().toString();
                if(nickName.equals("")){
                    showMessage("닉네임을 입력해 주세요.");
                }
                else {


                    new Thread() {
                        @Override
                        public void run() {
                            getJoson.requestWebServer("api/userValidByName", validCallbackByName, nickName);

                        }
                    }.start();
                }
            }
        });

        //이메일 중복체크버튼 구현
        button_valid_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = editText_Email_ID.getText().toString();
                if(email.equals("")){
                    showMessage("이메일을 입력해 주세요");
                }
                else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(getApplicationContext(), "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            getJoson.requestWebServer("api/userValidByEmail", validCallbackByEmail, email);
                            if(isValidTestEmail == true){
                                towardToAuthEmail(email);
                            }

                        }
                    }.start();
                }
            }
        });

        //회원가입 버튼 구현
        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Nickname = editText_nickName.getText().toString();
                boolean isRightInput = true;
                if(isEmptyOrWhiteSpace(Nickname)) {
                    textView_ErrorNick.setVisibility(View.VISIBLE);
                    isRightInput = false;
                }
                else {
                    textView_ErrorNick.setVisibility(View.INVISIBLE);
                    if(Nickname.length() < 2 || Nickname.length() > 10) {
                        editText_nickName.setError("2-10자로 입력해주세요");
                        isRightInput = false;
                    }
                }

                final String Email_ID = editText_Email_ID.getText().toString();
//                if(isEmptyOrWhiteSpace(Email_ID)) {
//                    textView_ErrorEmail_ID.setVisibility(View.VISIBLE);
//                    isRightInput = false;
//                }
//                else {
//                    if(Email_ID.contains("@"))
//                        textView_ErrorEmail_ID.setVisibility(View.INVISIBLE);
//                    else {
//                        editText_Email_ID.setError("이메일 형식으로 입력해주세요");
//                        isRightInput = false;
//                    }
//                        //@를 포함했는지 안했는지 추가해야됨
//                }
                String Password = editText_Password.getText().toString();
                if(isEmptyOrWhiteSpace(Password)) {
                    textView_ErrorPassword.setVisibility(View.VISIBLE);
                    isRightInput = false;
                }
                else {
                    textView_ErrorPassword.setVisibility(View.INVISIBLE);
                    if(Password.length() < 8 || Password.length() > 20) {
                        editText_Password.setError("8-20자로 입력해주세요");
                        isRightInput = false;
                    }
                }

                final String Password2 = editText_Password2.getText().toString();
                if(isEmptyOrWhiteSpace(Password2)) {
                    textView_ErrorPassword2.setVisibility(View.VISIBLE);
                    isRightInput = false;
                }
                else {
                    textView_ErrorPassword2.setVisibility(View.INVISIBLE);
                    if(!Password.equals(Password2)) {
                        editText_Password2.setError("비밀번호와 같지 않습니다");
                        isRightInput = false;
                    }
                }

                if(isRightInput){
                    if(isValidTest == false || isAuthEmail == false){
                        if(isValidTest == false)
                            Toast.makeText(getApplicationContext(), "중복체크를 해주세요.", Toast.LENGTH_SHORT).show();
                        if(isAuthEmail == false)
                            Toast.makeText(getApplicationContext(), "이메일 인증을 해주세요.", Toast.LENGTH_SHORT).show();

                    }
                    else {

                        new Thread() {
                            @Override
                            public void run() {
                                getJoson.requestWebServer("api/userStore", callback, Nickname, Email_ID, Password2);

                            }
                        }.start();
                        goToMain(Nickname);
                    }
                }
            }
        });


    }

    //닉네임 중복확인 requestWebServer에 대한 콜백함수
    private Callback validCallbackByName = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            String res = response.body().string();
            try{
                Log.d("goodCheck", res);
                JSONObject jsonObject = new JSONObject(res);

                if(jsonObject.getString("result").equals("NG")) {
                    membership_register.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("nickInValid", "닉네임 중복체크 완료.");
                            //Handle UI here
//                            Toast.makeText(getApplicationContext(), "닉네임 중복 체크 완료",
//                                    Toast.LENGTH_LONG).show();
                            showMessage("닉네임 중복체크 완료.");
                            isValidTest = true;
                        }
                    });

                }

                else{
                    membership_register.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("nickValid", "닉네임 중복됩니다.");
                            //Handle UI here
//                            Toast.makeText(getApplicationContext(), "닉네임이 중복됩니다. 다시 입력해 주세요.",
//                                    Toast.LENGTH_LONG).show();
                            showMessage("닉네임이 중복됩니다.");
                        }
                    });
                    isValidTest = false;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };
    //이메일 인증 Activity에서 날아온 Intent를 분석하기 위한 함수

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(data.getStringExtra("Success").equals("true"))
                isAuthEmail = true;
        }

    }

    //이메일 중복확인 requestWebServer 함수에 대한 콜백함수
   private Callback validCallbackByEmail = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            String res = response.body().string();
            try{
                Log.d("goodCheck", res);
                JSONObject jsonObject = new JSONObject(res); //한줄이니까 JSONObject로 받음. 만약 배열이면 JSONArray로
                //받은 결과값이 NG이면
                if(jsonObject.getString("result").equals("NG")){
                    membership_register.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("emailInValid", "이메일 중복체크 완료");
                            //Handle UI here
//                            Toast.makeText(getApplicationContext(), "이메일 중복 체크 완료",
//                                    Toast.LENGTH_LONG).show();
//                            showMessage("이메일 중복체크 완료");
                        }
                    });
                    isValidTestEmail = true;



                }
                //받은결과가 NG가 아닐경우
                else{
                    membership_register.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("emailValid", "이메일 중복됩니다.");
                            //Handle UI here
//                            Toast.makeText(getApplicationContext(), "이메일이 중복됩니다. 다시 입력해 주세요.",
//                                    Toast.LENGTH_LONG).show();
                            showMessage("이메일이 중복됩니다.");

                        }
                    });
                    isValidTestEmail = false;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };
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

    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();

            try {
                Log.d("goodInsert", s);
                JSONObject jsonObject = new JSONObject(s);
                final String nickName = jsonObject.getString("name");

//                membership_register.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(membership_register.this, "환영합니다" + nickName + "님", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                membership_register.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intent = new Intent(membership_register.this, MainFormActivity.class);
////계정 세션으로 넘겨주는 것 구현해야함
//
//                        startActivity(intent);
//                    }
//                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    public void towardToAuthEmail(String email){
        Intent intent = new Intent(getApplicationContext(), EmailAuthActivity.class);
        intent.putExtra("email", email);
        startActivityForResult(intent, authAct);
    }

    public void goToMain(String nickName){

        Toast.makeText(membership_register.this, "환영합니다" + nickName + "님", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(membership_register.this, MainFormActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    static boolean isEmptyOrWhiteSpace(String s){
        if (s == null) return true;
        return s.trim().length() == 0;
    }
}
