package com.wk.includeMergeViewsub.include

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.wk.includeMergeViewsub.R

/**
 * <pre>
 *      author : wk
 *      e-mail : 122642603@qq.com
 *      time   : 2018/04/02
 *      GitHub : https://github.com/wk1995
 *      CSDN   : http://blog.csdn.net/qq_33882671
 *      desc   : 在include标签里面加入了与include相关联的layout不同的id
 * </pre>
 */
class IncludeIdErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.include_id_error_activity)
        //把include的id换成text1就不会报错了
//        text1.text = "include加上了id，与layout的id不同"

        //下面这样写也不会报错，换句话说include的id会覆盖掉其所指的layout的根id
//        ( include_layout_1 as TextView).text="include加上了id，这里是不同的"

        //下面这样写也不行,因为v其实就是textView，如果对应的layout根布局是ViewGroup，那么这个就行了
//        val v=findViewById(R.id.include1)
//        val text=v.findViewById<TextView>(R.id.text1)
//        text.text = "include加上了id，与layout的id不同"
    }
}
