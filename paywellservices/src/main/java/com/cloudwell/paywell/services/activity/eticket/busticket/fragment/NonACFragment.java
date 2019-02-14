package com.cloudwell.paywell.services.activity.eticket.busticket.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.busticket.SeatListActivity;
import com.cloudwell.paywell.services.activity.eticket.busticket.model.SearchBus;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.listener.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class NonACFragment extends Fragment {
    private static final String AVAILABLE_SEAT = " Seats Available";
    private AppHandler mAppHandler;
    private TextView _errorMsg;
    private TextView _dateView;
    private ArrayList<SearchBus> mBusList;
    private RecyclerView mRecyclerView;
    private SearchBusAdapter mSearchBusAdapter;
    private GridLayoutManager mGridLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bus_list, container, false);
        _dateView = (TextView) rootView.findViewById(R.id.searchDate);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerBusListView);
        mRecyclerView.setHasFixedSize(true);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        _errorMsg = (TextView) rootView.findViewById(R.id.errorMsg);

        _dateView.setTypeface(AppController.getInstance().getOxygenLightFont());
        _errorMsg.setTypeface(AppController.getInstance().getOxygenLightFont());

        mAppHandler = AppHandler.getmInstance(getContext().getApplicationContext());
        mBusList = new ArrayList<>();
        setRetainInstance(true);
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatTarget = new SimpleDateFormat(getResources().getString(R.string.date_format));
            String str_output = formatTarget.format(format.parse(mAppHandler.getJourneyDate()));
            _dateView.setText(str_output);
            _dateView.setVisibility(View.VISIBLE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        new NonACBusAsync(rootView, mBusList).execute(mAppHandler.getSearchBus());
        return rootView;
    }

    private class NonACBusAsync extends AsyncTask<String, List<SearchBus>, List<SearchBus>> {

        private View rootView;
        private List<SearchBus> busList;

        public NonACBusAsync(View rootView, ArrayList<SearchBus> busList) {
            this.rootView = rootView;
            this.busList = busList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<SearchBus> doInBackground(String... params) {
            if (params[0] != null) {
                try {
                    JSONObject jsonObj = new JSONObject(params[0]);
                    String status = jsonObj.getString("response_status");
                    if (status.equals("200")) {
                        mAppHandler.setReceiptNo(jsonObj.getString("trx_id"));
                        JSONArray jsonArray = jsonObj.getJSONArray("response_data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject row = jsonArray.getJSONObject(i);
                            String _availableSeat = row.getString("avalableSeats");
                            String _seatType = row.getString("seat_types_id");
                            String _fares = row.getString("fares");
                            String _coachType = row.getString("coach_type");
                            String _companyName = row.getString("company_name");
                            String _arrivalTime = row.getString("arrival_time");
                            String _optionId = row.getString("option_id");

                            if (Integer.valueOf(_availableSeat) > 0) {
                                SearchBus searchBus = new SearchBus();
                                if (_coachType.equalsIgnoreCase("Non Ac")) {
                                    searchBus.setAvailableSeats(_availableSeat);
                                    searchBus.setSeatsType(_seatType);
                                    searchBus.setFare(_fares);
                                    searchBus.setCoachType(_coachType);
                                    searchBus.setCompanyName(_companyName);
                                    searchBus.setArrivalTime(_arrivalTime);
                                    searchBus.setOptionID(_optionId);
                                    busList.add(searchBus);
                                }
                            }
                        }

                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Snackbar snackbar = Snackbar.make(rootView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                snackbar.show();
            }
            return busList;
        }

        @Override
        protected void onPostExecute(List<SearchBus> result) {
            if (result.size() != 0) {
                mSearchBusAdapter = new SearchBusAdapter(busList, listener);
                mRecyclerView.setAdapter(mSearchBusAdapter);
            } else {
                _dateView.setVisibility(View.GONE);
                _errorMsg.setVisibility(View.VISIBLE);
            }
        }
    }

    private static class SearchBusAdapter extends RecyclerView.Adapter<SearchBusHolder> {

        List<SearchBus> mBuses;
        private RecyclerItemClickListener.OnItemClickListener mOnItemClickCallback;

        public SearchBusAdapter(List<SearchBus> buses, RecyclerItemClickListener.OnItemClickListener listener) {
            this.mBuses = buses;
            this.mOnItemClickCallback = listener;
        }

        @Override
        public SearchBusHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_list_item, null);
            return new SearchBusHolder(v);
        }

        @Override
        public void onBindViewHolder(SearchBusHolder searchBusHolder, int position) {
            SearchBus _aCompany = mBuses.get(position);
            searchBusHolder.busName.setText(_aCompany.getCompanyName());
            searchBusHolder.seatType.setText(_aCompany.getSeatsType());
            searchBusHolder.arrivalTime.setText(_aCompany.getArrivalTime());
            searchBusHolder.availableSeat.setText(_aCompany.getAvailableSeats() + AVAILABLE_SEAT);
            searchBusHolder.coachType.setText(_aCompany.getCoachType());
            searchBusHolder.cardView.setOnClickListener(new RecyclerItemClickListener(position, mOnItemClickCallback));
        }

        @Override
        public int getItemCount() {
            return mBuses.size();
        }
    }

    private static class SearchBusHolder extends RecyclerView.ViewHolder {

        private final CardView cardView;
        private TextView busName;
        private TextView seatType;
        private TextView arrivalTime;
        private TextView availableSeat;
        private TextView coachType;

        public SearchBusHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.cardView);
            this.busName = (TextView) view.findViewById(R.id.tvBusName);
            this.seatType = (TextView) view.findViewById(R.id.seatType);
            this.arrivalTime = (TextView) view.findViewById(R.id.arrivalTime);
            this.availableSeat = (TextView) view.findViewById(R.id.availableSeat);
            this.coachType = (TextView) view.findViewById(R.id.coachType);

            this.busName.setTypeface(AppController.getInstance().getOxygenLightFont());
            this.seatType.setTypeface(AppController.getInstance().getOxygenLightFont());
            this.arrivalTime.setTypeface(AppController.getInstance().getOxygenLightFont());
            this.availableSeat.setTypeface(AppController.getInstance().getOxygenLightFont());
            this.coachType.setTypeface(AppController.getInstance().getOxygenLightFont());
        }
    }

    private RecyclerItemClickListener.OnItemClickListener listener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClicked(View view, int position) {
            Intent intent = new Intent(getActivity(), SeatListActivity.class);
            intent.putExtra(SeatListActivity.OPTION_ID, mBusList.get(position).getOptionID());
            intent.putExtra(SeatListActivity.COMPANY_NAME, mBusList.get(position).getCompanyName());
            intent.putExtra(SeatListActivity.ARRIVAL_TIME, mBusList.get(position).getArrivalTime());
            intent.putExtra(SeatListActivity.COACH_TYPE, mBusList.get(position).getCoachType());
            intent.putExtra(SeatListActivity.FRAGMENT_KEY, SeatListActivity.NON_AC);
            startActivity(intent);
            getActivity().finish();
        }
    };
}

