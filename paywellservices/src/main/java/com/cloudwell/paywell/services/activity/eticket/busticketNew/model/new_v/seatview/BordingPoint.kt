package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 5/12/20.
 */
class BordingPoint {
    @SerializedName("reporting_branch_id")
    @Expose
    var reportingBranchId: Int? = null

    @SerializedName("counter_name")
    @Expose
    var counterName: String? = null

    @SerializedName("schedule_time")
    @Expose
    var scheduleTime: String? = null

}