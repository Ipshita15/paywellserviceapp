package com.cloudwell.paywell.services.activity.myFavorite

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.DisplayMetrics
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.myFavorite.adapter.FavoirteAdapter
import com.cloudwell.paywell.services.activity.myFavorite.adapter.HeaderRecyclerViewSection
import com.cloudwell.paywell.services.activity.myFavorite.adapter.helper.OnStartDragListener
import com.cloudwell.paywell.services.activity.myFavorite.adapter.helper.SimpleItemTouchHelperCallback
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu
import com.cloudwell.paywell.services.activity.myFavorite.model.MessageEvent
import com.cloudwell.paywell.services.activity.myFavorite.model.MessageEventFavDeleted
import com.cloudwell.paywell.services.database.DatabaseClient
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MyFavoriteMenuActivity : Activity(), OnStartDragListener {
    lateinit var sectionAdapter: SectionedRecyclerViewAdapter;
    var allFavoriteData = kotlin.collections.mutableMapOf<String, List<FavoriteMenu>>()

    var previewPogistion = 0
    private var mItemTouchHelper: ItemTouchHelper? = null

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        mItemTouchHelper?.startDrag(viewHolder)

    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_favorite_menu)

        initialisationView()


    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onMessageEvent(event: MessageEvent) {

        previewPogistion = event.index;

        val title = event.title;

        val favoriteMenu = event.favoriteMenu;
        favoriteMenu.status = MenuStatus.Favourite.text;
        val update = DatabaseClient.getInstance(applicationContext).appDatabase.mFavoriteMenuDab().update(favoriteMenu)


        this.runOnUiThread {
            getAllUnFavoriteItem()
            getAllFavoriteDate()
        }


    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onFavoriteItemdeleted(event: MessageEventFavDeleted) {


        val favoriteMenu = event.favoriteMenu;
        favoriteMenu.status = MenuStatus.UnFavorite.text;
        val update = DatabaseClient.getInstance(applicationContext).appDatabase.mFavoriteMenuDab().update(favoriteMenu)


        this.runOnUiThread {
            getAllUnFavoriteItem()
            getAllFavoriteDate()
        }


    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    private fun generartedUnFavaroitRecycview(DBDatas: List<FavoriteMenu>) {

        allFavoriteData = mutableMapOf<String, List<FavoriteMenu>>()
        val allCategory = mutableSetOf<String>()

        DBDatas.forEach {
            val category = it.category
            val string = getString(it.category);
            allCategory.add(string)
        }


        allCategory.forEach {
            val category = it
            val data = mutableListOf<FavoriteMenu>()

            DBDatas.forEach {
                val string = getString(it.category)
                if (category.equals(string)) {
                    data.add(it)
                }
            }

            allFavoriteData.put(category, data)
        }


        sectionAdapter = SectionedRecyclerViewAdapter()


        // generator HeaderRecyclerViewSection

        for ((index, value) in allCategory.withIndex()) {

            val sectionData = HeaderRecyclerViewSection(applicationContext, index, value, allFavoriteData.get(value))
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
//        sectionHeader.isNestedScrollingEnabled = true;

        if (previewPogistion != 0) {
            sectionAdapter.notifyDataSetChanged();
            sectionAdapter.notifyItemRemoved(previewPogistion)
        }


    }

    private fun initialisationView() {
        getAllFavoriteDate();

        getAllUnFavoriteItem()


    }

    private fun getAllFavoriteDate() {


        val allUnFavoriteMenu = DatabaseClient.getInstance(applicationContext).appDatabase.mFavoriteMenuDab().allFavoriteMenu
        allUnFavoriteMenu.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Consumer<List<FavoriteMenu>> {
            @Throws(Exception::class)
            override fun accept(users: List<FavoriteMenu>) {

                generatedFavaroitRecycView(users)

            }


        });

        doAsync {
            val result = DatabaseClient.getInstance(applicationContext).appDatabase.mFavoriteMenuDab().getAllFavoriteMenu()

            val blockingGet = result.blockingGet();
            uiThread {

                generatedFavaroitRecycView(blockingGet)
            }
        }


    }


    private fun generatedFavaroitRecycView(result: List<FavoriteMenu>) {

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


        val recyclerView = findViewById<RecyclerView>(R.id.add_favoirte) as RecyclerView
        recyclerView.setHasFixedSize(true)

        val glm = GridLayoutManager(applicationContext, columns)
        recyclerView.layoutManager = glm


        val recyclerListAdapter = FavoirteAdapter(applicationContext, result, this)
        recyclerView.layoutManager = glm
        recyclerView.adapter = recyclerListAdapter;
        recyclerView.isNestedScrollingEnabled = true;

        val callback = SimpleItemTouchHelperCallback(recyclerListAdapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper?.attachToRecyclerView(recyclerView)


    }

    private fun getAllUnFavoriteItem() {

        val allUnFavoriteMenu = DatabaseClient.getInstance(applicationContext).appDatabase.mFavoriteMenuDab().allUnFavoriteMenu
        allUnFavoriteMenu.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Consumer<List<FavoriteMenu>> {
            @Throws(Exception::class)
            override fun accept(users: List<FavoriteMenu>) {

                generartedUnFavaroitRecycview(users)

            }


        });
    }


}
