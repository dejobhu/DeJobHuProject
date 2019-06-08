package com.dejobhu.skhu.dejobhu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;
import com.dejobhu.skhu.dejobhu.Singleton.Userinfo;
import com.github.akshay_naik.texthighlighterapi.TextHighlighter;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddQustion extends AppCompatActivity {

    LinearLayout view;
    ArrayList<View> viewArrayList=new ArrayList<>();
    ArrayList<String> keyArrayList=new ArrayList<>();
    ImageView SelectImageview;
    TextView title;
    TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qustion);


        title=findViewById(R.id.Add_Question_Title);
        content=findViewById(R.id.question_first_content);

        //----------------------Toobar Setting---------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_toolbar);

        Button Appbar_My=toolbar.findViewById(R.id.add_appbar_btn_1);
//        Button Appbar_SNS=toolbar.findViewById(R.id.appbar_btn_2);
        //툴바안 글씨 색깔 설정
        Appbar_My.setTextColor(Color.rgb(0,0,0));
//        Appbar_SNS.setTextColor(Color.rgb(0,0,0));
        Appbar_My.setTextSize(18.5f);
//        Appbar_SNS.setTextSize(18.5f);
//        //버튼 리스너
        Appbar_My.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddQustion.this,"My test",Toast.LENGTH_LONG).show();
            }
        });
        view=findViewById(R.id.Add_Question_view);
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


        BoomMenuButton bmm=findViewById(R.id.bmb);

            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(R.drawable.ic_subject_black_24dp)
                    .normalTextRes(R.string.edittext)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if(viewArrayList.size()>0) {
                                View beforeview = viewArrayList.get(viewArrayList.size() - 1);
                                beforeview.findViewById(R.id.question_btn).setVisibility(View.GONE); // 전에 올렸던 삭제버튼 안보이게하기

                                try{
                                    beforeview.findViewById(R.id.question_JavaORText_btn).setVisibility(View.GONE); //전에 올렸던 java text 버튼을 안보이게 하기
                                }catch (NullPointerException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            View v=getLayoutInflater().inflate(R.layout.question_edit,null);// 새롭게 넣을 Content
                            v.findViewById(R.id.question_btn).setOnClickListener(listener);
                            v.findViewById(R.id.question_JavaORText_btn).setOnClickListener(Javalistener);

                            viewArrayList.add(v); //추가할 View를 arrayList에 저장
                            keyArrayList.add("text");//추가할 View의 타입을 arrayList에 저장
                            view.addView(v);
                        }
                    });
            bmm.addBuilder(builder);

            builder = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_photo_size_select_actual_black_24dp)
                .normalTextRes(R.string.image)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                            if(viewArrayList.size()>0) { //이미 1개가 추가로 저장된 상태

                                //위와 동일
                            View beforeview = viewArrayList.get(viewArrayList.size() - 1);
                            beforeview.findViewById(R.id.question_btn).setVisibility(View.GONE);
                                try{
                                    beforeview.findViewById(R.id.question_JavaORText_btn).setVisibility(View.GONE);
                                }catch (NullPointerException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            View v=getLayoutInflater().inflate(R.layout.question_image,null);
                            v.findViewById(R.id.question_btn).setOnClickListener(listener);
                            v.findViewById(R.id.question_image).setOnClickListener(ImageListener);
                            viewArrayList.add(v);
                            keyArrayList.add("image");
                            view.addView(v);
                    }
                });
        bmm.addBuilder(builder);

        findViewById(R.id.add_appbar_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        GetJoson joson=GetJoson.getInstance();

                        HashMap<String,String> hash=new HashMap<>();
                        hash.put("id",""+ Userinfo.shared.getId());
                        hash.put("title",title.getText().toString());
                        hash.put("text0",content.getText().toString());
                        hash.put("last",""+keyArrayList.size()+1);
                        for(int i=0;i<keyArrayList.size();i++)
                        {
                            String key=keyArrayList.get(i).toString();

                            View view=viewArrayList.get(i);
                            if(key.equals("text")) {
                                TextView textView=view.findViewById(R.id.question_edit_text);
                                hash.put(key + (i+1), textView.getText().toString());
                            }else{//key => image
                                ImageView imageView=view.findViewById(R.id.question_image);
                                hash.put(key+(i+1),getStringImage(((BitmapDrawable)imageView.getDrawable()).getBitmap()));
                            }
                        }
                        Log.d("hash", hash.toString());
                        joson.PostRequest("api/post/upPost",callback,hash);
                    }
                }.start();
            }
        });
    }



    View.OnClickListener Javalistener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {//이 버튼은 눌렀을 때마다 JAVA와 Text가 토글됨

            if(((Button)v).getText().toString().compareTo("Java")==0) { //자바라면 자바형식으로 저장
                TextHighlighter highlighter = new TextHighlighter();
                highlighter.setLanguage(TextHighlighter.JAVA);

                String highlightedText = highlighter.getHighlightedText(((EditText)viewArrayList.get(viewArrayList.size()-1).findViewById(R.id.question_edit_text)).getText().toString());
                ((EditText) viewArrayList.get(viewArrayList.size() - 1).findViewById(R.id.question_edit_text)).setText(Html.fromHtml(highlightedText));
                ((Button)v).setText("Text");
            }else
            {
                String s= ((EditText) viewArrayList.get(viewArrayList.size() - 1).findViewById(R.id.question_edit_text)).getText().toString();
                ((EditText) viewArrayList.get(viewArrayList.size() - 1).findViewById(R.id.question_edit_text)).setText(s);
                ((Button)v).setText("Java");
            }
        }
    };

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) { //삭제
            view.removeView(viewArrayList.get(viewArrayList.size()-1));
            viewArrayList.remove(viewArrayList.size()-1);
            keyArrayList.remove(keyArrayList.size()-1);

            if(viewArrayList.size()>0) {
                View v1 = viewArrayList.get(viewArrayList.size() - 1);
                v1.findViewById(R.id.question_btn).setVisibility(View.VISIBLE);

                try{
                    v1.findViewById(R.id.question_JavaORText_btn).setVisibility(View.VISIBLE);
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                }
            }
        }
    };

    View.OnClickListener ImageListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            SelectImageview=(ImageView)v;

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        }
    };

    private Callback callback=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            AddQustion.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddQustion.this,"서버와의 연결이 불안정합니다.",Toast.LENGTH_LONG).show();
                }
            });

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            String s=response.body().string();
            Log.d("body",s);
            try {
                final JSONObject object=new JSONObject(s);

                if(object.getString("result").equals("OK"))
                {
                    AddQustion.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddQustion.this,"업로드에 성공하였습니다.",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }else
                {
                    AddQustion.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(AddQustion.this,object.getString("data"),Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                Toast.makeText(AddQustion.this,"서버 통신에 실패하였습니다. 관리자에게 문의해주세요",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    if(SelectImageview !=null);
                    SelectImageview.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] imageBytes =baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }
}
