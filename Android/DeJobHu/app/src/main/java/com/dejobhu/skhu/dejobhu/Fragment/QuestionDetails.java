package com.dejobhu.skhu.dejobhu.Fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dejobhu.skhu.dejobhu.R;


public class QuestionDetails extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.questiondetails,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View Lview=view.findViewById(R.id.content);
        Lview.setAlpha(0f);

        ObjectAnimator animator= ObjectAnimator.ofFloat(Lview,View.ALPHA,0f,1f);

        animator.setStartDelay(50);
        animator.setDuration(150);
        animator.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().setAlpha(0f);
    }
}
