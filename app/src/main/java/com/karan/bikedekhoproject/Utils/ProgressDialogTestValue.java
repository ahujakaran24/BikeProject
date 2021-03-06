package com.karan.bikedekhoproject.Utils;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.karan.bikedekhoproject.R;

/**
 * Created by karanahuja on 22/07/15.
 */

public class ProgressDialogTestValue extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainprogress);
        View view =findViewById(R.id.progress);
        showDialog(this,view);
        //removeDialog(view);
    }

    public static void showDialog(final Context mContext,final View view) {
        view.setVisibility(View.VISIBLE);
        showProgressDialog(mContext,view);
    }

    public static void removeDialog(View view) {
        view.setVisibility(View.GONE);
    }

    public static void showProgressDialog(final Context mContext,final View view) {
        final Handler handler = new Handler();
        final LinearLayout firstInnerCircle = (LinearLayout) view.findViewById(R.id.firstOuterCircle);
        final LinearLayout secondInnerCircle = (LinearLayout) view.findViewById(R.id.secondOuterCircle);
        final LinearLayout thirdInnerCircle = (LinearLayout) view.findViewById(R.id.thirdOuterCircle);
        final LinearLayout forthInnerCircle = (LinearLayout) view.findViewById(R.id.forthOuterCircle);
        final LinearLayout fifthInnerCircle = (LinearLayout) view.findViewById(R.id.fifthOuterCircle);

        firstInnerCircle.setBackgroundResource(R.drawable.inner_circle);


        final Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.animation);

        firstInnerCircle.startAnimation(animation);

        handler.postDelayed(new Runnable() {
            boolean forthImage;
            @Override
            public void run() {
                if(view.getVisibility()==View.VISIBLE) {
                    if (firstInnerCircle.getAnimation() != null) {
                        firstInnerCircle.setBackgroundResource(R.drawable.outer_circle);
                        firstInnerCircle.clearAnimation();
                        secondInnerCircle.startAnimation(animation);
                        secondInnerCircle.setBackgroundResource(R.drawable.inner_circle);

                    } else if (secondInnerCircle.getAnimation() != null) {

                        secondInnerCircle.setBackgroundResource(R.drawable.outer_circle);
                        secondInnerCircle.clearAnimation();
                        thirdInnerCircle.startAnimation(animation);
                        thirdInnerCircle.setBackgroundResource(R.drawable.inner_circle);

                    } else if (thirdInnerCircle.getAnimation() != null) {

                        thirdInnerCircle.setBackgroundResource(R.drawable.outer_circle);
                        thirdInnerCircle.clearAnimation();
                        forthInnerCircle.startAnimation(animation);
                        forthInnerCircle.setBackgroundResource(R.drawable.inner_circle);

                    } else if (forthInnerCircle.getAnimation() != null) {

                        forthInnerCircle.setBackgroundResource(R.drawable.outer_circle);
                        forthInnerCircle.clearAnimation();
                        fifthInnerCircle.startAnimation(animation);
                        fifthInnerCircle.setBackgroundResource(R.drawable.inner_circle);

                    } else if (fifthInnerCircle.getAnimation() != null) {

                        fifthInnerCircle.setBackgroundResource(R.drawable.outer_circle);
                        fifthInnerCircle.clearAnimation();
                        forthImage=true;
                        Handler handler =new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                firstInnerCircle.startAnimation(animation);
                                firstInnerCircle.setBackgroundResource(R.drawable.inner_circle);
                            }
                        },200);

                    }
                    if(forthImage) {
                        forthImage=false;
                        handler.postDelayed(this, 400);

                    }
                    else {
                        handler.postDelayed(this, 200);
                    }
                }
            }
        }, 200);

    }
}