package com.dejobhu.skhu.dejobhu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SendNote extends AppCompatActivity {
    EditText content;
    GetJoson getJoson = GetJoson.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

//        쪽지보내기 버튼으로부터 전달받은 유저정보 받음.
        Intent intent = getIntent();
        NoteReceiverStat user = (NoteReceiverStat) intent.getSerializableExtra("recvUser");

        //        x버튼 누르면 나가게
        ImageView exitImage = (ImageView) findViewById(R.id.exitImage);
        exitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = (EditText)findViewById(R.id.notecontent);
                String contents = content.getText().toString();

//                내용, 전달자 id, 받는자 id, 시간, 등등을 쪽지 관련 php에 ㅗㅂ냄
//                getJoson.requestWebServer();

            }
        });


    }
    public Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

//            모든 작업이 끝나면 레이아웃 종료.
            finish();
        }
    };
}
