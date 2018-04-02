package com.wk.includeMergeViewsub.include

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.wk.includeMergeViewsub.R
import kotlinx.android.synthetic.main.include_layout_1.*

/**
 * <pre>
 *      author : wk
 *      e-mail : 122642603@qq.com
 *      time   : 2018/04/02
 *      GitHub : https://github.com/wk1995
 *      CSDN   : http://blog.csdn.net/qq_33882671
 *      desc   : 有两个include
 * </pre>
 */
class IncludeLayout_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.include_layout_activity)
        text1.text="wk"
    }
}
