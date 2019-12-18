package com.cloudwell.paywell.services.activity.reg.nidOCR.view;

import com.cloudwell.paywell.consumer.ui.nidRegistion.model.User;
import com.cloudwell.paywell.services.activity.base.IView;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-09-03.
 */
public interface IInputNidListener extends IView {
    void openNextActivity(@NotNull User user);
    void setDefaultNIDImagInFirstNIDView();
    void setDefaultNIDImagInSecondNIDView();
}
