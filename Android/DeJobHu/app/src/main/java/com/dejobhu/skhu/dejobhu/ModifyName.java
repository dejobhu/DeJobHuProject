package com.dejobhu.skhu.dejobhu;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;
import com.dejobhu.skhu.dejobhu.Singleton.Userinfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ModifyName extends AppCompatActivity {

    GetJoson getJoson = GetJoson.getInstance();
    EditText nameText;
    boolean isChecked;
    boolean isPassed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_name);

        nameText = (EditText)findViewById(R.id.editText);
        ((Button)findViewById(R.id.checkButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPassed = false;
                isChecked = false;
                final String name = nameText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getJoson.requestWebServer("api/userValidByName", new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String s = response.body().string();
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    if(jsonObject.getString("result").equals("2000")){
                                        ModifyName.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "닉네임이 중복됩니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    else{
                                        ModifyName.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "닉네임이 중복체크 완료.", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                        isChecked = true;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                isPassed = true;
                            }
                        }, name);
                    }
                }).start();
                while(isPassed == false){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });



        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChecked = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((Button)findViewById(R.id.submitButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isChecked){
                    showMessage("중복체크를 진행해주세요.");
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getJoson.requestWebServer("api/user/modifyName", new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String s = response.body().string();
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    if(jsonObject.getString("result").equals("2000")){
                                        ModifyName.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "성공적으로 닉네임이 변경되었습니다.",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        Userinfo.shared.setName(nameText.getText().toString());
                                        finish();
                                    }
                                    else{
                                        ModifyName.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "닉네임 변경 실패",Toast.LENGTH_LONG).show();

                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, String.valueOf(Userinfo.shared.getId()), nameText.getText().toString());
                    }
                }).start();
            }
        });
    }
    public void showMessage(String message){
        AlertDialog alertDialog;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ModifyName.this);
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
}
