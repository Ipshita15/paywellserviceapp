package com.cloudwell.paywell.services.activity.eticket.airticket.multiCity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cloudwell.paywell.services.R;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class MultiCityFragment extends Fragment {
    private LinearLayout mainLayout;
    private FancyButton buttonSearch;
    private FancyButton addAnotherFlightBtn;
    private TranslateAnimation slideInAnim = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
            Animation.RELATIVE_TO_PARENT, -1, Animation.RELATIVE_TO_PARENT, 0);
    private TranslateAnimation slideOutAnim = new TranslateAnimation(
            Animation.REVERSE, 0, Animation.REVERSE, 0,
            Animation.REVERSE, 0, Animation.REVERSE, -1);
    private int addNoFlag = 0;
    private LayoutInflater inflater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_multi_city, container, false);
        initView(mainView);
        return mainView;
    }

    private void initView(View view) {
        mainLayout = view.findViewById(R.id.flightListContainerLL);
        buttonSearch = view.findViewById(R.id.btn_search);
        slideInAnim.setDuration(500);
        slideOutAnim.setDuration(500);

        addAnotherFlightBtn = view.findViewById(R.id.btn_add_another_flight);
        addAnotherFlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (addNoFlag < AppHandler.MULTIPLE_TOPUP_LIMIT) {
                addAnotherNo();
//                } else {
//                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.topup_limit_msg, Snackbar.LENGTH_LONG);
//                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
//                    View snackBarView = snackbar.getView();
//                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
//                    snackbar.show();
//                }
            }
        });
        inflater = getLayoutInflater();
        addAnotherNo();
    }

    private void addAnotherNo() {
        ++addNoFlag;
        final View flightView = inflater.inflate(R.layout.multi_city_list_item_view, null);
        ImageView removeBtn = flightView.findViewById(R.id.flightCancelIV);

        flightView.setTag(addNoFlag);

        if (addNoFlag == 1) {
//            removeBtn.setVisibility(View.GONE);
        }


        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --addNoFlag;
//                slideOutAnim.setAnimationListener(new Animation.AnimationListener() {
//
//                            @Override
//                            public void onAnimationStart(Animation animation) {
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animation animation) {
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animation animation) {
//
//                                mainLayout.removeView(flightView);
//
//
////                                flightView.postDelayed(new Runnable() {
////
////                                    @Override
////                                    public void run() {
////                                        mainLayout.removeView(flightView);
////                                    }
////
////                                }, 100);
//                            }
//                        });

                flightView.animate().alpha(0).setDuration(500).withEndAction(new Runnable() {
                    public void run() {
                        // rRemove the view from the parent layout
                        mainLayout.removeView(flightView);
                    }
                });
            }
        });
        mainLayout.addView(flightView);
        flightView.setAnimation(slideInAnim);
    }


}
