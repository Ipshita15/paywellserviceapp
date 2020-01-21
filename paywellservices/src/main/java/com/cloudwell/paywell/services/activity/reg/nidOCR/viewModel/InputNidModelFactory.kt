package com.cloudwell.paywell.services.activity.reg.nidOCR.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-09-04.
 */
class InputNidModelFactory() : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return  NidInputViewModel() as T
    }

}