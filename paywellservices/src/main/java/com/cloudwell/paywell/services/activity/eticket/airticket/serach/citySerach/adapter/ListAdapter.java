package com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.model.Airport;

import java.util.ArrayList;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 24/3/19.
 */
public class ListAdapter extends ArrayAdapter<Airport> {

    public ArrayList<Airport> MainList;

    public ArrayList<Airport> SubjectListTemp;

    public ListAdapter.SubjectDataFilter subjectDataFilter;

    public ListAdapter(Context context, int id, ArrayList<Airport> subjectArrayList) {

        super(context, id, subjectArrayList);

        this.SubjectListTemp = new ArrayList<Airport>();

        this.SubjectListTemp.addAll(subjectArrayList);

        this.MainList = new ArrayList<Airport>();

        this.MainList.addAll(subjectArrayList);
    }

    @Override
    public Filter getFilter() {

        if (subjectDataFilter == null) {

            subjectDataFilter = new ListAdapter.SubjectDataFilter();
        }
        return subjectDataFilter;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListAdapter.ViewHolder holder = null;

        if (convertView == null) {

            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.custom_layout, null);

            holder = new ListAdapter.ViewHolder();

            holder.SubjectName = (TextView) convertView.findViewById(R.id.tvName);


            convertView.setTag(holder);

        } else {
            holder = (ListAdapter.ViewHolder) convertView.getTag();
        }

        Airport subject = SubjectListTemp.get(position);

        holder.SubjectName.setText(subject.getAirportName());

//        holder.SubjectFullForm.setText(subject.getSubFullForm());

        return convertView;

    }

    public class ViewHolder {

        TextView SubjectName;
        TextView SubjectFullForm;
    }

    private class SubjectDataFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            charSequence = charSequence.toString().toLowerCase();

            FilterResults filterResults = new FilterResults();

            if (charSequence != null && charSequence.toString().length() > 0) {
                ArrayList<Airport> arrayList1 = new ArrayList<Airport>();

                for (int i = 0, l = MainList.size(); i < l; i++) {
                    Airport subject = MainList.get(i);

                    if (subject.toString().toLowerCase().contains(charSequence))

                        arrayList1.add(subject);
                }
                filterResults.count = arrayList1.size();

                filterResults.values = arrayList1;
            } else {
                synchronized (this) {
                    filterResults.values = MainList;

                    filterResults.count = MainList.size();
                }
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            SubjectListTemp = (ArrayList<Airport>) filterResults.values;

            notifyDataSetChanged();

            clear();

            for (int i = 0, l = SubjectListTemp.size(); i < l; i++)
                add(SubjectListTemp.get(i));

            notifyDataSetInvalidated();
        }
    }


}
