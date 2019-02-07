package com.dejobhu.skhu.dejobhu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple Transition which allows the views above the epic centre to transition upwards and views
 * below the epic centre to transition downwards.
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class SlideExpload extends Visibility {
    private final int[] mTempLoc = new int[2];
    private final String KEY_SCREEN_BOUNDS = "screenBounds";

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        view.getLocationOnScreen(mTempLoc);
        int left = mTempLoc[0];
        int top = mTempLoc[1];
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();
        transitionValues.values.put(KEY_SCREEN_BOUNDS, new Rect(left, top, right, bottom));
    }

    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        captureValues(transitionValues);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Animator onAppear(ViewGroup sceneRoot, View view,
                             TransitionValues startValues, TransitionValues endValues) {
        if (endValues == null) return null;

        Rect bounds = (Rect) endValues.values.get(KEY_SCREEN_BOUNDS);
        float endY = view.getTranslationY();
        float startY = endY + calculateDistance(sceneRoot, bounds);
        return ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, startY, endY);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Animator onDisappear(ViewGroup sceneRoot, View view,
                                TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null) return null;

        Rect bounds = (Rect) startValues.values.get(KEY_SCREEN_BOUNDS);
        float startY = view.getTranslationY();
        float endY = startY + calculateDistance(sceneRoot, bounds);
        return ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, startY, endY);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int calculateDistance(View sceneRoot, Rect viewBounds) {
        sceneRoot.getLocationOnScreen(mTempLoc);
        int sceneRootY = mTempLoc[1];

        if (getEpicenter() == null) {
            return sceneRoot.getHeight();
        } else if (viewBounds.top <= getEpicenter().top) {
            return sceneRootY - getEpicenter().top;
        } else {
            return sceneRootY+sceneRoot.getHeight()-getEpicenter().bottom;
        }
    }
}
