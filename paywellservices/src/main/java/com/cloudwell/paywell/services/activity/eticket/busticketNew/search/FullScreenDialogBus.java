package com.cloudwell.paywell.services.activity.eticket.busticketNew.search;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TableLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.CityName;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.BusLunCityRequest;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.CitiesListItem;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.search.view.ICitySerach;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.search.viewModel.CitySearchViewModel;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;
import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by YASIN on 24,June,2019
 * Email: yasinenubd5@gmail.com
 */
public class FullScreenDialogBus extends DialogFragment implements View.OnClickListener, ICitySerach {

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    public static String TAG = "FullScreenDialog";
    private TextView dhakaCityTV, coxBazarCityTV, benapoleCityTV, kolkataCityTV, chittagong, sylhetCityTV;
    private RecyclerView cityRecyclerView;

    OnCitySet onCitySet;
    private String toOrFrom;
    private EditText citySearchET;
    private TableLayout predefineDataTL;
    private CustomAdapter customAdapter;

    CitySearchViewModel viewMode;
    List<CitiesListItem> cityList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.full_screen_dialog, container, false);

        onCitySet = (OnCitySet) getActivity();
        predefineDataTL = view.findViewById(R.id.preDefinedDataTL);
        citySearchET = view.findViewById(R.id.citySearchET);
        cityRecyclerView = view.findViewById(R.id.cityRecyclerView);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        dhakaCityTV = view.findViewById(R.id.dhakaCityTV);
        coxBazarCityTV = view.findViewById(R.id.coxBazarCityTV);
        benapoleCityTV = view.findViewById(R.id.benapoleCityTV);
        kolkataCityTV = view.findViewById(R.id.kolkataCityTV);
        chittagong = view.findViewById(R.id.chittagongCityTV);
        sylhetCityTV = view.findViewById(R.id.sylhetCityTV);
        dhakaCityTV.setOnClickListener(this);
        coxBazarCityTV.setOnClickListener(this);
        benapoleCityTV.setOnClickListener(this);
        kolkataCityTV.setOnClickListener(this);
        chittagong.setOnClickListener(this);
        sylhetCityTV.setOnClickListener(this);

        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        toolbar.setBackgroundColor(getResources().getColor(R.color.color_tab_background_bus));
        toOrFrom = getArguments().getString(BusCitySearchActivity.FullSCREEN_DIALOG_HEADER, "Search Transport");
        toolbar.setTitle(toOrFrom);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        cityRecyclerView.setLayoutManager(gridLayoutManager);


        actionSearch();
        initViewModel();


        return view;
    }

    private void actionSearch() {
        citySearchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    predefineDataTL.setVisibility(View.INVISIBLE);
                    cityRecyclerView.setVisibility(View.VISIBLE);
                    customAdapter.getFilter().filter(charSequence);

                } else {
                    predefineDataTL.setVisibility(View.VISIBLE);
                    cityRecyclerView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void initViewModel() {
         viewMode = ViewModelProviders.of(this).get(CitySearchViewModel.class);
        viewMode.setIbusTransportListView(this);

        AppHandler mAppHandler =  AppHandler.getmInstance(getContext().getApplicationContext());

        BusLunCityRequest pojo = new BusLunCityRequest();
        pojo.setDeviceId(mAppHandler.getAndroidID());
        pojo.setUsername(mAppHandler.getUserName());

        if (AppController.isBusTicket){
            pojo.setTransportType("1");
        }else {
            pojo.setTransportType("0");
        }



       viewMode.getbusAndLaunchCities(pojo);


    }

    @Override
    public void generateCitylist(@Nullable List<CitiesListItem> it) {
        Log.e("", "");

        this.cityList = it;
        customAdapter = new CustomAdapter(getActivity(), it, onCitySet, toOrFrom, this);
        cityRecyclerView.setAdapter(customAdapter);
    }

    @Override
    public void showProgress() {
        BusCitySearchActivity activity = (BusCitySearchActivity) getActivity();
        activity.showProgress();
    }

    @Override
    public void hiddenProgress() {
        BusCitySearchActivity activity = (BusCitySearchActivity) getActivity();
        activity.dismissProgressDialog();

    }

    @Override
    public void showNoInternetConnectionFound() {

    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onClick(View view) {
        CitiesListItem citiesListItem;

        switch (view.getId()) {
            case R.id.dhakaCityTV:

                citiesListItem = new CitiesListItem( "44", CityName.DHAKA_CITY);
                onCitySet.setCityData(citiesListItem, toOrFrom);
                setCityDataToSP(toOrFrom, citiesListItem);
                dismiss();
                break;
            case R.id.coxBazarCityTV:
                 citiesListItem = new CitiesListItem( "42", CityName.COXBAZAR_CITY);
                 onCitySet.setCityData(citiesListItem, toOrFrom);
                 setCityDataToSP(toOrFrom, citiesListItem);
                 dismiss();
                break;
            case R.id.benapoleCityTV:
                citiesListItem = new CitiesListItem( "22", CityName.BENAPOLE_CITY);
                onCitySet.setCityData(citiesListItem, toOrFrom);
                setCityDataToSP(toOrFrom, citiesListItem);
                dismiss();
                break;
            case R.id.kolkataCityTV:
                citiesListItem = new CitiesListItem( "82", CityName.KOLKATA_CITY);
                onCitySet.setCityData(citiesListItem, toOrFrom);
                setCityDataToSP(toOrFrom, citiesListItem);
                dismiss();
                break;
            case R.id.chittagongCityTV:
                citiesListItem = new CitiesListItem( "38", CityName.CHITTAGONG_CITY);
                onCitySet.setCityData(citiesListItem, toOrFrom);
                setCityDataToSP(toOrFrom, citiesListItem);
                dismiss();
                break;
            case R.id.sylhetCityTV:
                citiesListItem = new CitiesListItem( "168", CityName.SYLHET_CITY);
                onCitySet.setCityData(citiesListItem, toOrFrom);
                setCityDataToSP(toOrFrom, citiesListItem);
                dismiss();
                break;
        }
    }

    private void setCityDataToSP(String toOrFrom, CitiesListItem city) {
        if (toOrFrom.equals(BusCitySearchActivity.FROM_STRING)) {
            AppStorageBox.put(getActivity(), AppStorageBox.Key.BUS_LEAVING_FROM_CITY, new Gson().toJson(city));
        } else {
            AppStorageBox.put(getActivity(), AppStorageBox.Key.BUS_GOING_TO_CITY, new Gson().toJson(city));
        }
    }

    public interface OnCitySet {
        void setCityData(CitiesListItem citiesListItem, String toOrFrom);
    }

    public  class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements Filterable {

        Context context;
        ArrayList<CitiesListItem> cityArrayList;
        OnCitySet onCitySet;
        String toOrFrom;
        FullScreenDialogBus fullScreenDialogBus;

        CustomAdapter(Context context, List<CitiesListItem> cityArrayList, OnCitySet onCitySet, String toOrFrom, FullScreenDialogBus fullScreenDialogBus) {
            this.context = context;
            this.cityArrayList = (ArrayList<CitiesListItem>) cityArrayList;
            this.onCitySet = onCitySet;
            this.toOrFrom = toOrFrom;
            this.fullScreenDialogBus = fullScreenDialogBus;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item_bus, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


            holder.textView.setText(String.format("%s", cityArrayList.get(position).getCitiesName()));
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCitySet.setCityData(cityArrayList.get(position), toOrFrom);
                    setCityDataToSP(toOrFrom, cityArrayList.get(position));
                    fullScreenDialogBus.dismiss();
                }
            });

        }


        @Override
        public int getItemCount() {
            return cityArrayList.size();
        }

        public void clear() {
            cityArrayList.clear();
            notifyDataSetChanged();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;// init the item view's

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.cityNameTV);
            }
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults = new FilterResults();
                    if (charSequence != null) {
                        String filterString = charSequence.toString().toLowerCase();
                        final List<CitiesListItem> list = cityList;
                        int count = list.size();
                        final ArrayList<CitiesListItem> tempList = new ArrayList<CitiesListItem>();

                        String filterableString;

                        for (int i = 0; i < count; i++) {
                            filterableString = list.get(i).getCitiesName();
                            if (filterableString.toLowerCase().startsWith(filterString.toLowerCase())) {
                                tempList.add(list.get(i));
                            }
                        }
                        filterResults.values = tempList;
                        filterResults.count = tempList.size();

                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    if (filterResults != null && (filterResults.count > 0)) {
                        notifyDataSetChanged();
                    }
                    if (filterResults.values != null) {
                        cityArrayList = (ArrayList<CitiesListItem>) filterResults.values;
                    } else {
                        cityArrayList = null;
                    }
                    notifyDataSetChanged();
                }
            };
        }


    }

}


