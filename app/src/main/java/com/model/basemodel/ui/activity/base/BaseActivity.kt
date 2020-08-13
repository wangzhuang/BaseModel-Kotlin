package com.model.basemodel.ui.activity.base

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bluejamesbond.text.Console.log
import com.gyf.immersionbar.ImmersionBar
import com.model.basemodel.R
import com.model.basemodel.ui.dialog.ProgressBarDialog
import com.model.basemodel.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_base.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.XEventBus
import org.jetbrains.anko.AnkoLogger


/**
 * BaseModel
 * Created by WZ.
 */
abstract class BaseActivity : IBase, AppCompatActivity(), AnkoLogger {
    private var statusBarHeight = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setView(layoutResId,isStatusBarTransient,-1)
        val toolbar = findViewById(R.id.toolbar) as? Toolbar
        toolbar?.setNavigationOnClickListener {
            onBack()
        }
        val toolbar_title = findViewById(R.id.toolbar_title) as? TextView
        toolbar_title?.text = title

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        if (!XEventBus.getDefault().isRegistered(this)) {
            XEventBus.getDefault().register(this)
        }
        getIntentMessageData()
        initView()
        initData()
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
                    log("setView", "offsetView.getPaddingTop=" + offsetView.paddingTop)
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

    open fun onBack() {
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
    }


    abstract val title: String
    abstract val layoutResId: Int
    abstract val isStatusBarTransient: Boolean
    open var statusBarColor: Int = R.color.bg_gray
    abstract fun getIntentMessageData()
    abstract override fun initView()
    abstract override fun initData()

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
        XEventBus.getDefault().unregister(this);
        super.onDestroy()
    }

    override fun getResources(): Resources {
        val configuration = Configuration()
        configuration.setToDefaults()
        return updateConfiguration(configuration)
    }

    private fun updateConfiguration(config: Configuration): Resources {
        val mContext: Context = baseContext
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mContext.createConfigurationContext(config)
        } else {
            mContext.resources
                .updateConfiguration(config, baseContext.resources.displayMetrics)
        }
        return mContext.resources
    }

    private var mAlertDialog: AlertDialog? = null
    /**
     * 请求权限
     *
     *
     * 如果权限被拒绝过，则提示用户需要权限
     */
    fun requestPermission(permission: String, rationale: String, requestCode: Int) {
        showAlertDialog(
            getString(R.string.internal_storage_permissions), rationale,
            DialogInterface.OnClickListener { dialog, which ->
                PermissionHelper.requestPermission(
                    this,
                    requestCode,
                    arrayOf(permission)
                );
            }, getString(R.string.ensure), null, getString(R.string.cancel)
        )
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
    protected fun showAlertDialog(
        title: String?, message: String?,
        onPositiveButtonClickListener: DialogInterface.OnClickListener?,
        positiveText: String,
        onNegativeButtonClickListener: DialogInterface.OnClickListener?,
        negativeText: String
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener)
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener)
        mAlertDialog = builder.show()
    }

    var progress_bar_dialog: ProgressBarDialog? = null

    fun showProgressBarDialog(text: String) {
        progress_bar_dialog = ProgressBarDialog(text)
        progress_bar_dialog?.show(supportFragmentManager, "progress_bar_dialog")
    }

    fun hideProgressBarDialog() {
        progress_bar_dialog?.dismiss()
    }


}
