package com.cloudwell.paywell.services;

import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2020-04-15.
 */
public class TestExam {

//    LinkedHashSet<Object> mtSet = new MyLinkedHashSet<>(5);



    MaxSizeHashMap<String, Object>  mtSet = new  MaxSizeHashMap<String, Object>(5);






    @Test
    public void tes(){

        add("topup","topup");
        add("iva", "iva");
        add("bus", "bus");
        add("airtickt", "airtickt");
        add("iva", "iva");
        add("bus","bus");
        add("bus", "bus");
        add("bus", "bus");
        add("bus", "bus");
        add("bus", "busnew last");
        add("topup","topup");

//        Iterator<String> iterator = mtSet.iterator();
//
//        while (iterator.hasNext()){
//            String next = iterator.next();
//            System.out.println(next);
//        }




        for (String key: mtSet.keySet()) {
            System.out.println("key : " + key);
            System.out.println("value : " + mtSet.get(key));
        }


    }

    private void add(String key , Object item) {
        mtSet.put(key, item);

    }


    public class MyLinkedHashSet<T> extends LinkedHashSet<T> {
        private long maxSize;

        public MyLinkedHashSet(long maxSize) {
            this.maxSize = maxSize;
        }

        @Override
        public boolean add(T item) {
            if (size() == maxSize) {
                removeFirst();
            }
            return super.add(item);
        }

        private void removeFirst() {
            if (size() > 0) {
                Iterator<T> iterator = iterator();
                T item = iterator.next();
                remove(item);
            }
        }

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


    @Test
    public void test(){


        // create a 2d array
        int[][] a = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
        };

        // first for...each loop access the individual array
        // inside the 2d array
//        for (int[] innerArray: a) {
//            // second for...each loop access each element inside the row
//            for(int data: innerArray) {
//                print(data);
//            }
//            System.out.println("");
//
//        }

        int row;
        int coloum;
        for(row = 0; row < 3; row++)
        {

            for(coloum = 0; coloum < 3; coloum++)
            {

                int indexI =row +1;
                int indexJ =coloum +1;

                System.out.print(indexI+""+indexJ+" ");

            }
            System.out.println(" ");
        }



    }


}
