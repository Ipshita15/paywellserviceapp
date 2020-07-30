package com.cloudwell.paywell.services.activity.navigationdrawer;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/22/20.
 */
public class TitleMenu extends ExpandableGroup<SubTitle> {
    private int imageResource = 0;

    public TitleMenu(String title, List<SubTitle> items, int imageResource) {
        super(title, items);
        this.imageResource = imageResource;
    }

    public int getImageResource() {
        return imageResource;
    }
}
