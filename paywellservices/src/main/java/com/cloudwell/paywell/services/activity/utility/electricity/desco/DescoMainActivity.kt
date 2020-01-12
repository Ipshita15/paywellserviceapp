package com.cloudwell.paywell.services.activity.utility.electricity.desco

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.utility.electricity.desco.postpaid.DESCOPostpaidMainActivity
import com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.DescoPrepaidMainActivity
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import kotlinx.android.synthetic.main.activity_desco_main2.*

class DescoMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desco_main2)

        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(R.string.home_utility_desco)
        }
        val mAppHandler = AppHandler.getmInstance(applicationContext)

        if (mAppHandler.getAppLanguage().equals("en", ignoreCase = true)) {
            descoPostpaidBtn.setTypeface(AppController.getInstance().oxygenLightFont)
            descoPrepaidBtn.setTypeface(AppController.getInstance().oxygenLightFont)

        } else {
            descoPostpaidBtn.setTypeface(AppController.getInstance().aponaLohitFont)
            descoPrepaidBtn.setTypeface(AppController.getInstance().aponaLohitFont)

        }
    }

    fun onButtonClicker(view: View) {


        when (view.getId()) {
            R.id.descoPostpaidBtn -> {
                startActivity(Intent(this, DESCOPostpaidMainActivity::class.java))
            }
            R.id.descoPrepaidBtn -> {
                startActivity(Intent(this, DescoPrepaidMainActivity::class.java))
            }
            else -> {
            }
        }

    }
}
