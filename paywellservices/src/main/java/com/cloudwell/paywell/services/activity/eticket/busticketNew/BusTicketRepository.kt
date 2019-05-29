package com.cloudwell.paywell.services.activity.eticket.busticketNew

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.Airport
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Bus
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResGetBusListData
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.database.DatabaseClient
import com.cloudwell.paywell.services.retrofit.ApiUtils
import okhttp3.ResponseBody
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
class BusTicketRepository(private val mContext: Context) {

    private var mAppHandler: AppHandler? = null


    fun getBusListData(): MutableLiveData<ResGetBusListData> {
        mAppHandler = AppHandler.getmInstance(mContext)
//        val userName = mAppHandler!!.imeiNo
        val userName = "cwntcl"
        val skey = "fLdjl3VX_OPOx6zvadOGuCvq2Ay0civ6v-HUQeiLVRg"

        val data = MutableLiveData<ResGetBusListData>()

        ApiUtils.getAPIServicePHP7().getBusListData(userName, skey).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
//                    data.value = response.body()
                    val body = response.body()?.string()
                    com.orhanobut.logger.Logger.v("" + body)

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                com.orhanobut.logger.Logger.e("" + t.message)
                //data.value = ResGetBusListData(t)
            }
        })
        return data
    }


    fun getAirportBy(iac: String?): MutableLiveData<Airport> {
        val data = MutableLiveData<Airport>()
        doAsync {

            val airportBy = DatabaseClient.getInstance(mContext).appDatabase.mAirtricketDab().getAirportBy(iac)

            uiThread {
                data.value = airportBy
            }
        }

        return data

    }

    fun saveBuss(Buss: List<Bus>): MutableLiveData<LongArray> {


        val data = MutableLiveData<LongArray>()
        doAsync {

            val inseredIds = DatabaseClient.getInstance(mContext).appDatabase.mBusTicketDab().insert(Buss)

            uiThread {
                data.value = inseredIds
            }
        }

        return data

    }


}