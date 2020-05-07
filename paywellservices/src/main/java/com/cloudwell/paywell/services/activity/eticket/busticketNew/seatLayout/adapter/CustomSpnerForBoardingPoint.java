package com.cloudwell.paywell.services.activity.eticket.busticketNew.seatLayout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BoothInfo;

import java.util.ArrayList;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2020-05-03.
 */
public class CustomSpnerForBoardingPoint extends ArrayAdapter<BoothInfo> {

    private Context ctx;
    private ArrayList<BoothInfo> BoothInfo;


    public CustomSpnerForBoardingPoint(Context context, ArrayList<BoothInfo> BoothInfo) {
        super(context,  R.layout.spinner_value_layout, R.id.spinnerTextView);
        this.ctx = context;
        this.BoothInfo = BoothInfo;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_value_layout, null);

        }


        TextView textView = (TextView) convertView.findViewById(R.id.spinnerTextView);
        textView.setText(BoothInfo.get(position).getCounter_name());



        return convertView;

    }

}
