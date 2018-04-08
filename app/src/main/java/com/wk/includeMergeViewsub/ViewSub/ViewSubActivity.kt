package com.wk.includeMergeViewsub.ViewSub

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.wk.includeMergeViewsub.R
import kotlinx.android.synthetic.main.activity_view_sub.*



class ViewSubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_sub)

        btnShow.setOnClickListener {
            viewSub.visibility= View.VISIBLE
            // or
//            viewSub.inflate()
        }
        btnHide.setOnClickListener {
            viewSub.visibility= View.GONE
        }
    }
}
