package com.dejobhu.skhu.dejobhu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;

import com.dejobhu.skhu.dejobhu.Singleton.Userinfo;

import com.dejobhu.skhu.dejobhu.login.SaveSharedPreference;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class activity_login extends AppCompatActivity {
    GetJoson getJoson = GetJoson.getInstance();

    boolean isAutoLoginChecked;
    boolean validPass;
    boolean validEmail;



    //네이버 client 정보
    private static final String TAG = "OAuthSampleActivity";
    private static String OAUTH_CLIENT_ID = "TQP9iDx2BHY61vQBAJBf";
    private static String OAUTH_CLIENT_SECRET = "bjxWk80DV3";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인 테스트";
    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;
    private OAuthLoginButton mOAuthLoginButton;
    /**
     * UI 요소들
     */
    private TextView mApiResultText;
    private static TextView mOauthAT;
    private static TextView mOauthRT;
    private static TextView mOauthExpires;
    private static TextView mOauthTokenType;
    private static TextView mOAuthState;

  //  private OAuthLoginButton mOAuthLoginButton;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;


        initData();


        TextView textView_findId = (TextView)findViewById(R.id.textView_findID);
        TextView textView_membership = (TextView)findViewById(R.id.textView_membership);

        final TextView textView_ErrorEmail_ID = (TextView)findViewById(R.id.textView_ErrorEmail_ID);
        final TextView textView_ErrorPassword = (TextView)findViewById(R.id.textView_ErrorPass);
        TextView textView_findID = (TextView)findViewById(R.id.textView_findID);
        final EditText editText_Email_ID = (EditText)findViewById(R.id.editText_Email_ID);
        final EditText editText_Password = (EditText)findViewById(R.id.editText_Password);
        Button button_login = (Button)findViewById(R.id.button_login);

        // 비밀번호 찾기 액티비티로 넘어가는 버튼
        textView_findID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmailAuthToFindPWD.class);
                startActivity(intent);
            }
        });


//        회원가입으로 넘어가는 버튼


        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);


        textView_membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity_login.this,membership_register.class);
                startActivity(intent);
            }
        });



        //로그인 버튼을 눌럿을 때
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //패스워드와 이메일을 형식에 맞게 입력하였으면

//                내가 적은 이메일을 받음
                final String Email_ID = editText_Email_ID.getText().toString();
//                공백문자가 포함되어 있으면
                if (isEmptyOrWhiteSpace(Email_ID))
//                    에러 텍스트뷰로 지정한 것을 보여줌(평소엔 안보임)
                    textView_ErrorEmail_ID.setVisibility(View.VISIBLE);
                else {
//                    포함되어있지 않으면 숨김.
                    textView_ErrorEmail_ID.setVisibility(View.INVISIBLE);
                }
//                패스워드도 받아서
                final String Password = editText_Password.getText().toString();
//                만약 공백문자가 포함되어있으면
                if (isEmptyOrWhiteSpace(Password))
//                    에러텍스트뷰를 보여줌.
                    textView_ErrorPassword.setVisibility(View.VISIBLE);
                else {
                    textView_ErrorPassword.setVisibility(View.INVISIBLE);
                }
//                옳은 이메일, 패스워드인지 체크
                validEmail = validTestForEmail(Email_ID);
                validPass = validTestForPass(Password);
//                로그인 버튼을 눌렀는지 확인하기 위해서
                Log.d("버튼", "test");

                if (validEmail && validPass) {
                    //                만약 자동로그인 버튼을 눌렀다면
                    CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
                    isAutoLoginChecked = checkBox.isChecked();
                    if(isAutoLoginChecked){
//                  만약 자동로그인 체크 되어잇으면 그 정보를 저장하고, 다음에 불러올 때 UserInfo에 저장
                        SaveSharedPreference.setUserEmail(activity_login.this, Email_ID);
                    }
                    new Thread() {
                        public void run() {
// 파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                            getJoson.requestWebServer("api/user", callback, Email_ID, Password);
                        }
                    }.start();

//                requestWebServer를 잘 통과했는지 보기 위해서
                    Log.d("버튼", "end");
                } else if (!validPass) {
                    Toast.makeText(getApplicationContext(), "패스워드를 8-20자 사이로 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "이메일을 올바른 형식으로 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

        });



