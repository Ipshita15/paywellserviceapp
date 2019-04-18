package com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.Airport;
import com.cloudwell.paywell.services.utils.FormatHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 24/3/19.
 */
public class AirportSerachAdapter extends ArrayAdapter<Airport> {

    public ArrayList<Airport> MainList;

    public ArrayList<Airport> SubjectListTemp;

    public AirportSerachAdapter.SubjectDataFilter subjectDataFilter;


    public AirportSerachAdapter(Context context, int id, ArrayList<Airport> subjectArrayList) {

        super(context, id, subjectArrayList);

        this.SubjectListTemp = new ArrayList<Airport>();

        this.SubjectListTemp.addAll(subjectArrayList);

        this.MainList = new ArrayList<Airport>();

        this.MainList.addAll(subjectArrayList);

    }

    @Override
    public Filter getFilter() {

        if (subjectDataFilter == null) {

            subjectDataFilter = new AirportSerachAdapter.SubjectDataFilter();
        }
        return subjectDataFilter;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AirportSerachAdapter.ViewHolder holder = null;

        if (convertView == null) {

            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.custom_layout, null);

            holder = new AirportSerachAdapter.ViewHolder();

            holder.SubjectName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvCity = convertView.findViewById(R.id.tvCity);

            convertView.setTag(holder);

        } else {
            holder = (AirportSerachAdapter.ViewHolder) convertView.getTag();
        }

        Airport subject = SubjectListTemp.get(position);

        holder.SubjectName.setText(FormatHelper.formatText(subject.getAirportName()));
        holder.tvCity.setText((FormatHelper.formatText(subject.getCity() + "/" + subject.getCountry())));

//        holder.SubjectFullForm.setText(subject.getSubFullForm());

        return convertView;

    }

    public class ViewHolder {

        TextView SubjectName, tvCity;
        TextView SubjectFullForm;
    }

    private class SubjectDataFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {


            charSequence = charSequence.toString().toLowerCase();

            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (charSequence == null || charSequence.length() == 0) {
                // No filter implemented we return all the list
                results.values = MainList;
                results.count = MainList.size();
            } else {
                // We perform filtering operation
                List<Airport> nPlanetList = new ArrayList<Airport>();

                for (Airport p : MainList) {
                    if (p.getAirportName().toLowerCase().startsWith(charSequence.toString()) || p.getIata().toLowerCase().startsWith(charSequence.toString()) || p.getCountry().toLowerCase().startsWith(charSequence.toString()) || p.getCity().toLowerCase().startsWith(charSequence.toString()) || p.getIso().toLowerCase().startsWith(charSequence.toString())) {
                        nPlanetList.add(p);
                    }
                }
                results.values = nPlanetList;
                results.count = nPlanetList.size();
            }


            return results;

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
