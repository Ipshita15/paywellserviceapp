package com.cloudwell.paywell.services.activity.faq.adapter;

import android.content.Context;
import android.widget.ExpandableListView;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 11/24/20.
 */
public class SecondLevelExpandableListView extends ExpandableListView {

    public SecondLevelExpandableListView(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
