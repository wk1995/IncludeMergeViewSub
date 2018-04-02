# 1.include:
- 说明：提高include里面布局的复用，便于对相同视图内容进行统一的控制管理，提高布局重用性。然而，使用<include>标签总有一些值得我们注意的地方。
- 当include有id，且与include所对应的layout的id不同的时候，直接使用layout里面的控件，会出现空指针异常，两种方法解决该问题，1是直接使用include的id，二是修改include的id，使其id与layout的id一样，综合上述两种解决方案，include中的id会覆盖掉其所对应的layout的根的id
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
- 在include标签中所有的android:layout_*都是有效的，前提是必须要写layout_width和layout_height两个属性。除了visibility之外。
# 2.merge
- 说明：merge翻译成中文是合并的意思，在Android中通过使用merge能够减少视图的节点数， 
从而减少视图在绘制过程消耗的时间，达到提高UI性能的效果。使用merge时通常需要注意以下几点：
1. merge必须放在布局文件的根节点上。
2. merge并不是一个ViewGroup，也不是一个View，它相当于声明了一些视图，等待被添加。
3.  merge标签被添加到A容器下，那么merge下的所有视图将被添加到A容器下。
4.  因为merge标签并不是View，所以在通过LayoutInflate.inflate方法渲染的时候， 第二个参数必须指定一个父容器，且第三个参数必须为true，也就是必须为merge下的视图指定一个父亲节点。
5.  如果Activity的布局文件根节点是FrameLayout，可以替换为merge标签，这样，执行setContentView之后，会减少一层FrameLayout节点。
6.  自定义View如果继承LinearLayout，建议让自定义View的布局文件根节点设置成merge，这样能少一层结点。
7.  因为merge不是View，所以对merge标签设置的所有属性都是无效的。
8.  在主界面中，<include>标签的parent ViewGroup与包含的layout根容器ViewGroup==是相同的类型==，那么则可以将包含的layout根容器ViewGroup使用<merge>标签代替，从而减少一层ViewGroup的嵌套，从而提升UI性能渲染（例如activity的根节点为垂直的LinearLayout，那么include对应的layout的根节点就可以用merge代替且也会垂直的显示）
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
![image](C:/Users/Administrator/Desktop/merge.png)
```
<merge xmlns:android="http://schemas.android.com/apk/res/android">
    <TextView
        android:text="merge为根节点"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
</merge>
```
![image](C:/Users/Administrator/Desktop/frameLayout1.png)
我们可以看到，如果使用merge，明显少了一个FrameLayout节点，这也算一个视图优化技巧。
- 对于第六点：自定义View如果继承LinearLayout，建议让自定义View的布局文件根节点设置成merge，这点需要注意的是由于merge不是View，额外的属性需要通过代码来添加，具体的例子可以查看 https://blog.csdn.net/a740169405/article/details/50473909

# 3.参考
1. https://blog.csdn.net/a740169405/article/details/50473909
2. https://blog.csdn.net/xyz_lmn/article/details/14524567
# 4.相关代码