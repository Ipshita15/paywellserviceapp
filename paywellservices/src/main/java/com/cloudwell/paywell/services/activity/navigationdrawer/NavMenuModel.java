package com.cloudwell.paywell.services.activity.navigationdrawer;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/22/20.
 */
public class NavMenuModel {
    public String menuTitle;
    public int menuIconDrawable;
    public List<SubMenuModel> subMenu;


    public NavMenuModel(String menuTitle, int menuIconDrawable) {
        this.menuTitle = menuTitle;
        this.menuIconDrawable = menuIconDrawable;
        this.subMenu = new ArrayList<>();
    }

    public NavMenuModel(String menuTitle, int menuIconDrawable, ArrayList<SubMenuModel> subMenu) {
        this.menuTitle = menuTitle;
        this.menuIconDrawable = menuIconDrawable;
        this.subMenu = new ArrayList<>();
        this.subMenu.addAll(subMenu);
    }

    public static class SubMenuModel {
        public String subMenuTitle;
        public Fragment fragment;

        public SubMenuModel(String subMenuTitle) {
            this.subMenuTitle = subMenuTitle;
        }
    }
}
