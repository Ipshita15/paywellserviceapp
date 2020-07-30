package com.cloudwell.paywell.services.activity.navigationdrawer;

import android.view.View;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.utils.ResorceHelper;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/22/20.
 */
public class SubTitleViewHolder extends ChildViewHolder {
    private TextView subTitleTextView;

    public SubTitleViewHolder(View itemView) {
        super(itemView);
        subTitleTextView = (TextView) itemView.findViewById(R.id.main_nav_submenu_item_title);
    }

    public void setSubTitletName(String name) {
        subTitleTextView.setText(ResorceHelper.getResId(name, R.string.class));
    }
}
