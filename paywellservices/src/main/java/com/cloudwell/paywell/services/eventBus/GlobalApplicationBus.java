package com.cloudwell.paywell.services.eventBus;

import com.squareup.otto.Bus;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/2/19.
 */
public class GlobalApplicationBus {
    private static Bus sBus;

    public static Bus getBus() {
        if (sBus == null)
            sBus = new Bus();
        return sBus;
    }
}

