package com.cloudwell.paywell.services.activity.faq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 11/24/20.
 */
public class SecondLevelAdapter extends BaseExpandableListAdapter {
    private Context context;


    List<ArrayList<String>> data;

    String[] headers;

    ImageView ivGroupIndicator;


    public SecondLevelAdapter(Context context, String[] headers, List<ArrayList<String>> data) {
        this.context = context;
        this.data = data;
        this.headers = headers;

    }

    @Override
    public Object getGroup(int groupPosition) {

        return headers[groupPosition];
    }

    @Override
    public int getGroupCount() {

        return headers.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_second, null);
        TextView text = (TextView) convertView.findViewById(R.id.rowSecondText);
        String groupText = getGroup(groupPosition).toString();
        text.setText(groupText);

        ImageView viewById = (ImageView) convertView.findViewById(R.id.ivGroupIndicator);
        LinearLayout root = (LinearLayout) convertView.findViewById(R.id.rootLL);
        if (isExpanded) {
            viewById.setImageResource(R.drawable.faq_up_arrow);
            root.setBackgroundColor(Color.parseColor("#eafff4"));
        } else {
            viewById.setImageResource(R.drawable.faq_down_arrow);
            root.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        ArrayList<String> childData;

        childData = data.get(groupPosition);


        return childData.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_third, null);

        TextView textView = (TextView) convertView.findViewById(R.id.rowThirdText);

        ArrayList<String> childArray = data.get(groupPosition);

        String text = childArray.get(childPosition);

        textView.setText(text);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<String> children = data.get(groupPosition);


        return children.size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
