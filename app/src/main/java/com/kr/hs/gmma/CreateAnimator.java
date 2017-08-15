package com.kr.hs.gmma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.widget.Button;

/**
 * Created by lghlo on 2017-08-15.
 */

public class CreateAnimator {

    protected Animator createShowItemAnimator(View item, View fab) {
        float dx = fab.getX() - item.getX();
        float dy = fab.getY() - item.getY();

        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                PropertyValuesHolder.ofFloat("rotation", 0f, 720f),
                PropertyValuesHolder.ofFloat("translationX", dx, 0f),
                PropertyValuesHolder.ofFloat("translationY", dy, 0f)
        );
        return anim;
    }

    protected Animator createHideItemAnimator(final View item, View fab) {
        float dx = fab.getX() - item.getX();
        float dy = fab.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                PropertyValuesHolder.ofFloat("rotation", 720f, 0f),
                PropertyValuesHolder.ofFloat("rotation", 720f, 0f),
                PropertyValuesHolder.ofFloat("translationX", 0f, dx),
                PropertyValuesHolder.ofFloat("translationY", 0f, dy)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });
        return anim;
    }

    protected Animator createLineHideItemAnimator(final View item, View fab){
        float dx = fab.getX() - item.getX();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                PropertyValuesHolder.ofFloat("translationX", 0f, dx)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });
        return anim;
    }

    protected Animator createLineShowItemAnimator(final View item, View fab){
        float dx = fab.getX() - item.getX();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                PropertyValuesHolder.ofFloat("translationX", dx, 0f)
                );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });
        return anim;
    }
}
