package com.cloudwell.paywell.services.activity.eticket.busticketNew.seatLayout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview.BordingPoint;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2020-05-03.
 */
public class CustomSpnerForBoardingPoint extends BaseAdapter {

    private Context ctx;
    private ArrayList<BordingPoint> BoothInfo;


    public CustomSpnerForBoardingPoint(Context context, ArrayList<BordingPoint> BoothInfo) {
        this.ctx = context;
        this.BoothInfo = BoothInfo;

    }


    @Override
    public int getCount() {
        return BoothInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return BoothInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NotNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_value_layout, null);

        }

        BordingPoint m = BoothInfo.get(position);

        TextView textView = (TextView) convertView.findViewById(R.id.spinnerTextView);
        textView.setText(m.getCounterName() + ": " + m.getScheduleTime());


        return convertView;

    }

}
