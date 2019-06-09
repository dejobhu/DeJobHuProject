package com.dejobhu.skhu.dejobhu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;
import com.dejobhu.skhu.dejobhu.Singleton.Userinfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SendNote extends AppCompatActivity {
    public Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            SendNote.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SendNote.this, "서버와의 통신이 되지 않습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("통신실패","");
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();
            Log.d("응답", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.getString("result").equals("NG")){
                    SendNote.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SendNote.this, "쪽지 보내기 실패", Toast.LENGTH_SHORT).show();
                            Log.d("통신성공","메소드 실패");

                        }
                    });
                }
                else{
                    JSONObject data = jsonObject.getJSONObject("data");
                    int recv_id = data.getInt("recv_id");
                    SendNote.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "쪽지를 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("통신성공","메소드 성공");

                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            모든 작업이 끝나면 레이아웃 종료.
            finish();
        }
    };
    EditText content;
    GetJoson getJoson = GetJoson.getInstance();
    int recv_id;
    int send_id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

//        쪽지보내기 버튼으로부터 전달받은 유저정보 받음.
        Intent intent = getIntent();
        NoteReceiverStat user = (NoteReceiverStat) intent.getSerializableExtra("recvUser");
        recv_id = user.getId();
        send_id = Userinfo.shared.getId();
//        Log.d("수신자 : ",recv_id+"");
//        Log.d("송신자 : ",send_id+"");

        //        x버튼 누르면 나가게
        ImageView exitImage = (ImageView) findViewById(R.id.exitImage);
        exitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = (EditText) findViewById(R.id.notecontent);
                final String contents = content.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //내용, 전달자 id, 받는자 id, 시간, 등등을 쪽지 관련 php에 ㅗㅂ냄
                        //getJoson.PostRequest("");
                        HashMap<String, String> hash = new HashMap<>();
                        hash.put("recv_id", "" + recv_id);
                        hash.put("send_id", "" + send_id);
                        hash.put("content", contents);
                        Log.d("hash", hash.toString());
                        getJoson.PostRequest("api/note/sendNote", callback, hash);
                    }
                }).start();


            }
        });


    }
}
