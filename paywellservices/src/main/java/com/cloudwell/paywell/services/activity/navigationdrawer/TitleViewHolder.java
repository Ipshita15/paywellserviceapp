package com.cloudwell.paywell.services.activity.navigationdrawer;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.utils.ResorceHelper;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/22/20.
 */
public class TitleViewHolder extends GroupViewHolder {
    private TextView titleView;
    private String titleString;
    private ImageView arrow;
    private ImageView icon;
    NavMenuAdapter adapter;

    public TitleViewHolder(View itemView, NavMenuAdapter adapter) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.nav_menu_item_icon);
        titleView = (TextView) itemView.findViewById(R.id.nav_menu_item_title);
        arrow = (ImageView) itemView.findViewById(R.id.nav_menu_item_arrow);
        this.adapter = adapter;
    }

    public void setGenreTitle(Context context, ExpandableGroup title) {
        if (title instanceof TitleMenu) {
            if (((TitleMenu) title).getImageResource() != 0) {
                Glide.with(context)
                        .load(((TitleMenu) title).getImageResource())
                        .into(icon);
            }

            titleString = title.getTitle();


            titleView.setText(ResorceHelper.getResId(title.getTitle(), R.string.class));

            if (title.getItems().size() > 0) {
                arrow.setVisibility(View.VISIBLE);

                boolean isExpand = false;
                for (int i = 0; i < adapter.isExpandList.size(); i++) {
                    if (titleString.equals(adapter.isExpandList.get(i))) {
                        isExpand = true;
                        break;
                    }
                }
                if (isExpand) {
                    arrow.setImageResource(R.drawable.nav_submenu_open);
                } else {
                    arrow.setImageResource(R.drawable.ic_nav_summenu_close);
                }
            } else {
                arrow.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void expand() {
        adapter.isExpandList.add(titleString);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void collapse() {
        adapter.isExpandList.remove(titleString);
        adapter.notifyDataSetChanged();
    }
}
