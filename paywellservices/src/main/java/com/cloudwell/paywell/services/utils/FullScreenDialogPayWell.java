package com.cloudwell.paywell.services.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Created by YASIN on 03,July,2019
 * Email: yasinenubd5@gmail.com
 */
public class FullScreenDialogPayWell extends DialogFragment {

    public static final String TITLE_KEY = "title";
    public static final String THEME_KEY = "theme";
    public static final String LAYOUT_KEY = "layout";
    public static final String DIALOG_KEY = "dialog";

    private int themeId;
    private int layoutId;
    private String title;

    private OnSetFullScreenDialogView onSetFullScreenDialogView;

    public static FullScreenDialogPayWell newInstance(String title, int themeResourceId, OnSetFullScreenDialogView onSetFullScreenDialogView) {
        FullScreenDialogPayWell frag = new FullScreenDialogPayWell();
        Bundle args = new Bundle();
        args.putInt(THEME_KEY, themeResourceId);
        args.putString(TITLE_KEY, title);
        args.putParcelable(DIALOG_KEY, onSetFullScreenDialogView);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeId = getArguments().getInt(THEME_KEY);
        title = getArguments().getString(TITLE_KEY);
        onSetFullScreenDialogView = (OnSetFullScreenDialogView) getArguments().getParcelable(DIALOG_KEY);
        setStyle(DialogFragment.STYLE_NORMAL, themeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return onSetFullScreenDialogView.setView(inflater, container, getDialog());
    }

    public interface OnSetFullScreenDialogView extends Parcelable {
        View setView(LayoutInflater inflater, ViewGroup container, Dialog dialog);
    }
}
