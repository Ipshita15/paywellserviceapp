package com.cloudwell.paywell.services.libaray

import android.content.res.Resources
import android.view.ViewGroup

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/3/19.
 */
fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun ViewGroup.children() = 0.until(childCount).map { getChildAt(it) }