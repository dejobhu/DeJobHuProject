package com.dejobhu.skhu.dejobhu;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;

public class AddQustion extends AppCompatActivity {

    LinearLayout view;
    ArrayList<View> viewArrayList=new ArrayList<>();
    ArrayList<String> keyArrayList=new ArrayList<>();
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
                            View v=getLayoutInflater().inflate(R.layout.question_edit,null);
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
                            View v=getLayoutInflater().inflate(R.layout.question_image,null);
                            viewArrayList.add(v);
                            keyArrayList.add("image");
                            view.addView(v);
                    }
                });
        bmm.addBuilder(builder);

    }


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
}
