package com.cloudwell.paywell.services.activity;

import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2020-04-15.
 */
public class RecentUsedStackSet {


    public static RecentUsedStackSet instance = null;
    MaxSizeHashMap<String, RecentUsedMenu>  mtSet = new  MaxSizeHashMap<String, RecentUsedMenu>(8);


    private RecentUsedStackSet(){

    }

    public static RecentUsedStackSet getInstance(){
        if(instance == null){
            instance = new RecentUsedStackSet();
        }
        return instance;
    }

    public void add( RecentUsedMenu item) {
        mtSet.put(item.getName(), item);

    }

    public void addAll(List<RecentUsedMenu> item) {

        for (RecentUsedMenu i:item) {
            mtSet.put(i.getName(), i);
        }

    }


    public ArrayList<RecentUsedMenu> getAll() {

        Collection<RecentUsedMenu> values = mtSet.values();

        ArrayList<RecentUsedMenu> mobileOperatorArrayList = new ArrayList<>(values);

        Collections.reverse(mobileOperatorArrayList);
        return mobileOperatorArrayList;

    }


    public class MaxSizeHashMap<K, V> extends LinkedHashMap<K, V> {
        private final int maxSize;

        public MaxSizeHashMap(int maxSize) {
            this.maxSize = maxSize;
        }


        @Nullable
        @Override
        public V put(K key, V value) {
            boolean containsKey = containsKey(key);
            if (containsKey){
                remove(key);
                return super.put(key, value);
            }else {
                return super.put(key, value);
            }


        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxSize;
        }
    }


}
