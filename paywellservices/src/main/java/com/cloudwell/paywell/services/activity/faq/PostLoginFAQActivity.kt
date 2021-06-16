package com.cloudwell.paywell.services.activity.faq

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnGroupExpandListener
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.faq.adapter.ExpandableListAdapter
import com.cloudwell.paywell.services.activity.faq.adapter.ThreeLevelListAdapter


class PostLoginFAQActivity : BaseActivity() {

    var listAdapter: ExpandableListAdapter? = null
    var listDataHeader: List<String>? = null
    var listDataChild: HashMap<String, List<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_f_a_q_post)
        setToolbar(getString(R.string.faq))

        val isAfterLogin = intent.getBooleanExtra("isAfterLogin", false)

        prepareListData(isAfterLogin)

        listAdapter = ExpandableListAdapter(this, listDataHeader, listDataChild)

    }

    private fun prepareListData(isAfterLogin: Boolean) {
        listDataHeader = ArrayList()
        listDataChild = HashMap()
        listDataHeader = getResources().getStringArray(R.array.faq_header).toList()


        val listChild = getResources().getStringArray(R.array.faq_child).toList()

        (listDataHeader as List<String>).forEachIndexed { index, element ->
            val key = listDataHeader!!.get(index);
            val subData = mutableListOf<String>(listChild.get(index))
            listDataChild!!.put(key, subData.toList())

        }
        Log.e("", "")

        val data: MutableList<LinkedHashMap<String, ArrayList<String>>> = ArrayList()


        val parent = getResources().getStringArray(R.array.parent)
        val q1 = getResources().getStringArray(R.array.q1)
        val q2 = getResources().getStringArray(R.array.q2)
        val q3 = getResources().getStringArray(R.array.q3)
        val q4 = getResources().getStringArray(R.array.q4)
        val q5 = getResources().getStringArray(R.array.q5)
        val q6 = getResources().getStringArray(R.array.q6)
        val q7 = getResources().getStringArray(R.array.q7)

        val a1 = getResources().getStringArray(R.array.a1)
        val a2 = getResources().getStringArray(R.array.a2)
        val a3 = getResources().getStringArray(R.array.a3)
        val a4 = getResources().getStringArray(R.array.a4)
        val a5 = getResources().getStringArray(R.array.a5)
        val a6 = getResources().getStringArray(R.array.a6)
        val a7 = getResources().getStringArray(R.array.a7)

        val thirdLevelq1: LinkedHashMap<String, ArrayList<String>> = LinkedHashMap()
        val thirdLevelq2: LinkedHashMap<String, ArrayList<String>> = LinkedHashMap()
        val thirdLevelq3: LinkedHashMap<String, ArrayList<String>> = LinkedHashMap()
        val thirdLevelq4: LinkedHashMap<String, ArrayList<String>> = LinkedHashMap()
        val thirdLevelq5: LinkedHashMap<String, ArrayList<String>> = LinkedHashMap()
        val thirdLevelq6: LinkedHashMap<String, ArrayList<String>> = LinkedHashMap()
        val thirdLevelq7: LinkedHashMap<String, ArrayList<String>> = LinkedHashMap()

        val secondLevel: MutableList<Array<String>> = ArrayList()


        secondLevel.add(q1)
        secondLevel.add(q2)
        secondLevel.add(q3)
        secondLevel.add(q4)
        secondLevel.add(q5)
        secondLevel.add(q6)
        secondLevel.add(q7)

        a1.forEachIndexed { index, s ->
            thirdLevelq1[q1[index]] = ArrayList(mutableListOf(a1[index]))
        }

        a2.forEachIndexed { index, s ->
            thirdLevelq2[q2[index]] = ArrayList(mutableListOf(a2[index]))
        }

        a3.forEachIndexed { index, s ->
            thirdLevelq3[q3[index]] = ArrayList(mutableListOf(a3[index]))
        }
        a4.forEachIndexed { index, s ->
            thirdLevelq4[q4[index]] = ArrayList(mutableListOf(a4[index]))
        }
        a5.forEachIndexed { index, s ->
            thirdLevelq5[q5[index]] = ArrayList(mutableListOf(a5[index]))
        }

        a6.forEachIndexed { index, s ->
            thirdLevelq6[q6[index]] = ArrayList(mutableListOf(a6[index]))
        }
        a7.forEachIndexed { index, s ->
            thirdLevelq7[q7[index]] = ArrayList(mutableListOf(a7[index]))
        }


        data.add(thirdLevelq1)
        data.add(thirdLevelq2)
        data.add(thirdLevelq3)
        data.add(thirdLevelq4)
        data.add(thirdLevelq5)
        data.add(thirdLevelq6)
        data.add(thirdLevelq7)


        val expandableListView = findViewById<View>(R.id.expandible_listview) as ExpandableListView
        //passing three level of information to constructor
        //passing three level of information to constructor
        val threeLevelListAdapterAdapter = ThreeLevelListAdapter(this, parent, secondLevel, data)
        expandableListView.setAdapter(threeLevelListAdapterAdapter)
        expandableListView.setOnGroupExpandListener(object : OnGroupExpandListener {
            var previousGroup = -1
            override fun onGroupExpand(groupPosition: Int) {
                if (groupPosition != previousGroup) expandableListView.collapseGroup(previousGroup)
                previousGroup = groupPosition
            }
        })

    }


}