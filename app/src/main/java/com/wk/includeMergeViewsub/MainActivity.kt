package com.wk.includeMergeViewsub

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import com.wk.includeMergeViewsub.include.IncludeIdErrorActivity
import com.wk.includeMergeViewsub.include.IncludeLayout_Activity
import com.wk.includeMergeViewsub.include.IncludeSimpleNormalActivity
import com.wk.includeMergeViewsub.merge.MergeFrameLayoutActivity
import com.wk.includeMergeViewsub.merge.MergeIncludeActivity
import com.wk.includeMergeViewsub.merge.MergeIncludeMergeActivity
import com.wk.includeMergeViewsub.merge.MergeMergeActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),AdapterView.OnItemClickListener {
    private val ss by lazy {
        val s = ArrayList<String>()
        s.add("最简单的include，正常")
        s.add("include的id与其指向layout里面的id不同，错误，include的id会覆盖掉layout的id")
        s.add("include的layout_属性")
        s.add("Merge:FrameLayout为根节点")
        s.add("Merge:FrameLayout为根节点替换成merge")
        s.add("Merge:include没用merge")
        s.add("Merge:include用merge")
        s
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,0,ss) as ListAdapter
        list.setOnItemClickListener(this)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(this,ss[position],Toast.LENGTH_SHORT).show()
        var activityClass:Class<Activity>?=null
        when(position){
            0->{
                activityClass= IncludeSimpleNormalActivity().javaClass
            }
            1->{
                activityClass= IncludeIdErrorActivity().javaClass
            }
            2->{
                activityClass= IncludeLayout_Activity().javaClass
            }
            3->{
                activityClass= MergeFrameLayoutActivity().javaClass
            }
            4->{
                activityClass= MergeMergeActivity().javaClass
            }
            5->{
                activityClass= MergeIncludeActivity().javaClass
            }
            6->{
                activityClass= MergeIncludeMergeActivity().javaClass
            }
        }
        if(activityClass==null) return
        val intent= Intent(this@MainActivity,activityClass)
        startActivity(intent)
    }
}