//        imageView_Close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                onBackPressed();
//            }
//        });

        SpannableString content1 = new SpannableString("비밀번호 찾기");
        content1.setSpan(new UnderlineSpan(), 0, textView_findId.length(), 0);
        textView_findId.setText(content1);

        SpannableString content2 = new SpannableString("회원가입");
        content2.setSpan(new UnderlineSpan(), 0, textView_membership.length(), 0);
        textView_membership.setText(content2);
    }

      private void initData() {
        mOAuthLoginInstance = OAuthLogin.getInstance();

        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);

        /*
         * 2015년 8월 이전에 등록하고 앱 정보 갱신을 안한 경우 기존에 설정해준 callback intent url 을 넣어줘야 로그인하는데 문제가 안생긴다.
         * 2015년 8월 이후에 등록했거나 그 뒤에 앱 정보 갱신을 하면서 package name 을 넣어준 경우 callback intent url 을 생략해도 된다.
         */
        //mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_callback_intent_url);
    }

    private void initView() {
        mApiResultText = (TextView) findViewById(R.id.api_result_text);

        mOauthAT = (TextView) findViewById(R.id.oauth_access_token);
        mOauthRT = (TextView) findViewById(R.id.oauth_refresh_token);
        mOauthExpires = (TextView) findViewById(R.id.oauth_expires);
        mOauthTokenType = (TextView) findViewById(R.id.oauth_type);
        mOAuthState = (TextView) findViewById(R.id.oauth_state);

        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        updateView();
    }
    private void updateView() {

    }


    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();

    }

    /**
     * startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다.
     */
    static private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);

            } else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }

    };

    public void onButtonClick(View v) throws Throwable {

        switch (v.getId()) {
            case R.id.buttonOAuth: {
                mOAuthLoginInstance.startOauthLoginActivity(activity_login.this, mOAuthLoginHandler);
                break;
            }

            default:
                break;
        }
    }


    private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(mContext);

            if (!isSuccessDeleteToken) {
                // 서버에서 token 삭제에 실패했어도 클라이언트에 있는 token 은 삭제되어 로그아웃된 상태이다
                // 실패했어도 클라이언트 상에 token 정보가 없기 때문에 추가적으로 해줄 수 있는 것은 없음
                Log.d(TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorCode(mContext));
                Log.d(TAG, "errorDesc:" + mOAuthLoginInstance.getLastErrorDesc(mContext));
            }

            return null;
        }


    }



    private class RefreshTokenTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            mApiResultText.setText((String) "");
        }
        @Override
        protected String doInBackground(Void... params) {
            return mOAuthLoginInstance.refreshAccessToken(mContext);
        }

        protected void onPostExecute(String content) {
            mApiResultText.setText((String) content);
        }

    }

    static boolean isEmptyOrWhiteSpace(String s){
        if (s == null) return true;
        return s.trim().length() == 0;
    }

//    로그인 콜백함수
    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
//        응답이 왔으면
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();

            try {


                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.getString("result").equals("NG") || jsonObject.getString("result").equals("1000")) {

                    //스레드(Main) 안에 스레드(Button 안의 URL통신을 위한 스레드)를 구현했기 때문에 액티비티 UI가 표시되지 않는다. 따라서
                    //main부에서 직접 UI를 띄워주기 위해 runOnUiThread를 사용한다.
                    activity_login.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Handle UI here
                            Toast.makeText(getApplicationContext(), " 해당하는 아이디 혹은 패스워드가 존재하지 않습니다.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
                //만약 result가 NG가 아닌 다른 것이 나온다면 화면전환을 해준다.
                //jsonObject를 이용한 사용자 정보 추출 구현해야함
                else{


                    JSONObject user=jsonObject.getJSONObject("data");
//                    JSONObject user=data.getJSONObject("0");
                    Userinfo userinfo=Userinfo.shared;

                    //JsonData 파싱
                    userinfo.setId(user.getInt("id"));
                    userinfo.setEmail(user.getString("email"));
                    userinfo.setName(user.getString("name"));







                    Intent intent = new Intent(getApplicationContext(), MainFormActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private boolean validTestForPass(String pass){
        if(pass.length() >= 8 && pass.length() <=20)
            return true;
        else
            return false;
    }

    private boolean validTestForEmail(String email){
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;
        else return false;
    }
}