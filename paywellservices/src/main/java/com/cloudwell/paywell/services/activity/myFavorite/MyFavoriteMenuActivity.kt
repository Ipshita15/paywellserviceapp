package com.cloudwell.paywell.services.activity.myFavorite

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.DisplayMetrics
import android.view.MenuItem
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.myFavorite.adapter.FavoirteAdapter
import com.cloudwell.paywell.services.activity.myFavorite.adapter.HeaderRecyclerViewSection
import com.cloudwell.paywell.services.activity.myFavorite.adapter.helper.OnStartDragListener
import com.cloudwell.paywell.services.activity.myFavorite.adapter.helper.SimpleItemTouchHelperCallback
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu
import com.cloudwell.paywell.services.activity.myFavorite.model.MessageEvent
import com.cloudwell.paywell.services.activity.myFavorite.model.MessageEventFavDeleted
import com.cloudwell.paywell.services.activity.myFavorite.model.MessageEventPositionMove
import com.cloudwell.paywell.services.constant.AllConstant
import com.cloudwell.paywell.services.database.DatabaseClient
import com.cloudwell.paywell.services.preference.FavoritePreference
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_favorite_menu.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MyFavoriteMenuActivity : AppCompatActivity(), OnStartDragListener {
    lateinit var sectionAdapter: SectionedRecyclerViewAdapter;
    var allFavoriteData = kotlin.collections.mutableMapOf<String, List<FavoriteMenu>>()

    var previewPogistion = 0
    private var mItemTouchHelper: ItemTouchHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_favorite_menu)

        assert(getSupportActionBar() != null)
        if (getSupportActionBar() != null) {
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()!!.setTitle(R.string.title_activity_my_favorite_menu)


        }


        initialisationView()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed();
        finish()
    }


    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        mItemTouchHelper?.startDrag(viewHolder)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onFavoriteItemAdd(event: MessageEvent) {

        var counter = FavoritePreference.with(applicationContext).getInt(AllConstant.COUNTER_FAVORITE, 0);
        if (counter == 0) {
            counter = 1;
        } else {
            counter = counter + 1;
        }
        if (counter > 8) {
            showMessage()
            return
        } else {
            FavoritePreference.with(applicationContext).addInt(AllConstant.COUNTER_FAVORITE, counter).save()

            previewPogistion = event.index;
            event.title;

            val favoriteMenu = event.favoriteMenu;
            favoriteMenu.status = MenuStatus.Favourite.text;
            favoriteMenu.favoriteListPosition = counter;
            DatabaseClient.getInstance(applicationContext).appDatabase.mFavoriteMenuDab().update(favoriteMenu)


            this.runOnUiThread {
                Toast.makeText(applicationContext, getString(R.string.Added) + getString(event.favoriteMenu.name), Toast.LENGTH_LONG).show()
                getAllUnFavoriteItem()
                getAllFavoriteDate()
            }
        }


    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onFavoriteItemdeleted(event: MessageEventFavDeleted) {

        var counter = FavoritePreference.with(applicationContext).getInt(AllConstant.COUNTER_FAVORITE, 0)
        if (counter == 0) {
            counter = 0
        } else {
            counter -= 1
        }
        FavoritePreference.with(applicationContext).addInt(AllConstant.COUNTER_FAVORITE, counter).save()

        val favoriteMenu = event.favoriteMenu;
        favoriteMenu.status = MenuStatus.UnFavorite.text;
        favoriteMenu.favoriteListPosition = counter
        DatabaseClient.getInstance(applicationContext).appDatabase.mFavoriteMenuDab().update(favoriteMenu)


        this.runOnUiThread {
            getAllUnFavoriteItem()
            getAllFavoriteDate()
        }

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onFavoriteItemPositionMove(event: MessageEventPositionMove) {


        DatabaseClient.getInstance(applicationContext).appDatabase.mFavoriteMenuDab().update(event.fromMenu)
        DatabaseClient.getInstance(applicationContext).appDatabase.mFavoriteMenuDab().update(event.toMenu)

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
            it.category
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
        val columns: Int;
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
        sectionHeader.isNestedScrollingEnabled = false;

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

                val sortedWith = users.sortedWith(object : Comparator<FavoriteMenu> {
                    override fun compare(p1: FavoriteMenu, p2: FavoriteMenu): Int = when {
                        p1.favoriteListPosition > p2.favoriteListPosition -> 0
                        p1.favoriteListPosition == p2.favoriteListPosition -> 1
                        else -> -1
                    }
                })

                generatedFavaroitRecycView(sortedWith)

            }


        });


    }


    private fun generatedFavaroitRecycView(result: List<FavoriteMenu>) {

        val display = this.getWindowManager().getDefaultDisplay()
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = resources.displayMetrics.density
        val dpWidth = outMetrics.widthPixels / density
        var columns: Int;
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
        recyclerView.isNestedScrollingEnabled = false;

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


    private fun showMessage() {

        val snackbar = Snackbar.make(coordinatorLayout_fav, R.string.can_not_add_more_than, Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(Color.parseColor("#ffffff"))
        val snackBarView = snackbar.view
        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
        snackbar.show()
    }


}
