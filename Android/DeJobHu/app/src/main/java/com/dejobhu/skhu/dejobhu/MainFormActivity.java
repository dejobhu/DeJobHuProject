package com.dejobhu.skhu.dejobhu;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dejobhu.skhu.dejobhu.Fragment.QuestionListFragment;
import com.dejobhu.skhu.dejobhu.Handler.BackPressCloseHandler;
import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;
import com.dejobhu.skhu.dejobhu.Singleton.Userinfo;
import com.dejobhu.skhu.dejobhu.login.FirstAuthActivity;
import com.dejobhu.skhu.dejobhu.login.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainFormActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    GetJoson getJoson = GetJoson.getInstance();
//    뒤로가기 버튼을 담당할 핸들러
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);
//        뒤로가기 버튼 두번누르는거 구현
        backPressCloseHandler = new BackPressCloseHandler(this);



//        자동로그인 인텐트에 의해 이메일이 전달받았다면
        Intent intent = getIntent();
        final String email = intent.getStringExtra("SSP_EMAIL");
        if(email != null){
            Log.d("넘어온 이메일 ", email);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("자동로그인", "쓰레드");
                    getJoson.requestWebServer("api/retUserStatByEmail", autoLoginCallback, email);
                }
            }).start();
//                    TODO: 자동로그인에 의해 인텐트 전달받음, 그 이메일로 JSON 얻어와서 id랑, 닉네임 확보하고 Userinfo에 저장하는것까지 완수하기 !
        }


        //----------------------Toobar Setting---------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Button Appbar_My=toolbar.findViewById(R.id.appbar_btn_1);
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
                Toast.makeText(MainFormActivity.this,"My test",Toast.LENGTH_LONG).show();
            }
        });
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent=new Intent(MainFormActivity.this,AddQustion.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new QuestionListFragment())
                    .commit();
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View view=navigationView.getHeaderView(0);

        ((TextView)view.findViewById(R.id.nav_header_name)).setText(Userinfo.shared.getName()); //Drawable 사용자 이름 변경


        // 별점 받아오기 , 프로필 받아오기, 회원등급 받아오기

        navigationView.setNavigationItemSelectedListener(this);
    }

    Callback autoLoginCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();
//            Log.d("자동로그인", "콜백함수");
//            Log.d("자동로그인 s", s);
            try{
                JSONObject jsonObject = new JSONObject(s);
//                Log.d("자동로그인", "try-catch문");
                JSONObject dataObject = jsonObject.getJSONObject("data");
//                Log.d("id", dataObject.getInt("id"));
                int id = dataObject.getInt("id");
                String email = dataObject.getString("email");
                String name = dataObject.getString("name");
//                Log.d("자동로그인 id", Integer.toString(id));
//                Log.d("자동로그인 email", email);
//                Log.d("자동로그인 name", name);
                Userinfo user = Userinfo.shared;
                user.setEmail(email);
                user.setId(id);
                user.setName(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent=new Intent(MainFormActivity.this,list_MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        }
//        로그아웃 버튼 리스너 설정.
        else if (id == R.id.nav_logout){
            SaveSharedPreference.clearUserEmail(MainFormActivity.this);
            Intent i = new Intent(getApplicationContext(), activity_login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
