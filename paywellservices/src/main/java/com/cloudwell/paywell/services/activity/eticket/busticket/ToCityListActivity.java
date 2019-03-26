package com.cloudwell.paywell.services.activity.eticket.busticket;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.busticket.model.City;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.listener.RecyclerItemClickListener;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ToCityListActivity extends AppCompatActivity {
    private MenuItem searchItem;
    public static final String CITY_ID_FROM = "cityIdFrom";
    public static String CITY_NAME = "cityName";
    public static String CITY_ID = "cityId";
    private static final String TAG_CONTACTS = "response_data";
    private static final String TAG_ID = "destination_id";
    private static final String TAG_NAME = "destination_name";

    private List<City> mCities;
    private SearchView searchView;
    private String cityIdFrom;
    private ProgressBar mProgressBar;
    private boolean isSearchGridView;
    private ArrayList<City> mSortedCities;
    private RelativeLayout mRelativeLayout;
    private GridLayoutManager mGridLayoutManager;
    private RecyclerView mRecyclerView;
    private FromCityAdapter mFromCityAdapter;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_city_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.to_city_msg);
        initView();
        initSearchView();
    }

    private void initView() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        mGridLayoutManager = new GridLayoutManager(ToCityListActivity.this, 2);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerGridView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mCities = new ArrayList<>();
        AppHandler mAppHandler = AppHandler.getmInstance(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cityIdFrom = extras.getString(CITY_ID_FROM);
        }

        ConnectionDetector cd = new ConnectionDetector(AppController.getContext());
        if (cd.isConnectingToInternet()) {
            new GetCityTo().execute(getResources().getString(R.string.bus_ticket_url),
                    "imei=" + mAppHandler.getImeiNo(),
                    "&mode=destinationlist",
                    "&from_id=" + cityIdFrom.toString(),
                    "&format=json",
                    "&securityKey=" + mAppHandler.getTicketToken());
        } else {
            Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

    private void initSearchView() {
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(1000);

        android.support.v7.widget.SearchView.SearchAutoComplete searchAutoComplete = (android.support.v7.widget.SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        // Collapse the search menu when the user hits the back key
        searchAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    showSearch(false);
                }
            }
        });
        try {
            // This sets the cursor
            // resource ID to 0 or @null
            // which will make it visible
            // on white background
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchAutoComplete, 0);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void showSearch(boolean visible) {
        if (visible)
            MenuItemCompat.expandActionView(searchItem);
        else
            MenuItemCompat.collapseActionView(searchItem);
    }

    /**
     * Called when the hardware search button is pressed
     */
    @Override
    public boolean onSearchRequested() {
        showSearch(true);
        // dont show the built-in search dialog
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showSearch(false);
        Bundle extras = intent.getExtras();
        String userQuery = String.valueOf(extras.get(SearchManager.USER_QUERY));
        if (userQuery.length() > 0) {
            performSearch(userQuery);
            isSearchGridView = true;
        } else {
            mFromCityAdapter = new FromCityAdapter(mCities, listener);
            mRecyclerView.setAdapter(mFromCityAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        searchItem = menu.add(android.R.string.search_go);
        searchItem.setIcon(R.drawable.ic_search_white_36dp);
        MenuItemCompat.setActionView(searchItem, searchView);
        MenuItemCompat.setShowAsAction(searchItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetCityTo extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String jsonStr = null;
            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3] + params[4] + params[5]);
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                jsonStr = client.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    String response = jsonObj.getString("response_status");
                    if (response.equals("200")) {
                        JSONArray jsonArray = jsonObj.getJSONArray(TAG_CONTACTS);
                        // looping through All Contacts
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject cityObject = jsonArray.getJSONObject(i);
                            City city = new City();
                            city.setCityId(cityObject.getString(TAG_ID));
                            city.setCityName(cityObject.getString(TAG_NAME));
                            mCities.add(city);
                        }
                        Collections.sort(mCities,
                                new Comparator<City>() {
                                    @Override
                                    public int compare(City lhs, City rhs) {
                                        return lhs.getCityName().compareToIgnoreCase(rhs.getCityName());
                                    }
                                });

                        mFromCityAdapter = new FromCityAdapter(mCities, listener);
                        mRecyclerView.setAdapter(mFromCityAdapter);
                        // Dismiss the progress dialog
                        mProgressBar.setVisibility(View.GONE);
                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mProgressBar.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }

    } // end of GetCOntacts asyncTask

    private void performSearch(String searchText) {
        mSortedCities = new ArrayList<>();
        for (City perCity : mCities) {
            if (searchText.length() > 0) {
                if (perCity.getCityName().toLowerCase().contains(searchText.toLowerCase())) {
                    mSortedCities.add(perCity);
                }
            }
        }
        mFromCityAdapter = new FromCityAdapter(mSortedCities, listener);
        mRecyclerView.setAdapter(mFromCityAdapter);
        mFromCityAdapter.updateList();
    }

    private static class FromCityAdapter extends RecyclerView.Adapter<ToCityHolder> {

        List<City> mCities;
        private RecyclerItemClickListener.OnItemClickListener mOnItemClickCallback;

        public FromCityAdapter(List<City> cities, RecyclerItemClickListener.OnItemClickListener listener) {
            this.mCities = cities;
            this.mOnItemClickCallback = listener;
        }

        public void updateList() {
            notifyDataSetChanged();
        }

        @Override
        public ToCityHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_list_item, null);
            return new ToCityHolder(v);
        }

        @Override
        public void onBindViewHolder(ToCityHolder fromCityHolder, int position) {
            String str = mCities.get(position).getCityName();
            fromCityHolder.mCityName.setText(str);
            fromCityHolder.cardView.setOnClickListener(new RecyclerItemClickListener(position, mOnItemClickCallback));
        }

        @Override
        public int getItemCount() {
            return mCities.size();
        }
    }

    private static class ToCityHolder extends RecyclerView.ViewHolder {

        private final CardView cardView;
        protected TextView mCityName;

        public ToCityHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.cardView);
            this.mCityName = (TextView) view.findViewById(R.id.grid_city_name);
            this.mCityName.setTypeface(AppController.getInstance().getOxygenLightFont());
        }

    }

    private RecyclerItemClickListener.OnItemClickListener listener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClicked(View view, int position) {
            Intent intent = new Intent();
            if (!isSearchGridView) {
                intent.putExtra(CITY_NAME, mCities.get(position).getCityName());
                intent.putExtra(CITY_ID, String.valueOf(mCities.get(position).getCityId()));
            } else {
                intent.putExtra(CITY_NAME, mSortedCities.get(position).getCityName());
                intent.putExtra(CITY_ID, String.valueOf(mSortedCities.get(position).getCityId()));
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    };


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ToCityListActivity.this, BusMainActivity.class);
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
