package com.cloudwell.paywell.services.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cloudwell.paywell.services.app.AppController;

/**
 * Created by android on 8/2/2016.
 */
public class CustomAdapter<T> extends ArrayAdapter<T> {

    public CustomAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(AppController.getInstance().getOxygenLightFont());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ((TextView) view).setAllCaps(false);
//            }
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        ((TextView) view).setTypeface(AppController.getInstance().getOxygenLightFont());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        ((TextView) view).setAllCaps(false);
//        }
        return view;
    }
}
