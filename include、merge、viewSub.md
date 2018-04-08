# 1.include:
- 说明：提高include里面布局的复用，便于对相同视图内容进行统一的控制管理，提高布局重用性。然而，使用<include>标签总有一些值得我们注意的地方。
- ***使用include最常见的问题就是findViewById查找不到目标控件，这个问题出现的前提是在include时设置了id，而在findViewById时却用了被include进来的布局的根元素id***.当include有id，且与include所对应的layout的id不同的时候，直接使用layout里面的控件，会出现空指针异常，两种方法解决该问题，1是直接使用include的id，二是修改include的id，使其id与layout的id一样，综合上述两种解决方案，include中的id会覆盖掉其所对应的layout的根的id
```
<include
        android:id="@+id/include1"
        layout="@layout/include1"/>
```

```
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/text1"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

```
  Caused by: java.lang.IllegalStateException: text1 must not be null  at com.wk.includemergeviewsub.IncludeActivity.onCreate(IncludeActivity.kt:12)
```


# 2.merge
- 说明：merge翻译成中文是合并的意思，在Android中通过使用merge能够减少视图的节点数， 
从而减少视图在绘制过程消耗的时间，达到提高UI性能的效果。使用merge时通常需要注意以下几点：

**1. merge必须放在布局文件的根节点上。**

**2. merge并不是一个ViewGroup，也不是一个View，它相当于声明了一些视图，等待被添加。**
**3.  merge标签被添加到A容器下，那么merge下的所有视图将被添加到A容器下。**

**4.因为merge标签并不是View，所以在通过LayoutInflate.inflate方法渲染的时候，第二个参数必须指定一个父容器，且第三个参数必须为true，也就是必须为merge下的视图指定一个父亲节点**。

**5.  如果Activity的布局文件根节点是FrameLayout，可以替换为merge标签，这样，执行setContentView之后，会减少一层FrameLayout节点。**

**6.  自定义View如果继承LinearLayout，建议让自定义View的布局文件根节点设置成merge，这样能少一层结点。**
 
**7.  因为merge不是View，所以对merge标签设置的所有属性都是无效的。**

**8.  在主界面中，<include>标签的parent ViewGroup与包含的layout根容器ViewGroup==是相同的类型==，那么则可以将包含的layout根容器ViewGroup使用<merge>标签代替，从而减少一层ViewGroup的嵌套，从而提升UI性能渲染（例如activity的根节点为垂直的LinearLayout，那么include对应的layout的根节点就可以用merge代替且也会垂直的显示）**
- 关于第一点,在LayoutInflater类中rInflate方法中写道，如果merge不是根节点，报错
```
 } else if (TAG_MERGE.equals(name)) {
                throw new InflateException("<merge /> must be the root element");
```
- 关于第三点，常用在自定义View中遇到，附上系统LayoutInflater类inflate(XmlPullParser parser, @Nullable ViewGroup root, boolean attachToRoot) 方法，对于该现象的源码:

```
if (TAG_MERGE.equals(name)) {
 // 如果是merge标签，指定的root为空，或则attachToRoot为false，则抛出异常信息
    if (root == null || !attachToRoot) {
       throw new InflateException("<merge /> can be used only with a valid "
          + "ViewGroup root and attachToRoot=true");
       }
 rInflate(parser, root, inflaterContext, attrs, false);
```
- 关于第五点，我们来写个例子吧，查看视图节点我们可以通过android studio上HierarchyViewer来
```
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.wk.includeMergeViewsub.merge.MergeFrameLayoutActivity">
    <TextView
        android:text="frameLayout为根节点"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
</FrameLayout>
```
![image](https://github.com/wk1995/IncludeMergeViewSub/blob/master/merge.png)
```
<merge xmlns:android="http://schemas.android.com/apk/res/android">
    <TextView
        android:text="merge为根节点"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
</merge>
```
![image](https://github.com/wk1995/IncludeMergeViewSub/blob/master/frameLayout1.png)
我们可以看到，如果使用merge，明显少了一个FrameLayout节点，这也算一个视图优化技巧。
- 对于第六点：自定义View如果继承LinearLayout，建议让自定义View的布局文件根节点设置成merge，这点需要注意的是由于merge不是View，额外的属性需要通过代码来添加，具体的例子可以查看 https://blog.csdn.net/a740169405/article/details/50473909
# 3.ViewSub
我们在开发应用程序的时候，经常会在运行时动态根据条件来决定显示哪个View或某个布局。大多数开发者会把可能用到的View都写在上面，先把它们的可见性都设为View.GONE，然后在代码中动态的更改它的可见性。这样做的优点是逻辑简单而且控制起来比较灵活。但它的缺点很明显，耗费资源。虽然把View的初始可见View.GONE，但在Inflate布局的时候View仍然会被Inflate，也就是说仍然会创建对象，会被实例化，会被设置属性，导致耗费内存等资源。ViewSub最大的优点是当你需要时才会加载，使用他并不会影响UI初始化时的性能。各种不常用的布局想进度条、显示错误消息等可以使用标签，以减少内存使用量，加快渲染速度。是一个不可见的，大小为0的View。ViewStub是一个轻量级的View，它一个看不见的，不占布局位置，占用资源非常小的控件。可以为ViewStub指定一个布局，在Inflate布局的时候，只有ViewStub会被初始化，然后当ViewStub被设置为可见的时候即ViewSub.visibility=View.VISIBLE，或是调用了ViewStub.inflate()的时候，ViewStub所向的布局就会被Inflate和实例化，然后ViewStub的布局属性都会传给它所指向的布局。这样，就可以使用ViewStub来方便的在运行时控制是否显示某个布局。标签的使用如下代码所示：

```
<ViewStub
        android:id="@+id/viewSub"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout="@layout/include_layout_1"/>
```
当你想加载布局时，可以使用下面其中一种方法：
viewSub.visibility= View.VISIBLE或
viewSub.inflate()
当调用inflate()函数的时候，ViewStub被引用的资源替代，并且返回引用的view。但ViewStub也不是万能的，下面总结下ViewStub能做的事儿和什么时候该用ViewStub，什么时候该用可见性的控制。

1.ViewStub只能Inflate一次，之后ViewStub对象会被置为空。按句话说，某个被ViewStub指定的布局被Inflate后，就不能够再通过ViewStub来控制它了。

2.ViewStub只能用来Inflate一个布局文件，而不是某个具体的View，当然也可以把View写在某个布局文件中。

```
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
```

```
java.lang.IllegalStateException:  viewSub must not be null 
```
基于以上的特点，那么可以考虑使用ViewStub的情况有：
- 在程序一个生命周期期间，某个布局一旦显示就不会再发生变化（隐藏），除非重新启动
- 想要控制某个布局，而不是View（且以上一个为前提）

需要注意的是：某些布局属性要加在ViewStub而不是实际的布局上面，而ViewStub的属性在inflate()后会都传给相应的布局。
# 4.参考
1. https://blog.csdn.net/a740169405/article/details/50473909
2. https://blog.csdn.net/xyz_lmn/article/details/14524567
3. https://www.jianshu.com/p/aac07345e8c3
