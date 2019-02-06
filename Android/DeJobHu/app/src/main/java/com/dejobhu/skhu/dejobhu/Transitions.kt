package com.dejobhu.skhu.dejobhu

import android.os.Build
import android.support.annotation.RequiresApi
import android.transition.TransitionSet
import android.view.animation.Interpolator

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun TransitionSet.setCommonInterpolator(interpolator: Interpolator): TransitionSet {
    (0 until transitionCount)
            .map { index -> getTransitionAt(index) }
            .forEach { transition -> transition.interpolator = interpolator }

    return this
}