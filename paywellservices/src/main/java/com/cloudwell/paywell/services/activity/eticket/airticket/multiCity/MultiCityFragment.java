package com.cloudwell.paywell.services.activity.eticket.airticket.multiCity;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.airticket.ClassBottomSheetDialog;
import com.cloudwell.paywell.services.activity.eticket.airticket.PassengerBottomSheetDialog;
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.AirportsSearchActivity;
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.model.Airport;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class MultiCityFragment extends Fragment {
    private static final int REQ_CODE_MULTI_CITY = 184;
    private LinearLayout mainLayout, passengerQuantityLL;
    private FancyButton buttonSearch;
    private FancyButton addAnotherFlightBtn;
    private TranslateAnimation slideInAnim = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
            Animation.RELATIVE_TO_PARENT, -1, Animation.RELATIVE_TO_PARENT, 0);
    private TranslateAnimation slideOutAnim = new TranslateAnimation(
            Animation.REVERSE, 0, Animation.REVERSE, 0,
            Animation.REVERSE, 0, Animation.REVERSE, -1);
    private int addNoFlag = 0;
    private LayoutInflater inflater;
    private ArrayList<View> flightViewList = new ArrayList<>();
    View flightView;
    String value = "1";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addNoFlag = 0;
        View mainView = inflater.inflate(R.layout.fragment_multi_city, container, false);
        initView(mainView);
        return mainView;
    }

    private void initView(View view) {
        mainLayout = view.findViewById(R.id.flightListContainerLL);
        buttonSearch = view.findViewById(R.id.btn_search);
        passengerQuantityLL = view.findViewById(R.id.passengerQuantityLL);
        slideInAnim.setDuration(30);
        slideOutAnim.setDuration(30);

        addAnotherFlightBtn = view.findViewById(R.id.btn_add_another_flight);
        addAnotherFlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addNoFlag < AppHandler.MULTI_CITY_LIMIT) {
                addAnotherNo();
                } else {
                    Snackbar snackbar = Snackbar.make(view.findViewById(R.id.multiCityMainFL), R.string.multicity_limit_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
        passengerQuantityLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        inflater = getLayoutInflater();
        addAnotherNo();
        addAnotherNo();
    }

    private void addAnotherNo() {
        ++addNoFlag;
        flightView = inflater.inflate(R.layout.multi_city_list_item_view, null);
        ImageView removeBtn = flightView.findViewById(R.id.flightCancelIV);
        LinearLayout passengerClassLL = flightView.findViewById(R.id.passengerClassLL);
        LinearLayout departViewLL = flightView.findViewById(R.id.departViewLL);
        LinearLayout fromLL = flightView.findViewById(R.id.tvFrom);
        TextView tvDepartDate = flightView.findViewById(R.id.tvDepartDate);
        TextView flightNumberTV = flightView.findViewById(R.id.flightNumberTV);

        flightView.setId(addNoFlag);
        flightView.setTag(addNoFlag);
        flightViewList.add(flightView);

        if (addNoFlag <= 2) {
            removeBtn.setVisibility(View.GONE);
        }
        ((TextView) flightView.findViewById(R.id.flightNumberTV)).setText(String.valueOf(addNoFlag));
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = ((int) flightView.getTag()); i <= addNoFlag; i++) {
                    View view = getActivity().findViewById(android.R.id.content).findViewWithTag(i);
                    view.setTag(i - 1);
                    ((TextView) view.findViewById(R.id.flightNumberTV)).setText(String.valueOf(i - 1));
                }
                --addNoFlag;
//                Toast.makeText(getContext(), ""+String.valueOf(((int)flightView.getTag())), Toast.LENGTH_SHORT).show();


                flightView.animate().alpha(0).setDuration(30).withEndAction(new Runnable() {
                    public void run() {
                        // rRemove the view from the parent layout
                        mainLayout.removeView(flightView);
                    }
                });
            }
        });
        passengerClassLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("myClassName", ((TextView) flightView.findViewById(R.id.airTicketClass)).getText().toString());
                ClassBottomSheetDialog bottomSheetDialog = new ClassBottomSheetDialog();
                bottomSheetDialog.setOnClassListener(new ClassBottomSheetDialog.ClassBottomSheetListener() {
                    @Override
                    public void onButtonClickListener(@NotNull String text) {
                        ((TextView) flightView.findViewById(R.id.airTicketClass)).setText(text);
                    }
                });
                bottomSheetDialog.setArguments(b);
                bottomSheetDialog.show(getFragmentManager(), "classBottomSheet");
            }
        });
        passengerQuantityLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePassengerClick(view);
            }
        });

        departViewLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(tvDepartDate);
            }
        });
        fromLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AirportsSearchActivity.class);
                value = flightNumberTV.getText().toString();
                intent.putExtra("from", value);
                intent.putExtra("isTo", false);
                startActivityForResult(intent, REQ_CODE_MULTI_CITY);
            }
        });
        mainLayout.addView(flightView);
        flightView.setAnimation(slideInAnim);
    }

    private void handlePassengerClick(View view) {
        Bundle b = new Bundle();
        b.putString("myAdult", ((TextView) view.findViewById(R.id.airTicketAdult)).getText().toString());
        b.putString("myKid", ((TextView) view.findViewById(R.id.airTicketKid)).getText().toString());
        b.putString("myInfant", ((TextView) view.findViewById(R.id.airTicketInfant)).getText().toString());

        PassengerBottomSheetDialog passengerBottomSheet = new PassengerBottomSheetDialog();
        passengerBottomSheet.setmListenerPsngr(new PassengerBottomSheetDialog.PsngrBottomSheetListener() {
            @Override
            public void onAdultButtonClickListener(@NotNull String text) {
                ((TextView) view.findViewById(R.id.airTicketAdult)).setText(text);
            }

            @Override
            public void onKidButtonClickListener(@NotNull String text) {
                ((TextView) view.findViewById(R.id.airTicketKid)).setText(text);

            }

            @Override
            public void onInfantButtonClickListener(@NotNull String text) {
                ((TextView) view.findViewById(R.id.airTicketInfant)).setText(text);
            }
        });
        passengerBottomSheet.setArguments(b);
        passengerBottomSheet.show(getFragmentManager(), "psngrBottomSheet");
    }

    private void showDatePicker(TextView textView) {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int thismonth = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                Date date = calendar.getTime();

                String nameOfDayOfWeek = new SimpleDateFormat("EEE").format(date);
                String nameOfMonth = new SimpleDateFormat("MMM").format(calendar.getTime());

                textView.setText(nameOfDayOfWeek + "," + i2 + nameOfMonth);
                textView.setTextColor(Color.BLACK);
            }
        }, year, thismonth, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.MONTH, 6);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CODE_MULTI_CITY) {

                int position = Integer.parseInt(data.getStringExtra("from"));
                Airport get = (Airport) AppStorageBox.get(getContext(), AppStorageBox.Key.AIRPORT);
                View view = getActivity().findViewById(android.R.id.content).findViewById(position);
                view.setBackgroundColor(Color.parseColor("#000000"));
//                ((TextSwitcher)view.findViewById(R.id.tsOneWayTripFrom)).setText(get.getCity());

            }
        }
    }
}
