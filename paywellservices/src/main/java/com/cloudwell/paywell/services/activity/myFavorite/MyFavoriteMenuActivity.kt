package com.cloudwell.paywell.services.activity.myFavorite

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.myFavorite.adapter.HeaderRecyclerViewSection
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu
import com.cloudwell.paywell.services.database.DatabaseClient
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


class MyFavoriteMenuActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_favorite_menu)

        initialisationView()


    }

    private fun generartedUnFavaroitRecycview(users: List<FavoriteMenu>) {


        val allFavoriteData = mutableMapOf<Int, List<FavoriteMenu>>()

        val allCategory = mutableSetOf<Int>()

        users.forEach {
            val category = it.category
            allCategory.add(category)
        }


        allCategory.forEach {
            val category = it
            val data = mutableListOf<FavoriteMenu>()

            users.forEach {
                if (category.equals(it.category)) {
                    data.add(it)
                }
            }

            allFavoriteData.put(category, data)
        }


        val sectionAdapter = SectionedRecyclerViewAdapter()


        allCategory.forEach {

            val section = getString(it)
            val sectionData = HeaderRecyclerViewSection(section, allFavoriteData.get(it))
            sectionAdapter.addSection(sectionData)

        }

        val display = this.getWindowManager().getDefaultDisplay()
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = resources.displayMetrics.density
        val dpWidth = outMetrics.widthPixels / density
        var columns = 4;
        if (dpWidth > 320) {
            columns = 4;
        } else {
            columns = 3;
        }


        val glm = GridLayoutManager(applicationContext, columns)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (sectionAdapter.getSectionItemViewType(position)) {
                    SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER ->
                        return columns
                    else ->
                        return 1
                }
            }
        }

        val sectionHeader = findViewById<RecyclerView>(R.id.add_header) as RecyclerView
        sectionHeader.setLayoutManager(glm)
        sectionHeader.setHasFixedSize(true)
        sectionHeader.setAdapter(sectionAdapter)


    }

    private fun initialisationView() {
        val allUnFavoriteMenu = DatabaseClient.getInstance(applicationContext).appDatabase.mFavoriteMenuDab().allUnFavoriteMenu
        allUnFavoriteMenu.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Consumer<List<FavoriteMenu>> {
            @Throws(Exception::class)
            override fun accept(users: List<FavoriteMenu>) {

                generartedUnFavaroitRecycview(users)

            }


        })


    }


}
