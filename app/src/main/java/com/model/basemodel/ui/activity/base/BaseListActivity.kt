package com.yimai.app.ui.base

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bluejamesbond.text.Console
import com.gyf.immersionbar.ImmersionBar
import com.model.basemodel.R
import com.model.basemodel.ui.activity.base.IBase
import com.model.basemodel.util.PermissionHelper
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.common_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * BaseModel
 * Created by WZ.
 */
abstract class BaseListActivity : IBase, AppCompatActivity(), AnkoLogger {
    private var statusBarHeight = 0
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setView(layoutResId,isStatusBarTransient,-1)
        initToolBar()
        initListViewFrame()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        getIntentMessageData()
        initView()
        initData()
        initErrorLayout()
    }
    private fun setView(
        layoutId: Int,
        isStatusBarCover: Boolean,
        offsetViewId: Int
    ) {
        llContentView.removeAllViews()
        val childView: View = LayoutInflater.from(this).inflate(layoutId, null)
        statusBarHeight = getStatusBarHeight()
        if (!isStatusBarCover) { //状态栏不透明显示时，需要将整体布局下移
            childView.setPadding(
                childView.paddingLeft,
                childView.paddingTop + statusBarHeight,
                childView.paddingRight,
                childView.paddingBottom
            )
            ImmersionBar.with(this).statusBarDarkFont(false, 1.0f).statusBarColor(statusBarColor)
                .init()
        } else { //状态栏透明覆盖时，需要将标题栏下移
            if (offsetViewId > 0) {
                val offsetView =
                    childView.findViewById<View>(offsetViewId)
                if (offsetView != null) {
                    offsetView.setPadding(
                        offsetView.paddingLeft,
                        offsetView.paddingTop + statusBarHeight,
                        offsetView.paddingRight,
                        offsetView.paddingBottom
                    )
                    Console.log("setView", "offsetView.getPaddingTop=" + offsetView.paddingTop)
                }
            }
            //            ImmersionBar.with(this).statusBarDarkFont(false, 0.0f).statusBarColor(android.R.color.transparent).init();
            ImmersionBar.with(this).statusBarDarkFont(true, 0.0f)
                .autoStatusBarDarkModeEnable(true, 0.2f).init()
        }
        llContentView.addView(
            childView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    protected open fun getStatusBarHeight(): Int {
        var statusBarHeight = -1
        //获取status_bar_height资源的ID
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) { //根据资源ID获取响应的尺寸值
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        } else {
            try {
                val clazz =
                    Class.forName("com.android.internal.R\$dimen")
                val `object` = clazz.newInstance()
                val height =
                    clazz.getField("status_bar_height")[`object`].toString().toInt()
                statusBarHeight = resources.getDimensionPixelSize(height)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //        log("getStatusBarHeight", "" + statusBarHeight);
        return statusBarHeight
    }
    fun initToolBar() {
        val toolbar = findViewById(R.id.toolbar) as? Toolbar
        toolbar?.setNavigationOnClickListener {
            finish()
        }
        val toolbar_title = findViewById(R.id.toolbar_title) as? TextView
        toolbar_title?.text = title
    }

    var mRefreshLayout: SmartRefreshLayout? = null
    var mRecyclerView: RecyclerView? = null

    private fun initListViewFrame() {
        mRecyclerView = findViewById(R.id.recyler_view)
        mRecyclerView?.apply {
            layoutManager = LinearLayoutManager(this@BaseListActivity, LinearLayoutManager.VERTICAL, false)
        }
        mRefreshLayout = findViewById(R.id.refreshLayout)
        mRefreshLayout?.setOnRefreshListener { refreshLayout ->
            refreshLayout.layout.postDelayed({
                onRefresh()
                hideErrorLayout()
                refreshLayout.finishRefresh()
                refreshLayout.resetNoMoreData()//刷新时重制loadmore
            }, 2000)
        }
        mRefreshLayout?.setOnLoadMoreListener { refreshLayout ->
            refreshLayout.layout.postDelayed({
                onLoadMore()
                refreshLayout.finishLoadMore()
//                refreshLayout.finishLoadMoreWithNoMoreData()//将不会再次触发加载更多事件
            }, 2000)
        }
        //触发自动刷新
//        refreshLayout.autoRefresh()
    }

    //停止刷新
    fun refreshComplete() {
        mRefreshLayout?.finishRefresh()
    }

    //设置网络错误时，点击重新请求
    private fun initErrorLayout() {
        error_layout.onClick {
            onRefresh()
        }
    }

    abstract val title: String
    abstract val layoutResId: Int
    abstract fun getIntentMessageData()
    abstract override fun initView()
    abstract override fun initData()
    abstract val isStatusBarTransient: Boolean
    open var statusBarColor: Int = R.color.bg_default
    abstract fun onRefresh()
    abstract fun onLoadMore()
    @Subscribe(threadMode = ThreadMode.POSTING)
    open fun onEvent(event: Any) {

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    fun showErrorLayout() {
        error_layout.visibility = View.VISIBLE
    }

    fun hideErrorLayout() {
        error_layout.visibility = View.GONE
    }

    //    getString(R.string.Do_not_open_the_lottery)
    fun resultListIsEmpty(size: Int, content: String = getString(R.string.no_data)) {
        if (size == 0) {
            showEmptylayout(content)
        } else {
            hideEmptylayout()
        }
    }


    fun showEmptylayout(content: String) {
        empty_layout.visibility = View.VISIBLE
        content_text.text = content
    }

    fun hideEmptylayout() {
        empty_layout.visibility = View.GONE
    }
    private var mAlertDialog: AlertDialog? = null
    /**
     * 请求权限
     *
     *
     * 如果权限被拒绝过，则提示用户需要权限
     */
    fun requestPermission(permission: String, rationale: String, requestCode: Int) {
        showAlertDialog(getString(R.string.internal_storage_permissions), rationale,
            DialogInterface.OnClickListener { dialog, which ->  PermissionHelper.requestPermission(this,requestCode,arrayOf(permission)); }, getString(R.string.ensure), null, getString(R.string.cancel))
    }

    /**
     * 显示指定标题和信息的对话框
     *
     * @param title                         - 标题
     * @param message                       - 信息
     * @param onPositiveButtonClickListener - 肯定按钮监听
     * @param positiveText                  - 肯定按钮信息
     * @param onNegativeButtonClickListener - 否定按钮监听
     * @param negativeText                  - 否定按钮信息
     */
    protected fun showAlertDialog(title: String?, message: String?,
                                  onPositiveButtonClickListener: DialogInterface.OnClickListener?,
                                  positiveText: String,
                                  onNegativeButtonClickListener: DialogInterface.OnClickListener?,
                                  negativeText: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener)
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener)
        mAlertDialog = builder.show()
    }

}
