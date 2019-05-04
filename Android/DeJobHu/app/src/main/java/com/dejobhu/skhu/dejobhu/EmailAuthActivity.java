package com.dejobhu.skhu.dejobhu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class EmailAuthActivity extends AppCompatActivity {

    EditText insertPass;
    boolean isOnceClicked = false;
    int authPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_auth);


        final Button authButton = (Button)findViewById(R.id.authButton);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isOnceClicked) {
                    isOnceClicked = true;
                    Random random = new Random();
                    authPass = random.nextInt(900000) + 100000;
                    Log.d("인증번호 : " , "" + authPass);

                }
            }
        });

        insertPass = (EditText)findViewById(R.id.insertPass);

        Button sendButton = (Button)findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String randPass = authPass + "";
                String userPass = insertPass.getText().toString();
                if(userPass.equals(randPass)){
                    Intent intent = new Intent();
                    intent.putExtra("Success", "true");
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });
    }


}
