package com.dejobhu.skhu.dejobhu.Fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dejobhu.skhu.dejobhu.R;
import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;
import com.dejobhu.skhu.dejobhu.Singleton.Userinfo;
import com.dejobhu.skhu.dejobhu.adapters.CommentAdapter;
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

    ArrayList<ItemText> arrayListText = new ArrayList<>();
    ArrayList<ItemImage> arrayListImage = new ArrayList<>();
    LinearLayout linearLayout;
    ProgressBar progressBar;
    int post_id;
    RecyclerView recyclerView;
    CommentAdapter adapter;
    ArrayList<CommentItem> arrayListComment = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.questiondetails, container, false);
    }


    public QuestionDetails(int id) {
        this.post_id = id;
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().setAlpha(0f);
    }
}
