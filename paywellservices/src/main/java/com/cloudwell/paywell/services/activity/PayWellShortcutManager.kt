package com.cloudwell.paywell.services.activity

import android.os.Build
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu
import com.cloudwell.paywell.services.constant.AllConstant
import com.cloudwell.paywell.services.database.DatabaseClient
import com.cloudwell.paywell.services.utils.AppUtility
import com.cloudwell.paywell.services.utils.ResorceHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Yasin Hosain on 9/11/2019.
 * yasinenubd5@gmail.com
 */
class PayWellShortcutManager {

    companion object{

        private val COMPLETED_SHORTCUT_ID = "completed_shortcut_id"
        private val SHORTCUT_IDS = Arrays.asList(COMPLETED_SHORTCUT_ID)
        @TargetApi(Build.VERSION_CODES.N_MR1)



        fun  enableShortcutList(@NonNull context: Context) {
            doAsync {

                val favoriteMenuDab = DatabaseClient.getInstance(context).appDatabase.mFavoriteMenuDab()
                val favouriteMenus = favoriteMenuDab.getAppShortcut() as ArrayList<FavoriteMenu>
                Collections.sort(favouriteMenus, Comparator<FavoriteMenu> { o1, o2 ->
                    if (o1.favoriteListPosition > o2.favoriteListPosition) {
                        0
                    } else if (o1.favoriteListPosition > o2.favoriteListPosition) {
                        1
                    } else {
                        -1
                    }
                })
                val dislikeMenus: ArrayList<FavoriteMenu> = favoriteMenuDab.dislikedMenu as ArrayList<FavoriteMenu>
                val shortcuts = ArrayList<ShortcutInfo>()
                shortcuts.clear()
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                    val payWellShortcutManager: ShortcutManager = context.getSystemService(ShortcutManager::class.java)

                    for (x in 0 until favouriteMenus.size) {
                        if (x > 4) break
                        val completedTasksIntent = AppUtility.onAppShortcutItemClick(favouriteMenus.get(x), context)
                        completedTasksIntent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE, true)
                        completedTasksIntent.setAction(Intent.ACTION_VIEW)
                        completedTasksIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        val postShortcut = ShortcutInfo.Builder(context, favouriteMenus.get(x).name)
                                .setShortLabel(context.getString(ResorceHelper.getResId(favouriteMenus.get(x).name, R.string::class.java)))
                                .setLongLabel(context.getString(ResorceHelper.getResId(favouriteMenus.get(x).name, R.string::class.java)))
                                .setIcon(Icon.createWithResource(context, ResorceHelper.getResId(favouriteMenus.get(x).icon, R.drawable::class.java)))
                                .setIntents(arrayOf(Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK), completedTasksIntent))
                                .build()

                        shortcuts.add(postShortcut)
                    }
                    payWellShortcutManager.setDynamicShortcuts(shortcuts)
                    for (x in 0 until dislikeMenus.size) {
                        val shortcutManager: ShortcutManager = context.getSystemService(ShortcutManager::class.java)
                        shortcutManager.disableShortcuts(Arrays.asList(dislikeMenus.get(x).name))
                    }
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.N_MR1)
        fun enableSingleShortcut(context: Context, favouriteMenu: FavoriteMenu){

            val payWellShortcutManager: ShortcutManager = context.getSystemService(ShortcutManager::class.java)
            val completedTasksIntent = AppUtility.onAppShortcutItemClick(favouriteMenu, context)
            completedTasksIntent.setAction(Intent.ACTION_VIEW)
            completedTasksIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val postShortcut = ShortcutInfo.Builder(context, favouriteMenu.name)
                    .setShortLabel(context.getString(ResorceHelper.getResId(favouriteMenu.name, R.string::class.java)))
                    .setLongLabel(context.getString(ResorceHelper.getResId(favouriteMenu.name, R.string::class.java)))
                    .setIcon(Icon.createWithResource(context, ResorceHelper.getResId(favouriteMenu.icon, R.drawable::class.java)))
                    .setIntents(arrayOf(Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK), completedTasksIntent))
                    .build()

            payWellShortcutManager.addDynamicShortcuts(Arrays.asList(postShortcut))

        }


        @TargetApi(Build.VERSION_CODES.N_MR1)
        fun disableCompletedShortcut(@NonNull context: Context,shortcutId:String) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return
            val shortcutManager:ShortcutManager = context.getSystemService(ShortcutManager::class.java)
            shortcutManager.disableShortcuts(Arrays.asList(shortcutId))
        }
    }






}