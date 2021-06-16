package com.cloudwell.paywell.services.activity.eticket.busticketNew.model

import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview.BordingPoint
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview.SeatstructureDetailsItem

class ResSeatInfo(var tototalAvailableSeat: Int = 0, var seatsPattenStr: String, var allBusSeat: ArrayList<SeatstructureDetailsItem>, var bordingPoints: java.util.ArrayList<BordingPoint>)
