package com.cloudwell.paywell.services.activity.eticket.busticket.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.busticket.model.SeatView;
import com.cloudwell.paywell.services.app.AppHandler;

import java.util.ArrayList;
import java.util.List;

public class SeatListAdapter extends BaseAdapter {
    private Context _context;
    private LayoutInflater inflater;
    private ArrayList<SeatView> _seatList;
    private TextView _totalSeatSelect;
    private TextView _totalPrice;
    private AppHandler _appHandler;
    private List<String> totalAmount;
    private static List<SeatView> selectSeatList;

    public SeatListAdapter(Context context, ArrayList<SeatView> seatList, TextView totalSeatSelect, TextView totalPrice, AppHandler appHandler) {
        _context = context;
        this.inflater = LayoutInflater.from(_context);
        this._seatList = seatList;
        this._totalSeatSelect = totalSeatSelect;
        this._totalPrice = totalPrice;
        this._appHandler = appHandler;
        totalAmount = new ArrayList<>();
        selectSeatList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return _seatList.size();
    }

    @Override
    public Object getItem(int position) {
        return _seatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final View view = inflater.inflate(R.layout.seat_list_items, parent, false);
        final CheckBox _checkSeat = view.findViewById(R.id.checkSeat);
        _checkSeat.setText(_seatList.get(position).getSeatName());

        if (!_seatList.get(position).getStatus().equalsIgnoreCase("Available")) {
            _checkSeat.setBackgroundColor(Color.parseColor("#FF0000"));
            _checkSeat.setEnabled(false);
        } else {
            _checkSeat.setChecked(_seatList.get(position).isSelected());
            if (_seatList.get(position).isSelected()) {
                _checkSeat.setBackgroundColor(Color.parseColor("#5aac40"));
            } else {
                _checkSeat.setBackgroundColor(Color.parseColor("#A9A9A9"));
            }
            _checkSeat.setTag(position);
            _checkSeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    Double netAmount = 0.0;
                    String fare = "";
                    String seatNo = "";
                    String seatId = "";
                    String selectedSeatAndPrice = "";
                    _seatList.get(position).setSelected(isChecked);
                    _checkSeat.setTag(position);
                    if (_seatList.get(position).isSelected()) {
                        _checkSeat.setBackgroundColor(Color.parseColor("#5aac40"));
                        selectSeatList.add(_seatList.get(position));
                        totalAmount.add(_seatList.get(position).getFare());
                        for (SeatView seat : selectSeatList) {
                            selectedSeatAndPrice += seat.getSeatName() + "\t\t\t\t" + seat.getFare() + " Tk " + "\n";
                            seatId += seat.getSeatid() + ",";
                            seatNo += seat.getSeat_no() + ",";
                            fare += seat.getFare() + ",";
                        }

                        for (String amount : totalAmount) {
                            netAmount += Double.valueOf(amount);
                        }
                        _totalSeatSelect.setText(String.valueOf(selectSeatList.size()));
                        _totalPrice.setText(String.format("%s%s", String.valueOf(netAmount), _context.getString(R.string.tk)));

                        _appHandler.setSelectedSeatAndPrice(selectedSeatAndPrice);
                        _appHandler.setNumberOfSeat(String.valueOf(selectSeatList.size()));
                        _appHandler.setSeatId(seatId);
                        _appHandler.setSeatNo(seatNo);
                        _appHandler.setFare(fare);
                    } else {
                        _checkSeat.setBackgroundColor(Color.parseColor("#A9A9A9"));
                        selectSeatList.remove(_seatList.get(position));
                        totalAmount.remove(_seatList.get(position).getFare());
                        for (SeatView seat : selectSeatList) {
                            selectedSeatAndPrice += seat.getSeatName() + "\t\t\t\t" + seat.getFare() + _context.getString(R.string.tk) + "\n";
                            seatId += seat.getSeatid() + ",";
                            seatNo += seat.getSeat_no() + ",";
                            fare += seat.getFare() + ",";
                        }

                        for (String amount : totalAmount) {
                            netAmount += Double.valueOf(amount);
                        }
                        _totalSeatSelect.setText(String.valueOf(selectSeatList.size()));
                        _totalPrice.setText(String.format("%s%s", String.valueOf(netAmount), _context.getString(R.string.tk)));

                        _appHandler.setSelectedSeatAndPrice(selectedSeatAndPrice);
                        _appHandler.setNumberOfSeat(String.valueOf(selectSeatList.size()));
                        _appHandler.setSeatId(seatId);
                        _appHandler.setSeatNo(seatNo);
                        _appHandler.setFare(fare);
                    }
                }
            });

        }
        return view;
    }
}
