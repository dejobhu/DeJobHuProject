package com.dejobhu.skhu.dejobhu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.io.InputStream;
import java.util.ArrayList;

public class AddQustion extends AppCompatActivity {

    LinearLayout view;
    ArrayList<View> viewArrayList=new ArrayList<>();
    ArrayList<String> keyArrayList=new ArrayList<>();
    ImageView SelectImageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qustion);



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
        //---------------------------------------------------------------------------------------------------------


        BoomMenuButton bmm=findViewById(R.id.bmb);

            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(android.R.drawable.ic_menu_save)
                    .normalTextRes(R.string.edittext)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if(viewArrayList.size()>0) {
                                View beforeview = viewArrayList.get(viewArrayList.size() - 1);
                                beforeview.findViewById(R.id.question_btn).setVisibility(View.GONE);
                            }
                            View v=getLayoutInflater().inflate(R.layout.question_edit,null);
                            v.findViewById(R.id.question_btn).setOnClickListener(listener);
                            viewArrayList.add(v);
                            keyArrayList.add("text");
                            view.addView(v);
                        }
                    });
            bmm.addBuilder(builder);

            builder = new HamButton.Builder()
                .normalImageRes(android.R.drawable.ic_menu_save)
                .normalTextRes(R.string.image)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                            if(viewArrayList.size()>0) {
                            View beforeview = viewArrayList.get(viewArrayList.size() - 1);
                            beforeview.findViewById(R.id.question_btn).setVisibility(View.GONE);
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

    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            view.removeView(viewArrayList.get(viewArrayList.size()-1));
            viewArrayList.remove(viewArrayList.size()-1);
            keyArrayList.remove(keyArrayList.size()-1);

            if(viewArrayList.size()>0) {
                View v1 = viewArrayList.get(viewArrayList.size() - 1);
                v1.findViewById(R.id.question_btn).setVisibility(View.VISIBLE);
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
}
