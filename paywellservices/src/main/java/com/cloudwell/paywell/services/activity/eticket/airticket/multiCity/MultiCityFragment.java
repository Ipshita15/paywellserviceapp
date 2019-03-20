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
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.airticket.ClassBottomSheetDialog;
import com.cloudwell.paywell.services.activity.eticket.airticket.ClassModel;
import com.cloudwell.paywell.services.activity.eticket.airticket.PassengerBottomSheetDialog;
import com.cloudwell.paywell.services.activity.eticket.airticket.SearchRoundTripModel;
import com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.FlightSearchViewActivity;
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.AirportsSearchActivity;
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.model.Airport;
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.RequestAirSearch;
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.Segment;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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
    private SearchRoundTripModel searchRoundTripModel;
    String value = "1";
    private HashMap<Integer, String> flightDates = new HashMap<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addNoFlag = 0;
        View mainView = inflater.inflate(R.layout.fragment_multi_city, container, false);
        initView(mainView);
        return mainView;
    }

    private void initView(View parentView) {
        mainLayout = parentView.findViewById(R.id.flightListContainerLL);
        buttonSearch = parentView.findViewById(R.id.btn_search);
        passengerQuantityLL = parentView.findViewById(R.id.passengerQuantityLL);
        slideInAnim.setDuration(30);
        slideOutAnim.setDuration(30);

        addAnotherFlightBtn = parentView.findViewById(R.id.btn_add_another_flight);
        addAnotherFlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addNoFlag < AppHandler.MULTI_CITY_LIMIT) {
                addAnotherNo();
                } else {
                    Snackbar snackbar = Snackbar.make(parentView.findViewById(R.id.multiCityMainFL), R.string.multicity_limit_msg, Snackbar.LENGTH_LONG);
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
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSameCity = false;
                boolean isDateNotAvailable = false;
                long adult = Long.parseLong(((TextView) parentView.findViewById(R.id.airTicketAdult)).getText().toString());
                long child = Long.parseLong(((TextView) parentView.findViewById(R.id.airTicketKid)).getText().toString());
                long infant = Long.parseLong(((TextView) parentView.findViewById(R.id.airTicketInfant)).getText().toString());
                ArrayList<Segment> segments = new ArrayList<>();
                RequestAirSearch requestAirSearch = new RequestAirSearch(adult, child, infant, "MultiStop", segments);

                for (int a = 1; a <= addNoFlag; a++) {
                    Segment segment = new Segment("", "", "", "");
                    View flightView = getActivity().findViewById(android.R.id.content).findViewWithTag(a);
                    TextSwitcher from = flightView.findViewById(R.id.tsMultiCityTripFrom);
                    TextSwitcher to = flightView.findViewById(R.id.tsMultiCityTripTo);
                    TextView date = flightView.findViewById(R.id.tvDepartDate);
                    TextView cabinClass = flightView.findViewById(R.id.airTicketClass);
                    if ((((TextView) from.getCurrentView()).getText().toString().
                            equalsIgnoreCase(((TextView) to.getCurrentView()).getText().toString())) ||
                            ((TextView) from.getCurrentView()).getText().toString().equals("From") ||
                            ((TextView) to.getCurrentView()).getText().toString().equals("To")) {
//                        ((TextView) from.getCurrentView()).setError("Same City!!!");
//                        ((TextView) to.getCurrentView()).setError("Same City!!!");
                        isSameCity = true;
                        break;
                    } else {
//                        ((TextView) from.getCurrentView()).setError(null);
//                        ((TextView) to.getCurrentView()).setError(null);
                        segment.setOrigin(((TextView) from.getCurrentView()).getText().toString());
                        segment.setDestination(((TextView) to.getCurrentView()).getText().toString());
                        isSameCity = false;
                    }
                    if (date.getText().toString().isEmpty() || date.getText().toString().equalsIgnoreCase("Date")) {
//                        date.setError("Date not Found!!!");
                        isDateNotAvailable = true;
                        break;
                    } else {
//                        date.setError(null);


                        segment.setDepartureDateTime(flightDates.get(a));
                        isDateNotAvailable = false;
                    }
                    segment.setCabinClass(((TextView) flightView.findViewById(R.id.airTicketClass)).getText().toString());
                    segments.add(segment);
                }
                requestAirSearch.setSegments(segments);
                if (isSameCity || isDateNotAvailable) {
//                    Toast.makeText(getContext(), "Please provide all the data.", Toast.LENGTH_SHORT).show();
                } else {
                    AppStorageBox.put(getContext(), AppStorageBox.Key.REQUEST_AIR_SERACH, requestAirSearch);
                    Intent intent = new Intent(getActivity().getApplicationContext(), FlightSearchViewActivity.class);
                    startActivity(intent);

                }
            }
        });
        inflater = getLayoutInflater();
        addAnotherNo();
        addAnotherNo();
    }

    private void addAnotherNo() {
        ++addNoFlag;
        View flightView = inflater.inflate(R.layout.multi_city_list_item_view, null);
        ImageView removeBtn = flightView.findViewById(R.id.flightCancelIV);
        LinearLayout passengerClassLL = flightView.findViewById(R.id.passengerClassLL);
        LinearLayout departViewLL = flightView.findViewById(R.id.departViewLL);
        LinearLayout fromLL = flightView.findViewById(R.id.llFrom);
        LinearLayout toLL = flightView.findViewById(R.id.toLL);
        TextView tvDepartDate = flightView.findViewById(R.id.tvDepartDate);
        TextView flightNumberTV = flightView.findViewById(R.id.flightNumberTV);

        initChildView(flightView);

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
                    public void onButtonClickListener(@NotNull ClassModel classModel) {
                        ((TextView) flightView.findViewById(R.id.airTicketClass)).setText(classModel.getClassName());
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
                showDatePicker(tvDepartDate, (int) flightView.getTag());
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
        toLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AirportsSearchActivity.class);
                value = flightNumberTV.getText().toString();
                intent.putExtra("from", value);
                intent.putExtra("isTo", true);
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

    private void showDatePicker(TextView textView, int position) {

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
                String humanReadAbleDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime());

                flightDates.put(position, humanReadAbleDate);

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
                boolean isTo = data.getBooleanExtra("isTo", false);
                Airport get = (Airport) AppStorageBox.get(getContext(), AppStorageBox.Key.AIRPORT);
//                View view = getActivity().findViewById(android.R.id.content).findViewById(position);
//                view.setBackgroundColor(Color.parseColor("#000000"));
//                ((TextSwitcher)view.findViewById(R.id.tsOneWayTripFrom)).setText(get.getCity());
//                ((TextView)mainLayout.getChildAt(position-1).findViewById(R.id.tvFrom)).setText("ttt");


                if (isTo) {
                    searchRoundTripModel.setToName(get.getIata());
                    searchRoundTripModel.setToPortName(get.getAirportName());
                    ((TextSwitcher) getActivity().findViewById(android.R.id.content).findViewWithTag(position).findViewById(R.id.tsMultiCityTripTo)).setText(searchRoundTripModel.getToName());
                    ((TextSwitcher) getActivity().findViewById(android.R.id.content).findViewWithTag(position).findViewById(R.id.tsMultiCityTripToPort)).setText(searchRoundTripModel.getToPortName());

                } else {
                    searchRoundTripModel.setFromName(get.getIata());
                    searchRoundTripModel.setFromPortName(get.getAirportName());
                    ((TextSwitcher) getActivity().findViewById(android.R.id.content).findViewWithTag(position).findViewById(R.id.tsMultiCityTripFrom)).setText(searchRoundTripModel.getFromName());
                    ((TextSwitcher) getActivity().findViewById(android.R.id.content).findViewWithTag(position).findViewById(R.id.tsMultiCityTripFromPort)).setText(searchRoundTripModel.getFromPortName());
                }
            }
        }
    }

    private void initChildView(View flightView) {

        TextSwitcher tsFrom = flightView.findViewById(R.id.tsMultiCityTripFrom);
        TextSwitcher tsFromPort = flightView.findViewById(R.id.tsMultiCityTripFromPort);
        TextSwitcher tsTo = flightView.findViewById(R.id.tsMultiCityTripTo);
        TextSwitcher tsToPort = flightView.findViewById(R.id.tsMultiCityTripToPort);
        ImageView ivSwitchTrip = flightView.findViewById(R.id.ivOneWayTripTextSwitcher);


        tsFrom.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                TextView switcherTextView = new TextView(getContext());
                switcherTextView.setTextSize(18);
                return switcherTextView;
            }
        });
        tsFromPort.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                TextView switcherTextView = new TextView(getContext());
                switcherTextView.setTextSize(10);
                return switcherTextView;
            }
        });
        tsTo.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                TextView switcherTextView = new TextView(getContext());
                switcherTextView.setTextSize(18);
                return switcherTextView;
            }
        });
        tsToPort.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                TextView switcherTextView = new TextView(getContext());
                switcherTextView.setTextSize(10);
                return switcherTextView;
            }
        });

        Animation inAnim = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.fade_in);
        Animation outAnim = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.fade_out);
        inAnim.setDuration(200);
        outAnim.setDuration(200);
        tsFrom.setInAnimation(inAnim);
        tsFrom.setOutAnimation(outAnim);
        tsFromPort.setInAnimation(inAnim);
        tsFromPort.setOutAnimation(outAnim);
        tsTo.setInAnimation(inAnim);
        tsTo.setOutAnimation(outAnim);
        tsToPort.setInAnimation(inAnim);
        tsToPort.setOutAnimation(outAnim);

        tsFrom.setText("From");
        tsFromPort.setText("Airport");
        tsTo.setText("To");
        tsToPort.setText("Airport");


        TextView textFrom = (TextView) tsFrom.getCurrentView();
        TextView textFromPort = (TextView) tsFromPort.getCurrentView();
        TextView textTo = (TextView) tsTo.getCurrentView();
        TextView textToPort = (TextView) tsToPort.getCurrentView();

        searchRoundTripModel = new SearchRoundTripModel(textFrom.getText().toString(), textTo.getText().toString(),
                textFromPort.getText().toString(), textToPort.getText().toString());

        ivSwitchTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tsFrom.setText(searchRoundTripModel.getToName());
                tsTo.setText(searchRoundTripModel.getFromName());
                String from = searchRoundTripModel.getFromName();
                searchRoundTripModel.setFromName(searchRoundTripModel.getToName());
                searchRoundTripModel.setToName(from);

                tsFromPort.setText(searchRoundTripModel.getToPortName());
                tsToPort.setText(searchRoundTripModel.getFromPortName());
                String to = searchRoundTripModel.getFromPortName();
                searchRoundTripModel.setFromPortName(searchRoundTripModel.getToPortName());
                searchRoundTripModel.setToPortName(to);
            }
        });
    }
}
