package com.dejobhu.skhu.dejobhu.Fragment;



import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dejobhu.skhu.dejobhu.Question;
import com.dejobhu.skhu.dejobhu.R;
import com.dejobhu.skhu.dejobhu.Singleton.GetJoson;
import com.dejobhu.skhu.dejobhu.SlideExpload;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public class QuestionListFragment extends Fragment {

    FastOutSlowInInterpolator transition=new FastOutSlowInInterpolator();
    private final Long TRASITION_DURATION=1000L;
    private final String TAP_POSTION="tap_position";

    int page=1;
    int lastpage=1;
    private  int tapPosition=NO_POSITION;
    Rect viewRect=new Rect();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<Question> arrayList= new ArrayList<Question>();



    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.questionsns,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            tapPosition=savedInstanceState.getInt(TAP_POSTION,NO_POSITION);
        }catch (NullPointerException e)
        {
            tapPosition=NO_POSITION;
        }


        postponeEnterTransition();

        recyclerView=view.findViewById(R.id.questionList);
        progressBar=view.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),LinearLayoutManager.VERTICAL));
         adapter=new QuestionAdapter();
        recyclerView.setAdapter(adapter);

        final SNSViewModal viewModal=ViewModelProviders.of(this).get(SNSViewModal.class);

        if(viewModal.getEmails().getValue()==null) viewModal.getQustions();

        viewModal.getEmails().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                render(state);
            }
        });

    }

    private void render(State state)
    {
        if(state instanceof State.InProgress){
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            new Thread(){
                @Override
                public void run() {
                    GetJoson joson=GetJoson.getInstance();
                    joson.PageRequest("api/post/all",callback,"page="+page);
                }
            }.run();
            startPostponedEnterTransition();
        }else {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            //현재 body 기준으로 갈라짐

            ((QuestionAdapter)adapter).setData(arrayList);
            getView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if(getExitTransition()==null) {
                        SlideExpload slideExpload=new SlideExpload();
                        slideExpload.setDuration(TRASITION_DURATION)
                                .setInterpolator(transition);
                        setExitTransition(slideExpload);
                    }
                    return true;
                }
            });

            LinearLayoutManager linearLayoutManager= (LinearLayoutManager) recyclerView.getLayoutManager();
            View view=linearLayoutManager.findViewByPosition(tapPosition);

            if(view!=null) {
                view.getGlobalVisibleRect(viewRect); //왜 쓴건지 모르겠음

                ((Transition) getExitTransition()).setEpicenterCallback(new Transition.EpicenterCallback() {
                    @Override
                    public Rect onGetEpicenter(Transition transition) {
                        return viewRect;
                    }
                });
            }

            startPostponedEnterTransition();
        }
    }

    private Callback callback=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s= response.body().string();
            try {
                JSONObject object=new JSONObject(s);
                if(object.getInt("result")==2000)
                {
                    JSONObject data=object.getJSONObject("data");
                    JSONArray array=data.getJSONArray("data");
                    lastpage=data.getInt("last_page");

                    for(int i=0;i<array.length();i++)
                    {
                        JSONObject value=array.getJSONObject(i);
                        arrayList.add(new Question(value.getString("user_name"),value.getString("title"),value.getString("created_at"),null));

                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"게시글을 불러왔습니다",Toast.LENGTH_LONG).show();
                            adapter.notifyDataSetChanged();
                        }});
                }else
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"서버통신이 실패했습니다. 관리자에게 문의주세요",Toast.LENGTH_LONG).show();
                        }});

                }

            } catch (JSONException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"서버통신이 실패했습니다. 관리자에게 문의주세요",Toast.LENGTH_LONG).show();
                    }
                });
            }

        }
    };
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAP_POSTION, tapPosition);
    }

    public class QuestionAdapter extends RecyclerView.Adapter<QuestionViewHolder> {


        ArrayList<Question> mData;


        public void setData(List<Question> data) {
            this.mData = (ArrayList<Question>) data;
            notifyDataSetChanged();
        }

        @Override
        public QuestionViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.questioncard,parent,false);
            return new QuestionViewHolder(view);
        }

        @Override
        public void onBindViewHolder( QuestionViewHolder holder,  int position) {

            final int p=position;
            final QuestionViewHolder holder1=holder;
            View.OnClickListener listener=new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tapPosition = p;
                    holder1.itemView.getGlobalVisibleRect(viewRect);

                    ((Transition)getExitTransition()).setEpicenterCallback(new Transition.EpicenterCallback() {
                        @Override
                        public Rect onGetEpicenter(Transition transition) {
                            return viewRect;
                        }
                    });


                    TransitionSet sharedElementTransition = new TransitionSet()
                            .addTransition(new ChangeBounds())
                            .addTransition(new ChangeTransform())
                            .addTransition(new ChangeImageTransform());

                    sharedElementTransition.setDuration(TRASITION_DURATION);
                    sharedElementTransition.setInterpolator(transition);

                    Fragment fragment = new QuestionDetails();
                    fragment.setSharedElementEnterTransition(sharedElementTransition);
                    fragment.setSharedElementReturnTransition(sharedElementTransition);

                    try {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.container, fragment)
                                .addToBackStack(null)
                                .addSharedElement(holder1.itemView,getString(R.string.transition_name))
                               .commit();


                    }catch (NullPointerException e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            holder.bindListener(mData.get(position).getBody(),listener);
            try{
                holder.imageView.setImageBitmap(mData.get(position).getImage());
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
            holder.body.setText(mData.get(position).getBody());
            holder.ID.setText(mData.get(position).getName());
            holder.Time.setText(mData.get(position).getTimestep());
            holder.imageView.setImageResource(R.drawable.face);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            try {
                return mData.size();
            }catch (NullPointerException e)
            {
                return 0;
            }
        }
    }
    class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView body;
        TextView ID;
        TextView Time;
        ImageView imageView;
        View view;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            body=itemView.findViewById(R.id.question_title);
            ID=itemView.findViewById(R.id.question_id);
            Time=itemView.findViewById(R.id.question_time);
            imageView=itemView.findViewById(R.id.question_image);


        }

        void bindListener(String question,View.OnClickListener listener)
        {
            view.setOnClickListener(listener);
            view.setTransitionName(question);
        }
    }

}
