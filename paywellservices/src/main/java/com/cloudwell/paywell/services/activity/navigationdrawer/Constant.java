package com.cloudwell.paywell.services.activity.navigationdrawer;

import android.content.Context;

import com.cloudwell.paywell.services.R;

import java.util.ArrayList;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/22/20.
 */
public class Constant {

    public static final String KEY_logout_text = "logout_text";
    public static final String KEY_about = "nav_about";
    public static final String KEY_nav_terms_and_conditions = "nav_terms_and_conditions";
    public static final String KEY_nav_policy_statement = "nav_policy_statement";
    public static final String KEY_home_statement = "home_statement";
    public static final String KEY_home_statement_mini = "home_statement_mini";
    public static final String KEY_home_statement_balance = "home_statement_balance";
    public static final String KEY_home_statement_sales = "home_statement_sales";
    public static final String KEY_home_statement_transaction = "home_statement_transaction";
    public static final String KEY_faq_text = "faq";


    public static final String KEY_home_settings = "home_settings";
    public static final String KEY_home_settings_change_pin = "home_settings_change_pin";
    public static final String KEY_home_settings_help = "home_settings_help";
    public static final String KEY_home_tutorial = "home_tutorial";

    public static ArrayList<NavMenuModel> getMenuNavigasi(Context context) {
        ArrayList<NavMenuModel> menu = new ArrayList<>();


        menu.add(new NavMenuModel(KEY_logout_text, R.drawable.ic_logout));
        menu.add(new NavMenuModel(KEY_about, R.drawable.ic_about));
        menu.add(new NavMenuModel(KEY_nav_terms_and_conditions, R.drawable.ic_terms_and_condition));
        menu.add(new NavMenuModel(KEY_nav_policy_statement, R.drawable.ic_privacy));

        ArrayList<NavMenuModel.SubMenuModel> statementSubMenuModels = new ArrayList<>();
        statementSubMenuModels.add(new NavMenuModel.SubMenuModel(KEY_home_statement_mini));
        statementSubMenuModels.add(new NavMenuModel.SubMenuModel(KEY_home_statement_balance));
        statementSubMenuModels.add(new NavMenuModel.SubMenuModel(KEY_home_statement_sales));
        statementSubMenuModels.add(new NavMenuModel.SubMenuModel(KEY_home_statement_transaction));
        menu.add(new NavMenuModel(KEY_home_statement, R.drawable.ic_nav_statement, statementSubMenuModels));


        ArrayList<NavMenuModel.SubMenuModel> settingsSubMenuModels = new ArrayList<>();
        settingsSubMenuModels.add(new NavMenuModel.SubMenuModel(KEY_home_settings_change_pin));
        settingsSubMenuModels.add(new NavMenuModel.SubMenuModel(KEY_home_tutorial));
        menu.add(new NavMenuModel(KEY_home_settings, R.drawable.ic_nav_settings, settingsSubMenuModels));


        menu.add(new NavMenuModel(KEY_faq_text, R.drawable.ic_faq_v1));


        return menu;
    }
}
