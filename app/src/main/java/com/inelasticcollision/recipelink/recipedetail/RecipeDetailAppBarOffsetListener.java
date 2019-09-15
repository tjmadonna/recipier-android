/*
 * RecipeDetailAppBarOffsetListener.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 9/24/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.recipedetail;

import android.animation.Animator;
import com.google.android.material.appbar.AppBarLayout;
import android.view.View;

final class RecipeDetailAppBarOffsetListener implements AppBarLayout.OnOffsetChangedListener {

    private final View mView;

    private final int mTriggerOffset;

    private boolean mEnabled;

    private boolean mAnimatingOut;

    private boolean mAnimatingIn;

    RecipeDetailAppBarOffsetListener(View view, int triggerOffset) {
        mView = view;
        mTriggerOffset = triggerOffset;
        mEnabled = false;
        mAnimatingOut = false;
        mAnimatingIn = false;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (!mEnabled) { return; }

        int scrollHeight = appBarLayout.getTotalScrollRange() - Math.abs(verticalOffset);

        if (scrollHeight < mTriggerOffset) {

            if (mAnimatingOut) { return; }

            // View should fade out

            animateViewAlpha(0.0f, new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mAnimatingOut = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mAnimatingOut = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mAnimatingOut = false;
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

        } else {

            if (mAnimatingIn) { return; }

            // View should fade in

            animateViewAlpha(1.0f, new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mAnimatingIn = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mAnimatingIn = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mAnimatingIn = false;
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

        }

    }

    private void animateViewAlpha(float alpha, final Animator.AnimatorListener animatorListener) {

        // If alpha already equals animation alpha, then don't animate
        if (mView.getAlpha() == alpha) { return; }

        // Animate the view's alpha using the provided alpha value and animator listener
        // 600 ms is default collapsing toolbar scrim animate during
        mView.animate()
                .alpha(alpha)
                .setDuration(600)
                .setListener(animatorListener)
                .start();

    }

}
