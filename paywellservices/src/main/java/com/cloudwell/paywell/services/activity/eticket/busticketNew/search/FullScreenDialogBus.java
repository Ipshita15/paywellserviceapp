package com.cloudwell.paywell.services.activity.eticket.busticketNew.search;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.cloudwell.paywell.services.app.storage.AppStorageBox;
import com.cloudwell.paywell.services.database.DatabaseClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YASIN on 24,June,2019
 * Email: yasinenubd5@gmail.com
 */
public class FullScreenDialogBus extends DialogFragment implements View.OnClickListener {

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    public static String TAG = "FullScreenDialog";
    private TextView dhakaCityTV,coxBazarCityTV,jessorCityTV,khulnaCityTV,chittagong, lalmonirhat,
            saidpur,mymensing,rangpur,thakurgaon,barisal,sylhet,comilla;
    private RecyclerView cityRecyclerView;

    OnCitySet onCitySet;
    private String toOrFrom;
    private Handler handler;
    private EditText citySearchET;
    private TableLayout predefineDataTL;
    static ArrayList<String> cityList=new ArrayList<>();
    private CustomAdapter customAdapter;


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
        predefineDataTL=view.findViewById(R.id.preDefinedDataTL);
        citySearchET=view.findViewById(R.id.citySearchET);
        cityRecyclerView=view.findViewById(R.id.cityRecyclerView);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        dhakaCityTV=view.findViewById(R.id.dhakaCityTV);
        coxBazarCityTV=view.findViewById(R.id.coxBazarCityTV);
        jessorCityTV=view.findViewById(R.id.jessorCityTV);
        khulnaCityTV=view.findViewById(R.id.khulnaCityTV);
        chittagong=view.findViewById(R.id.chittagongCityTV);
        lalmonirhat=view.findViewById(R.id.lalmonirhatCityTV);
        saidpur=view.findViewById(R.id.saidpurCityTV);
        mymensing=view.findViewById(R.id.mymensingCityTV);
        rangpur=view.findViewById(R.id.rangpurCityTV);
        thakurgaon=view.findViewById(R.id.thakurgaonCityTV);
        barisal=view.findViewById(R.id.barisalCityTV);
        sylhet=view.findViewById(R.id.sylheytCityTV);
        comilla=view.findViewById(R.id.comillaCityTV);
        dhakaCityTV.setOnClickListener(this);
        coxBazarCityTV.setOnClickListener(this);
        jessorCityTV.setOnClickListener(this);
        khulnaCityTV.setOnClickListener(this);
        chittagong.setOnClickListener(this);
        lalmonirhat.setOnClickListener(this);
        saidpur.setOnClickListener(this);
        mymensing.setOnClickListener(this);
        rangpur.setOnClickListener(this);
        thakurgaon.setOnClickListener(this);
        barisal.setOnClickListener(this);
        sylhet.setOnClickListener(this);
        comilla.setOnClickListener(this);
        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_tab_background_bus));
        toOrFrom=getArguments().getString(BusCitySearchActivity.FullSCREEN_DIALOG_HEADER,"Search Bus");
        toolbar.setTitle(toOrFrom);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        cityRecyclerView.setLayoutManager(gridLayoutManager);



        citySearchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                customAdapter.clear();
                if (charSequence.length() > 0 ) {
                    predefineDataTL.setVisibility(View.INVISIBLE);
                    cityRecyclerView.setVisibility(View.VISIBLE);
                    handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                    handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                            AUTO_COMPLETE_DELAY);
                }else {
                    predefineDataTL.setVisibility(View.VISIBLE);
                    cityRecyclerView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(citySearchET.getText().toString())) {
                        DatabaseAccessAsyncTask databaseAccessAsyncTask=new DatabaseAccessAsyncTask(getActivity(),cityRecyclerView);
                        databaseAccessAsyncTask.execute(citySearchET.getText().toString());
                    }
                }
                return false;
            }
        });

        customAdapter=new CustomAdapter(getActivity(),cityList,onCitySet,toOrFrom,this);
        cityRecyclerView.setAdapter(customAdapter);


        return view;
    }

    private class DatabaseAccessAsyncTask extends AsyncTask<String,Void,String>{
        private Context context;

        DatabaseAccessAsyncTask(Context context,RecyclerView recyclerView){
          this.context=context;
        }

        @Override
        protected String doInBackground(String... strings) {
            cityList=(ArrayList<String>) DatabaseClient.getInstance(context).getAppDatabase().mBusTicketDab().searchAvailableCityForBus();
            return strings[0];
        }

        @Override
        protected void onPostExecute(String searchString) {
            super.onPostExecute(searchString);
            customAdapter.getFilter().filter(searchString);
        }
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
        switch (view.getId()){
            case R.id.dhakaCityTV:
                onCitySet.setCityData(CityName.DHAKA_CITY,toOrFrom);
                setCityDataToSP(toOrFrom, CityName.DHAKA_CITY);
                dismiss();
                break;
            case R.id.coxBazarCityTV:
                onCitySet.setCityData(CityName.COXBAZAR_CITY,toOrFrom);
                setCityDataToSP(toOrFrom, CityName.COXBAZAR_CITY);
                dismiss();
                break;
            case R.id.jessorCityTV:
                onCitySet.setCityData(CityName.JESSOR_CITY,toOrFrom);
                setCityDataToSP(toOrFrom, CityName.JESSOR_CITY);
                dismiss();
                break;
            case R.id.khulnaCityTV:
                onCitySet.setCityData(CityName.KHULNA_CITY,toOrFrom);
                setCityDataToSP(toOrFrom, CityName.KHULNA_CITY);
                dismiss();
                break;
            case R.id.chittagongCityTV:
                onCitySet.setCityData(CityName.CHITTAGONG_CITY,toOrFrom);
                setCityDataToSP(toOrFrom, CityName.CHITTAGONG_CITY);
                dismiss();
                break;
            case R.id.lalmonirhatCityTV:
                onCitySet.setCityData(CityName.LALMONIRhat_CITY,toOrFrom);
                setCityDataToSP(toOrFrom, CityName.LALMONIRhat_CITY);
                dismiss();
                break;
            case R.id.saidpurCityTV:
                onCitySet.setCityData(CityName.SAIDPUR_CITY,toOrFrom);
                setCityDataToSP(toOrFrom, CityName.SAIDPUR_CITY);
                dismiss();
                break;
            case R.id.mymensingCityTV:
                onCitySet.setCityData(CityName.MYMENSING_CITY,toOrFrom);
                setCityDataToSP(toOrFrom, CityName.MYMENSING_CITY);
                dismiss();
                break;
            case R.id.rangpurCityTV:
                onCitySet.setCityData(CityName.RANGPUR_CITY,toOrFrom);
                setCityDataToSP(toOrFrom, CityName.RANGPUR_CITY);
                dismiss();
                break;
            case R.id.thakurgaonCityTV:
                onCitySet.setCityData(CityName.THAKURGAON_CITY,toOrFrom);
                setCityDataToSP(toOrFrom, CityName.THAKURGAON_CITY);
                dismiss();
                break;
            case R.id.barisalCityTV:
                onCitySet.setCityData(CityName.BARISAL_CITY,toOrFrom);
                setCityDataToSP(toOrFrom, CityName.BARISAL_CITY);
                dismiss();
                break;
            case R.id.sylheytCityTV:
                onCitySet.setCityData(CityName.SYLHET_CITY,toOrFrom);
                setCityDataToSP(toOrFrom, CityName.SYLHET_CITY);
                dismiss();
                break;
            case R.id.comillaCityTV:
                onCitySet.setCityData(CityName.COMILLA_CITY,toOrFrom);
                setCityDataToSP(toOrFrom, CityName.COMILLA_CITY);
                dismiss();
                break;
        }
    }

    private void setCityDataToSP(String toOrFrom, String city) {
        if (toOrFrom.equals(BusCitySearchActivity.FROM_STRING)) {
            AppStorageBox.put(getActivity(), AppStorageBox.Key.BUS_LEAVING_FROM_CITY, city);
        } else {
            AppStorageBox.put(getActivity(), AppStorageBox.Key.BUS_GOING_TO_CITY, city);
        }
    }

    public interface OnCitySet {
        void setCityData(String cityName, String toOrFrom);
    }

    public static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements Filterable {

        Context context;
        ArrayList<String> cityArrayList;
        OnCitySet onCitySet;
        String toOrFrom;
        FullScreenDialogBus fullScreenDialogBus;

        CustomAdapter(Context context, List<String> cityArrayList, OnCitySet onCitySet, String toOrFrom, FullScreenDialogBus fullScreenDialogBus){
            this.context=context;
            this.cityArrayList = (ArrayList<String>) cityArrayList;
            this.onCitySet=onCitySet;
            this.toOrFrom=toOrFrom;
            this.fullScreenDialogBus=fullScreenDialogBus;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item_bus, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.textView.setText(cityArrayList.get(position));
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCitySet.setCityData(holder.textView.getText().toString(),toOrFrom);
                    setCityDataToSP(toOrFrom, holder.textView.getText().toString());
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
                    FilterResults filterResults= new FilterResults();
                    if (charSequence != null) {
                        String filterString = charSequence.toString().toLowerCase();
                        final List<String> list = cityList;
                        int count = list.size();
                        final ArrayList<String> nlist = new ArrayList<String>();

                        String filterableString ;

                        for (int i = 0; i < count; i++) {
                            filterableString = list.get(i);
                            if (filterableString.toLowerCase().startsWith(filterString.toLowerCase())) {
                                nlist.add(filterableString);
                            }
                        }
                        filterResults.values=nlist;
                        filterResults.count=nlist.size();

                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    if (filterResults != null && (filterResults.count > 0)) {
                        notifyDataSetChanged();
                    }
                    if (filterResults.values != null) {
                        cityArrayList = (ArrayList<String>) filterResults.values;
                    } else {
                        cityArrayList = null;
                    }
                    notifyDataSetChanged();
                }
            };
        }

        private void setCityDataToSP(String toOrFrom, String city) {
            if (toOrFrom.equals(BusCitySearchActivity.FROM_STRING)) {
                AppStorageBox.put(context, AppStorageBox.Key.BUS_LEAVING_FROM_CITY, city);
            } else {
                AppStorageBox.put(context, AppStorageBox.Key.BUS_GOING_TO_CITY, city);
            }
        }
    }

}


