package com.cloudwell.paywell.services.activity.eticket.busticket.model;

public class SeatView {
    private String seatName;
    private String x_axis;
    private String y_axis;
    private String seat_type_id;
    private String status;
    private String seat_structure_id;
    private String fare;
    private String seat_no;
    private String seatid;

    private boolean selected;

    // ------------------------------------------------------------

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public String getX_axis() {
        return x_axis;
    }

    public void setX_axis(String x_axis) {
        this.x_axis = x_axis;
    }

    public String getY_axis() {
        return y_axis;
    }

    public void setY_axis(String y_axis) {
        this.y_axis = y_axis;
    }

    public String getSeat_type_id() {
        return seat_type_id;
    }

    public void setSeat_type_id(String seat_type_id) {
        this.seat_type_id = seat_type_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeat_structure_id() {
        return seat_structure_id;
    }

    public void setSeat_structure_id(String seat_structure_id) {
        this.seat_structure_id = seat_structure_id;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(String seat_no) {
        this.seat_no = seat_no;
    }

    public String getSeatid() {
        return seatid;
    }

    public void setSeatid(String seatid) {
        this.seatid = seatid;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean isChecked) {
        this.selected = isChecked;
    }

}
