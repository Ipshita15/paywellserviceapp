package com.cloudwell.paywell.services.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AppHandler {
    // Shared Preferences
    private SharedPreferences mPref;

    private SharedPreferences.Editor editor;

    // Shared mPref mode
    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "PayWellServices";
    public static final String KEY_WIMAX = "qubee";
    public static final String KEY_TYPE = "wrong_account";
    public static final String KEY_TYPE2 = "wrong_amount";
    public static final String TRX_TYPE = "recharge_not_received";
    public static final int MULTIPLE_TOPUP_LIMIT = 5;
    private static final String UPDATE_CHECK = "LastUpdateCheck";
    private static final String PIN = "pin";
    private static final String PW_BALANCE = "pwBalance";
    private static final String PW_RID = "pwRID";
    private static final String CITY_FROM = "cityFrom";
    private static final String CITY_ID_FROM = "cityIdFrom";
    private static final String CITY_TO = "cityTo";
    private static final String CITY_ID_TO = "cityIdTo";
    private static final String JOURNEY_DATE = "journeyDate";
    private static final String TICKET_TOKEN = "ticketToken";
    private static final String NUMBER_OF_SEAT = "numberOfSeat";
    private static final String SELECTED_SEAT_AND_PRICE = "selectedSeatAndPrice";
    private static final String SEAT_ID = "seatId";
    private static final String SEAT_NO = "seatNo";
    private static final String FARE = "fare";
    private static final String RECEIPT_NO = "receiptNo";
    private static final String BUS_LIST = "busList";
    private static final String IMEI_NO = "imeiNo";
    private static final String SOURCE_CODES_SIZE_ = "sourceCodeSize_";
    private static final String SOURCE_CODES_ = "sourceCodes_";
    private static final String SOURCE_SIZE_ = "sourceSize_";
    private static final String SOURCE_ = "sources_";
    private static final String UNKNOWN_SOURCE = "unknownSources";
    private static final String SOURCE_STATION = "sourceStation";
    private static final String UNKNOWN_SOURCE_STATION = "unknownSourceStation";
    private static final String SOURCE_STATION_CODE = "sourceStationCode";
    private static final String DESTINATION_NAME = "destinationName";
    private static final String DESTINATION_STATION_SIZE_ = "destinationStationSize_";
    private static final String DESTINATION_STATION_ = "destinationStation_";
    private static final String UNKNOWN_DESTINATION_STATION = "unknownDestinationStation";
    private static final String DESTINATION_STATION_CODE_SIZE_ = "destinationStationCodeSize_";
    private static final String DESTINATION_STATION_CODES_ = "destinationStationCodes_";
    private static final String UNKNOWN_DESTINATION_STATION_CODES = "unknownDestinationStationCode";
    private static final String PASSENGER_SIZE_ = "passengers_";
    private static final String PASSENGER_ = "passenger_";
    private static final String UNKNOWN_PASSENGER = "unknownPassenger";
    private static final String PASSENGER_CODE_SIZE_ = "passengerCodeSize_";
    private static final String PASSENGER_CODE = "passengerCode_";
    private static final String UNKNOWN_PASSENGER_CODE = "unknownPassengerCode";
    private static final String PASSENGER_TYPE_SIZE_ = "passengerTypeSize";
    private static final String PASSENGER_TYPE_ = "passengerType_";
    private static final String UNKNOWN_PASSENGER_TYPE = "unknownPassengerType";
    private static final String DESTINATION_STATION = "destinationStation";
    private static final String UNKNOWN_DESTINATION = "unknownDestination";
    private static final String DESTINATION_STATION_CODE = "destinationStationCode";
    private static final String UNKNOWN_DESTINATION_STATION_CODE = "unknownDestinationStationCode";
    private static final String TRAIN_NAME_SIZE_ = "trainNameSize_";
    private static final String TRAIN_NAME_ = "trainName_";
    private static final String UNKNOWN_TRAIN_NAME = "unknownTrainName";
    private static final String TRAIN_CODE_SIZE_ = "trainCodeSize_";
    private static final String TRAIN_CODE_ = "trainCode";
    private static final String UNKNOWN_TRAIN_CODE = "unknownTrainCode";
    private static final String CLASS_TYPE_SIZE_ = "classTypeSize_";
    private static final String CLASS_TYPE_ = "classType_";
    private static final String UNKNOWN_CLASS_TYPE = "unknownClassType";
    private static final String CLASS_TYPE_CODE_SIZE_ = "classTypeCodeSize_";
    private static final String CLASS_TYPE_CODE_ = "classTypeCode_";
    private static final String UNKNOWN_CLASS_TYPE_CODE = "unknownClassTypeCode";
    private static final String NUMBER_OF_PASSENGER = "numberOfPassenger";
    private static final String UNKNOWN_NUMBER_OF_PASSENGER = "unknownNumberOfPassenger";
    private static final String AGE_OF_PASSENGER = "ageOfPassenger";
    private static final String UNKNOWN_PASSENGER_AGE = "unknownPassengerAge";
    private static final String UNKNOWN_SOURCE_STATION_CODE = "unknownSourceStationCode";

    private static final String REG_DISTRICT_ARRAY = "district_array";
    public static Boolean REG_FLAG_ONE = false;
    public static Boolean REG_FLAG_TWO = false;
    public static Boolean REG_FLAG_THREE = false;

    private static final String MYCASH_OTP = "mycash_otp";
    private static final String MYCASH_BALANCE = "mycash_balance";

    private static final String APP_LANGUAGE = "en";
    private static final String APP_STATUS = "registered";

    private static final String USERNAME = "username";
    private static final String PHONE_NUMBER = "phone";

    private static final String QR_CODE_BITMAP = "bitmap";

    private static final String PHN_NUMBER_VERIFICATION_STATUS = "phn_num_status";
    private static final String MERCHANT_TYPE_VERIFICATION_STATUS = "merchant_type_status";
    private static final String PHN_NUMBER = "phn_num";
    private static final String PHN_UPDATE_CHECK = "phn_num_check_last_update";
    private static final String DAY_COUNT = "day_count";

    private static final String FIREBASE_ID = "firebase_id";
    private static final String FIREBASE_TOKEN_STATUS = "firebase_token_status";

    private static String INITIAL_CHANGE_PIN_STATUS = "initial_change_pin";

    private static final String LOCATION_UPDATE_CHECK = "last_location_update_check";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";

    private static final String GATEWAY_ID = "gateway_id";
    private static final String AGENT_PHN_NUM = "agent_phn_num";

    private static final String IMAGE_CACHE_CLEAN = "img_cache_clean";

    private static String DISPLAY_PICTURE_COUNT = "display_picture_count";
    private static String CENTER_AREA_DROP_DOWN_POSITION = "CENTER_AREA_DROP_DOWN_POSITION";

    private static final String DISPLAY_PICTURE_SIZE_ = "displayPictureSize";
    private static final String DISPLAY_PICTURE_ = "displaypicture_";
    private static final String UNKNOWN_DISPLAY_PICTURE = "unknownDisplayPicture";

    public AppHandler() {

    }

    public AppHandler(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = mPref.edit();
    }

    public ArrayList<String> getSources() {
        int size = mPref.getInt(SOURCE_SIZE_, PRIVATE_MODE);
        ArrayList<String> sources = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            sources.add(mPref.getString(SOURCE_ + i, UNKNOWN_SOURCE));
        return sources;
    }

    public void setSources(ArrayList<String> sources) {
        int size = mPref.getInt(SOURCE_SIZE_, PRIVATE_MODE);
        // clear the previous data if exists
        for (int i = 0; i < size; i++)
            editor.remove(SOURCE_ + i);
        // write the current list
        for (int i = 0; i < sources.size(); i++)
            editor.putString(SOURCE_ + i, sources.get(i));
        editor.putInt(SOURCE_SIZE_, sources.size());
        editor.commit();
    }
    public ArrayList<String> getSourceCodes() {
        int size = mPref.getInt(SOURCE_CODES_SIZE_, PRIVATE_MODE);
        ArrayList<String> sourceCodes = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            sourceCodes.add(mPref.getString(SOURCE_CODES_ + i, UNKNOWN_SOURCE));
        return sourceCodes;
    }

    public void setSourceCodes(ArrayList<String> sourceCodes) {
        int size = mPref.getInt(SOURCE_CODES_SIZE_, PRIVATE_MODE);
        // clear the previous data if exists
        for (int i = 0; i < size; i++)
            editor.remove(SOURCE_CODES_ + i);
        // write the current list
        for (int i = 0; i < sourceCodes.size(); i++)
            editor.putString(SOURCE_CODES_ + i, sourceCodes.get(i));
        editor.putInt(SOURCE_CODES_SIZE_, sourceCodes.size());
        editor.commit();
    }

    public String getImeiNo() {
        return mPref.getString(IMEI_NO, "unknown");
    }

    public void setImeiNo(String imeiNo) {
        editor.putString(IMEI_NO, imeiNo);
        editor.commit();
    }

    public String getPin() {
        return mPref.getString(PIN, "unknown");
    }

    public void setPin(String pin) {
        editor.putString(PIN, pin);
        editor.commit();
    }

    public String getMYCashBalance() {
        return mPref.getString(MYCASH_BALANCE, "unknown");
    }

    public void setMYCashBalance(String balance) {
        editor.putString(MYCASH_BALANCE, balance);
        editor.commit();
    }

    public String getMYCashOTP() {
        return mPref.getString(MYCASH_OTP, "unknown");
    }

    public void setMYCashOTP(String otp) {
        editor.putString(MYCASH_OTP, otp);
        editor.commit();
    }

    public long getUpdateCheck() {
        return mPref.getLong(UPDATE_CHECK, PRIVATE_MODE);
    }

    public void setUpdateCheck(long times) {
        editor.putLong(UPDATE_CHECK, times);
        editor.commit();
    }

    public void setTicketToken(String ticketToken) {
        editor.putString(TICKET_TOKEN, ticketToken);
        editor.commit();
    }

    public void setJourneyDate(String date) {
        editor.putString(JOURNEY_DATE, date);
        editor.commit();
    }

    public void setCityFrom(String cityTo) {
        editor.putString(CITY_FROM, cityTo);
        editor.commit();
    }

    public void setCityTo(String cityFrom) {
        editor.putString(CITY_TO, cityFrom);
        editor.commit();
    }

    public void setCityIdFrom(String cityIdFrom) {
        editor.putString(CITY_ID_FROM, cityIdFrom);
        editor.commit();
    }

    public void setCityIdTo(String cityIdTo) {
        editor.putString(CITY_ID_TO, cityIdTo);
        editor.commit();
    }

    public String getCityIdFrom() {
        return mPref.getString(CITY_ID_FROM, "unknown");
    }

    public String getCityIdTo() {
        return mPref.getString(CITY_ID_TO, "unknown");
    }

    public String getJourneyDate() {
        return mPref.getString(JOURNEY_DATE, "unknown");
    }

    public String getTicketToken() {
        return mPref.getString(TICKET_TOKEN, "unknown");
    }

    public void setReceiptNo(String receiptNO) {
        editor.putString(RECEIPT_NO, receiptNO);
    }

    public String getReceiptNo() {
        return mPref.getString(RECEIPT_NO, "unknown");
    }

    public String getCityFrom() {
        return mPref.getString(CITY_FROM, "unknown");
    }

    public String getCityTo() {
        return mPref.getString(CITY_TO, "unknown");
    }

    public void setSelectedSeatAndPrice(String numberOfSeat) {
        editor.putString(SELECTED_SEAT_AND_PRICE, numberOfSeat);
        editor.commit();
    }

    public String getSelectedSeatAndPrice() {
        return mPref.getString(SELECTED_SEAT_AND_PRICE, "unknown");
    }

    public void setNumberOfSeat(String numOfSeat) {
        editor.putString(NUMBER_OF_SEAT, numOfSeat);
    }

    public String getNumberOfSeat() {
        return mPref.getString(NUMBER_OF_SEAT, "unknown");
    }

    public void setSeatId(String seatId) {
        editor.putString(SEAT_ID, seatId);
        editor.commit();
    }

    public String getSeatId() {
        return mPref.getString(SEAT_ID, "unknown");
    }

    public void setSeatNo(String seatNo) {
        editor.putString(SEAT_NO, seatNo);
        editor.commit();
    }

    public String getSeatNo() {
        return mPref.getString(SEAT_NO, "unknown");
    }

    public void setFare(String fare) {
        editor.putString(FARE, fare);
        editor.commit();
    }

    public String getFare() {
        return mPref.getString(FARE, "fare");
    }

    public void setPWBalance(String balance) {
        editor.putString(PW_BALANCE, balance);
        editor.commit();
    }

    public void setSearchBus(String busList) {
        editor.putString(BUS_LIST, busList);
        editor.commit();
    }

    public String getSearchBus() {
        return mPref.getString(BUS_LIST, "unknown");
    }

    public String getPwBalance() {
        return mPref.getString(PW_BALANCE, "unknown");
    }

    public void setRID(String rid) {
        editor.putString(PW_RID, rid);
        editor.commit();
    }

    public String getRID() {
        return mPref.getString(PW_RID, "unknown");
    }

    public static void showDialog(FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        MyDialogFragment frag = new MyDialogFragment();
        frag.show(ft, "txn_tag");
    }

    public static String getCurrentDate() {
        String currentDate = null;
        try {
            Date todayDate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            currentDate = format.format(todayDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTime() {
        final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Dhaka"));
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");
        format.setTimeZone(c.getTimeZone());
        return format.format(c.getTime());
    }

    public static String timeStampFormat(String date) {
        String year = (String) date.subSequence(0, 4);
        String month = (String) date.subSequence(4, 6);
        String day = (String) date.subSequence(6, 8);
        String hr = (String) date.subSequence(8, 10);
        String min = (String) date.subSequence(10, 12);
        String sec = (String) date.subSequence(12, 14);
        return year + "-" + month + "-" + day + ' ' + ' ' + hr + "."
                + min + "." + sec;
    }

    public void setSourceStation(String mSource) {
        editor.putString(SOURCE_STATION, mSource);
        editor.commit();
    }

    public String getSourceStation() {
        return mPref.getString(SOURCE_STATION, UNKNOWN_SOURCE_STATION);
    }

    public void setSourceStationCode(String mSourceCode) {
        editor.putString(SOURCE_STATION_CODE, mSourceCode);
        editor.commit();
    }
    public String getSourceStationCode() {
        return mPref.getString(SOURCE_STATION_CODE, UNKNOWN_SOURCE_STATION_CODE);
    }
    public void setDestinationName(String mDestination) {
        editor.putString(DESTINATION_NAME, mDestination);
        editor.commit();
    }
    public String getDestinationName() {
        return mPref.getString(DESTINATION_NAME, UNKNOWN_DESTINATION);
    }
    public void setDestinationStations(ArrayList<String> destinationStations) {
        int size = mPref.getInt(DESTINATION_STATION_SIZE_, PRIVATE_MODE);
        // clear the previous data if exists
        for (int i = 0; i < size; i++)
            editor.remove(DESTINATION_STATION_ + i);
        // write the current list
        for (int i = 0; i < destinationStations.size(); i++)
            editor.putString(DESTINATION_STATION_ + i, destinationStations.get(i));
        editor.putInt(DESTINATION_STATION_SIZE_, destinationStations.size());
        editor.commit();
    }
    public ArrayList<String> getDestinationStations() {
        int size = mPref.getInt(DESTINATION_STATION_SIZE_, PRIVATE_MODE);
        ArrayList<String> stations = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            stations.add(mPref.getString(DESTINATION_STATION_ + i, UNKNOWN_DESTINATION_STATION));
        return stations;
    }

    public void setDestinationStationCodes(ArrayList<String> destinationStationCodes) {
        int size = mPref.getInt(DESTINATION_STATION_CODE_SIZE_, PRIVATE_MODE);
        // clear the previous data if exists
        for (int i = 0; i < size; i++)
            editor.remove(DESTINATION_STATION_CODES_ + i);
        // write the current list
        for (int i = 0; i < destinationStationCodes.size(); i++)
            editor.putString(DESTINATION_STATION_CODES_ + i, destinationStationCodes.get(i));
        editor.putInt(DESTINATION_STATION_CODE_SIZE_, destinationStationCodes.size());
        editor.commit();
    }
    public ArrayList<String> getDestinationStationCodes() {
        int size = mPref.getInt(DESTINATION_STATION_CODE_SIZE_, PRIVATE_MODE);
        ArrayList<String> stationCodes = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            stationCodes.add(mPref.getString(DESTINATION_STATION_CODES_ + i, UNKNOWN_DESTINATION_STATION_CODES));
        return stationCodes;
    }

    public void setPassengers(ArrayList<String> passengers) {
        int size = mPref.getInt(PASSENGER_SIZE_, PRIVATE_MODE);
        // clear the previous data if exists
        for (int i = 0; i < size; i++)
            editor.remove(PASSENGER_ + i);
        // write the current list
        for (int i = 0; i < passengers.size(); i++)
            editor.putString(PASSENGER_ + i, passengers.get(i));
        editor.putInt(PASSENGER_SIZE_, passengers.size());
        editor.commit();
    }
    public ArrayList<String> getPassengers() {
        int size = mPref.getInt(PASSENGER_SIZE_, PRIVATE_MODE);
        ArrayList<String> passengers = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            passengers.add(mPref.getString(PASSENGER_ + i, UNKNOWN_PASSENGER));
        return passengers;
    }

    public void setPassengerCodes(ArrayList<String> passengerCodes) {
        int size = mPref.getInt(PASSENGER_CODE_SIZE_, PRIVATE_MODE);
        // clear the previous data if exists
        for (int i = 0; i < size; i++)
            editor.remove(PASSENGER_CODE + i);
        // write the current list
        for (int i = 0; i < passengerCodes.size(); i++)
            editor.putString(PASSENGER_CODE + i, passengerCodes.get(i));
        editor.putInt(PASSENGER_CODE_SIZE_, passengerCodes.size());
        editor.commit();
    }
    public ArrayList<String> getPassengerCodes() {
        int size = mPref.getInt(PASSENGER_CODE_SIZE_, PRIVATE_MODE);
        ArrayList<String> passengerCodes = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            passengerCodes.add(mPref.getString(PASSENGER_CODE + i, UNKNOWN_PASSENGER_CODE));
        return passengerCodes;
    }

    public void setPassengerTypes(ArrayList<String> passengerTypes) {
        int size = mPref.getInt(PASSENGER_TYPE_SIZE_, PRIVATE_MODE);
        // clear the previous data if exists
        for (int i = 0; i < size; i++)
            editor.remove(PASSENGER_TYPE_ + i);
        // write the current list
        for (int i = 0; i < passengerTypes.size(); i++)
            editor.putString(PASSENGER_TYPE_ + i, passengerTypes.get(i));
        editor.putInt(PASSENGER_TYPE_SIZE_, passengerTypes.size());
        editor.commit();
    }
    public ArrayList<String> getPassengerTypes () {
        int size = mPref.getInt(PASSENGER_TYPE_SIZE_, PRIVATE_MODE);
        ArrayList<String> passengerCodes = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            passengerCodes.add(mPref.getString(PASSENGER_TYPE_ + i, UNKNOWN_PASSENGER_TYPE));
        return passengerCodes;
    }

    public void setDestinationStation(String mDestinationName) {
        editor.putString(DESTINATION_STATION, mDestinationName);
        editor.commit();
    }
    public String getDestinationStation () {
        return mPref.getString(DESTINATION_STATION, UNKNOWN_DESTINATION_STATION);
    }

    public void setDestinationStationCode(String destCode) {
        editor.putString(DESTINATION_STATION_CODE, destCode);
        editor.commit();
    }
    public String getDestinationStationCode () {
        return mPref.getString(DESTINATION_STATION_CODE, UNKNOWN_DESTINATION_STATION_CODE);
    }

    public void setTrainNames (ArrayList<String> trainNames) {
        int size = mPref.getInt(TRAIN_NAME_SIZE_, PRIVATE_MODE);
        // clear the previous data if exists
        for (int i = 0; i < size; i++)
            editor.remove(TRAIN_NAME_ + i);
        // write the current list
        for (int i = 0; i < trainNames.size(); i++)
            editor.putString(TRAIN_NAME_ + i, trainNames.get(i));
        editor.putInt(TRAIN_NAME_SIZE_, trainNames.size());
        editor.commit();
    }
    public ArrayList<String> getTrainNames () {
        int size = mPref.getInt(TRAIN_NAME_SIZE_, PRIVATE_MODE);
        ArrayList<String> passengerCodes = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            passengerCodes.add(mPref.getString(TRAIN_NAME_ + i, UNKNOWN_TRAIN_NAME));
        return passengerCodes;
    }

    public void setTrainCodes(ArrayList<String> trainCodes) {
        int size = mPref.getInt(TRAIN_CODE_SIZE_, PRIVATE_MODE);
        // clear the previous data if exists
        for (int i = 0; i < size; i++)
            editor.remove(TRAIN_CODE_ + i);
        // write the current list
        for (int i = 0; i < trainCodes.size(); i++)
            editor.putString(TRAIN_CODE_ + i, trainCodes.get(i));
        editor.putInt(TRAIN_CODE_SIZE_, trainCodes.size());
        editor.commit();
    }

    public ArrayList<String> getTrainCodes () {
        int size = mPref.getInt(TRAIN_CODE_SIZE_, PRIVATE_MODE);
        ArrayList<String> passengerCodes = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            passengerCodes.add(mPref.getString(TRAIN_CODE_ + i, UNKNOWN_TRAIN_CODE));
        return passengerCodes;
    }

    public void setClassTypes(ArrayList<String> classTypes) {
        int size = mPref.getInt(CLASS_TYPE_SIZE_, PRIVATE_MODE);
        // clear the previous data if exists
        for (int i = 0; i < size; i++)
            editor.remove(CLASS_TYPE_ + i);
        // write the current list
        for (int i = 0; i < classTypes.size(); i++)
            editor.putString(CLASS_TYPE_ + i, classTypes.get(i));
        editor.putInt(CLASS_TYPE_SIZE_, classTypes.size());
        editor.commit();
    }
    public ArrayList<String> getClassTypes () {
        int size = mPref.getInt(CLASS_TYPE_SIZE_, PRIVATE_MODE);
        ArrayList<String> passengerCodes = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            passengerCodes.add(mPref.getString(CLASS_TYPE_ + i, UNKNOWN_CLASS_TYPE));
        return passengerCodes;
    }

    public void setClassTypeCodes(ArrayList<String> classCode) {
        int size = mPref.getInt(CLASS_TYPE_CODE_SIZE_, PRIVATE_MODE);
        // clear the previous data if exists
        for (int i = 0; i < size; i++)
            editor.remove(CLASS_TYPE_CODE_ + i);
        // write the current list
        for (int i = 0; i < classCode.size(); i++)
            editor.putString(CLASS_TYPE_CODE_ + i, classCode.get(i));
        editor.putInt(CLASS_TYPE_CODE_SIZE_, classCode.size());
        editor.commit();
    }
    public ArrayList<String> getClassTypeCodes () {
        int size = mPref.getInt(CLASS_TYPE_CODE_SIZE_, PRIVATE_MODE);
        ArrayList<String> passengerCodes = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            passengerCodes.add(mPref.getString(CLASS_TYPE_CODE_ + i, UNKNOWN_CLASS_TYPE_CODE));
        return passengerCodes;
    }

    public void setNoOfPassenger(String noOfPassenger) {
        editor.putString(NUMBER_OF_PASSENGER, noOfPassenger);
        editor.commit();
    }
    public String getNumberOfPassenger () {
        return mPref.getString(NUMBER_OF_PASSENGER, UNKNOWN_NUMBER_OF_PASSENGER);
    }

    public void setAgeOfPassenger(String passengerAge) {
        editor.putString(AGE_OF_PASSENGER, passengerAge);
        editor.commit();
    }
    public String getAgeOfPassenger () {
        return mPref.getString(AGE_OF_PASSENGER, UNKNOWN_PASSENGER_AGE);
    }

    public void setPassengerCode(String passengerCode) {
        editor.putString(PASSENGER_CODE, passengerCode);
        editor.commit();
    }
    public String getPassengerCode () {
        return mPref.getString(PASSENGER_CODE, UNKNOWN_PASSENGER_CODE);
    }
    public static class MyDialogFragment extends DialogFragment {
        private ConnectionDetector cd;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.internet_connection_title_msg);
            builder.setMessage(R.string.connection_error_msg)
                    .setPositiveButton(R.string.retry_btn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            cd = new ConnectionDetector(AppController.getContext());
                            if (cd.isConnectingToInternet()) {
                                dismiss();
                            } else {
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                MyDialogFragment frag = new MyDialogFragment();
                                frag.show(ft, "txn_tag");
                            }
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    public void setDistrictArray(String districtArray) {
        editor.putString(REG_DISTRICT_ARRAY, districtArray);
        editor.commit();
    }

    public String getDistrictArray() { return mPref.getString(REG_DISTRICT_ARRAY, "unknown"); }

    public void setAppLanguage(String mLanguage) {
        editor.putString(APP_LANGUAGE, mLanguage);
        editor.commit();
    }
    public String getAppLanguage() {
        return mPref.getString(APP_LANGUAGE, "unknown");
    }

    public void setAppStatus(String mStatus) {
        editor.putString(APP_STATUS, mStatus);
        editor.commit();
    }
    public String getAppStatus() {
        return mPref.getString(APP_STATUS, "unknown");
    }

    public void setUserName(String mUserName) {
        editor.putString(USERNAME, mUserName);
        editor.commit();
    }
    public String getUserName() {
        return mPref.getString(USERNAME, "unknown");
    }

    public void setPhoneNumber(String mPhone) {
        editor.putString(PHONE_NUMBER, mPhone);
        editor.commit();
    }
    public String getPhoneNumber() {
        return mPref.getString(PHONE_NUMBER, "unknown");
    }

    public void setQrCodeImagePath(String mPath) {
        editor.putString(QR_CODE_BITMAP, mPath);
        editor.commit();
    }
    public String getQrCodeImagePath() {
        return mPref.getString(QR_CODE_BITMAP, "unknown");
    }

    public void setPhnNumberVerificationStatus(String mPhnNoStatus) {
        editor.putString(PHN_NUMBER_VERIFICATION_STATUS, mPhnNoStatus);
        editor.commit();
    }
    public String getPhnNumberVerificationStatus() {
        return mPref.getString(PHN_NUMBER_VERIFICATION_STATUS, "unknown");
    }

    public void setMerchantTypeVerificationStatus(String mMerchantType) {
        editor.putString(MERCHANT_TYPE_VERIFICATION_STATUS, mMerchantType);
        editor.commit();
    }
    public String getMerchantTypeVerificationStatus() {
        return mPref.getString(MERCHANT_TYPE_VERIFICATION_STATUS, "unknown");
    }

    public void setPhnNumber(String mPhnNo) {
        editor.putString(PHN_NUMBER, mPhnNo);
        editor.commit();
    }
    public String getPhnNumber() {
        return mPref.getString(PHN_NUMBER, "unknown");
    }

    public long getPhnUpdateCheck() {
        return mPref.getLong(PHN_UPDATE_CHECK, PRIVATE_MODE);
    }

    public void setPhnUpdateCheck(long phnTimes) {
        editor.putLong(PHN_UPDATE_CHECK, phnTimes);
        editor.commit();
    }

    public void setDayCount(int mCount) {
        editor.putInt(DAY_COUNT, mCount);
        editor.commit();
    }
    public int getDayCount() {
        return mPref.getInt(DAY_COUNT, PRIVATE_MODE);
    }

    public String getFirebaseId() {
        return mPref.getString(FIREBASE_ID, "unknown");
    }

    public void setFirebaseId(String firebaseId) {
        editor.putString(FIREBASE_ID, firebaseId);
        editor.commit();
    }

    public String getFirebaseTokenStatus() {
        return mPref.getString(FIREBASE_TOKEN_STATUS, "false");
    }

    public void setFirebaseTokenStatus(String firebaseId) {
        editor.putString(FIREBASE_TOKEN_STATUS, firebaseId);
        editor.commit();
    }

    public String getInitialChangePinStatus() {
        return mPref.getString(INITIAL_CHANGE_PIN_STATUS, "unknown");
    }

    public void setInitialChangePinStatus(String initialChangePinStatus) {
        editor.putString(INITIAL_CHANGE_PIN_STATUS, initialChangePinStatus);
        editor.commit();
    }

    public long getLocationUpdateCheck() {
        return mPref.getLong(LOCATION_UPDATE_CHECK, PRIVATE_MODE);
    }

    public void setLocationUpdateCheck(long times) {
        editor.putLong(LOCATION_UPDATE_CHECK, times);
        editor.commit();
    }

    public String getLongitude() {
        return mPref.getString(LONGITUDE, "unknown");
    }

    public void setLongitude(String longitude) {
        editor.putString(LONGITUDE, longitude);
        editor.commit();
    }

    public String getLatitude() {
        return mPref.getString(LATITUDE, "unknown");
    }

    public void setLatitude(String latitude) {
        editor.putString(LATITUDE, latitude);
        editor.commit();
    }

    public String getAgentPhnNum() {
        return mPref.getString(AGENT_PHN_NUM, "unknown");
    }

    public void setAgentPhnNum(String agentPhnNum) {
        editor.putString(AGENT_PHN_NUM, agentPhnNum);
        editor.commit();
    }

    public String getGatewayId() {
        return mPref.getString(GATEWAY_ID, "unknown");
    }

    public void setGatewayId(String gatewayId) {
        editor.putString(GATEWAY_ID, gatewayId);
        editor.commit();
    }

    public long getImgCacheCleanUpdateCheck() {
        return mPref.getLong(IMAGE_CACHE_CLEAN, PRIVATE_MODE);
    }

    public void setImgCacheCleanUpdateCheck(long times) {
        editor.putLong(IMAGE_CACHE_CLEAN, times);
        editor.commit();
    }

    public int getDisplayPictureCount() {
        return mPref.getInt(DISPLAY_PICTURE_COUNT, 0);
    }

    public void setDisplayPictureCount(int displayPictureCount) {
        editor.putInt(DISPLAY_PICTURE_COUNT, displayPictureCount);
        editor.commit();
    }

    public void setCenterDropDownPogistion(int position) {
        editor.putInt(CENTER_AREA_DROP_DOWN_POSITION, position);
        editor.commit();
    }

    public int getCenterDropDownPogistion() {
        return mPref.getInt(CENTER_AREA_DROP_DOWN_POSITION, 0);
    }

    public static String displayPictureArray[];

    public ArrayList<String> getDisplayPictureArrayList() {
        int size = mPref.getInt(DISPLAY_PICTURE_SIZE_, PRIVATE_MODE);
        ArrayList<String> displayPictureArrayList = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            displayPictureArrayList.add(mPref.getString(DISPLAY_PICTURE_ + i, UNKNOWN_DISPLAY_PICTURE));
        return displayPictureArrayList;
    }

    public void setDisplayPictureArrayList(ArrayList<String> displayPictureArrayList) {
        int size = mPref.getInt(DISPLAY_PICTURE_SIZE_, PRIVATE_MODE);
        // clear the previous data if exists
        for (int i = 0; i < size; i++)
            editor.remove(DISPLAY_PICTURE_ + i);
        // write the current list
        for (int i = 0; i < displayPictureArrayList.size(); i++)
            editor.putString(DISPLAY_PICTURE_ + i, displayPictureArrayList.get(i));
        editor.putInt(DISPLAY_PICTURE_SIZE_, displayPictureArrayList.size());
        editor.commit();
    }
}
