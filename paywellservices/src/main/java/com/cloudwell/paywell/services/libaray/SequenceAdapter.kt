package com.cloudwell.paywell.services.libaray


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/3/19.
 */
public abstract class SequenceAdapter<T> where T : Any {

    abstract fun getCount(): Int

    abstract fun getItem(position: Int): T

    abstract fun bindView(sequenceStep: SequenceStep, item: T)
}