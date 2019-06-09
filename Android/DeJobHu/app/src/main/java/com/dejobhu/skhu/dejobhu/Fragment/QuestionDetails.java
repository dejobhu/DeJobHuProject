package com.dejobhu.skhu.dejobhu.Fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dejobhu.skhu.dejobhu.MainFormActivity;
import com.dejobhu.skhu.dejobhu.MenuOptions;
import com.dejobhu.skhu.dejobhu.NoteReceiverStat;
import com.dejobhu.skhu.dejobhu.R;
import com.dejobhu.skhu.dejobhu.SendNote;
import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;
import com.dejobhu.skhu.dejobhu.Singleton.Userinfo;
import com.dejobhu.skhu.dejobhu.adapters.CommentAdapter;
import com.dejobhu.skhu.dejobhu.login.SaveSharedPreference;
import com.dejobhu.skhu.dejobhu.models.CommentItem;
import com.dejobhu.skhu.dejobhu.models.ItemImage;
import com.dejobhu.skhu.dejobhu.models.ItemText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


@SuppressLint("ValidFragment")
public class QuestionDetails extends Fragment {
    String passedId;
    String passedEmail;
    String passedName;
    int post_userId;
    boolean isPassed;
    ArrayList<ItemText> arrayListText = new ArrayList<>();
    ArrayList<ItemImage> arrayListImage = new ArrayList<>();
    LinearLayout linearLayout;
    ProgressBar progressBar;
    int post_id;
    RecyclerView recyclerView;
    CommentAdapter adapter;
    ArrayList<CommentItem> arrayListComment = new ArrayList<>();
    NoteReceiverStat user;
    GetJoson getJoson = GetJoson.getInstance();
    //    Toast를 자신이 속해있는 상위 컨테이너로 띄우기 위해
    private Context context;
    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "서버통신에 실패하였습니다.\n관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            String s = response.body().string();
            Log.d("url", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getInt("result") == 1000) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "서버통신에 실패하였습니다.\n관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }
                    });
                } else //서버통신 성공
                {
                    JSONObject data = jsonObject.getJSONObject("data");

                    JSONArray array = data.getJSONArray("text");
                    JSONObject object;
                    for (int i = 0; i < array.length(); i++) {
                        object = array.getJSONObject(i);
                        arrayListText.add(new ItemText(object.getInt("post_number"), object.getString("Content")));
                    }

                    array = data.getJSONArray("Image");

                    for (int i = 0; i < array.length(); i++) {
                        object = array.getJSONObject(i);
                        arrayListImage.add(new ItemImage(object.getInt("post_number"), object.getString("url")));
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getPost();
                        }
                    });
                }
            } catch (JSONException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "서버통신에 실패하였습니다.\n관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                });
            }
        }
    };


    public QuestionDetails(int id) {
        this.post_id = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        context = container.getContext();

        return inflater.inflate(R.layout.questiondetails, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        linearLayout = view.findViewById(R.id.detail_content);
        recyclerView = view.findViewById(R.id.detail_recycle);
        linearLayout.setAlpha(0f);

        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, View.ALPHA, 0f, 1f);

        animator.setStartDelay(50);
        animator.setDuration(150);
        animator.start();

        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
        new Thread() {
            @Override
            public void run() {
                GetJoson joson = GetJoson.getInstance();
                Userinfo userinfo = Userinfo.shared;
                joson.requestWebServer("api/post/getPost", callback, "" + post_id);
            }
        }.run();

        adapter = new CommentAdapter(getActivity(), arrayListComment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupVisible(MenuOptions.NUM_MAIN, false);
        menu.add(MenuOptions.NUM_MYPOST, MenuOptions.NUM_MODIFY, 0, "수정하기");
        menu.add(MenuOptions.NUM_MYPOST, MenuOptions.NUM_DELETE, 0, "삭제하기");
        menu.add(MenuOptions.NUM_OTHERS, MenuOptions.NUM_REPORT, 0, "신고하기");
        menu.add(MenuOptions.NUM_OTHERS, MenuOptions.NUM_NOTES, 0, "쪽지보내기");
        if (isMyPost()) {
            menu.setGroupVisible(MenuOptions.NUM_OTHERS, false);
            menu.setGroupVisible(MenuOptions.NUM_MYPOST, true);
        } else {
            menu.setGroupVisible(MenuOptions.NUM_MYPOST, false);
            menu.setGroupVisible(MenuOptions.NUM_OTHERS, true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MenuOptions.NUM_MODIFY) {
            Log.d("프래그먼트", "수정하기");
            return true;
        } else if (item.getItemId() == MenuOptions.NUM_DELETE) {
            Log.d("프래그먼트", "삭제하기");
            return true;
        } else if (item.getItemId() == MenuOptions.NUM_REPORT) {
            Log.d("프래그먼트", "신고하기");
            return true;
        } else if (item.getItemId() == MenuOptions.NUM_NOTES) {
            Log.d("프래그먼트", "쪽지보내기");
            runUserStatsByPostId();
            return true;
        } else {
            Log.d("프래그먼트", "아무것ㄷ아님");
        }
        return super.onOptionsItemSelected(item);
    }

    public void getPost() {
        int textIndex = 0;
        int ImageIndex = 0;
        for (int i = 0; i < arrayListImage.size() + arrayListText.size(); i++) {
            if (textIndex < arrayListText.size() && arrayListText.get(textIndex).getPostNum() == i) {
                TextView textView = new TextView(getContext());
                textView.setText(arrayListText.get(textIndex).getContent());
                textView.setTextSize(18.0f);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = 15;

                linearLayout.addView(textView, layoutParams);
                textIndex++;
            } else if (ImageIndex < arrayListImage.size()) {
                ImageView Imageview = new ImageView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.bottomMargin = 15;
                linearLayout.addView(Imageview, layoutParams);
                Glide.with(getContext()).load(arrayListImage.get(ImageIndex).getUrl()).into(Imageview);
                ImageIndex++;
            } else {
                break;
            }

        }

        progressBar.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
    }

    public void runUserStatsByPostId() {
        final int id = post_id;


        new Thread(new Runnable() {

            @Override
            public void run() {
                getJoson.requestWebServer("api/post/getUserStatsByPostId", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getString("result").equals("NG")) {
                                Toast.makeText(context, "게시글 유저 정보를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                JSONObject jsonData = jsonObject.getJSONObject("data");
                                passedId = jsonData.getString("id");
                                passedEmail = jsonData.getString("email");
                                passedName = jsonData.getString("name");
                                Log.d("유저정보", "" + passedId + ", " + passedEmail + ", " + passedName);
                                //                            쪽지 수신자에 대한 정보를 저장
                                Log.d("정수", passedId);
                                user = new NoteReceiverStat(Integer.parseInt(passedId), passedName, passedEmail);
                                Intent intent = new Intent(getActivity(), SendNote.class);
//        객체를 intent로 전달해야 함.
                                intent.putExtra("recvUser", user);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, Integer.toString(id));
            }
        }).start();


    }

    public boolean isMyPost() {
        isPassed = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                getJoson.requestWebServer("api/post/getUserStatsByPostId", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getString("result").equals("NG")) {
                                Toast.makeText(context, "유저 정보를 받아오지 못함.", Toast.LENGTH_SHORT).show();
                            } else {
                                JSONObject user = jsonObject.getJSONObject("data");
                                post_userId = user.getInt("id");
                                Log.d("받아온 아이디", "" + post_userId);
                                isPassed = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, Integer.toString(post_id));

            }

        }).start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {

        }
        while(isPassed == false){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("게시글 주인의 아이디", post_userId + "");
        Log.d("내 아이디", Userinfo.shared.getId() + "");

        return post_userId == Userinfo.shared.getId();
//        Log.d("내 아이디랑 게시글의 아이디가 같은지", (Integer.parseInt(post_userId) == Userinfo.shared.getId()) + "");
//        return true;/*(Integer.parseInt(post_userId) == Userinfo.shared.getId());*/

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().setAlpha(0f);
    }
}
