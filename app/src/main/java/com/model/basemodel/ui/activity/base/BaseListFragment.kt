package com.yimai.app.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.model.basemodel.R
import com.model.basemodel.ui.activity.base.IBase
import com.scwang.smartrefresh.layout.SmartRefreshLayout

import kotlinx.android.synthetic.main.common_list.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * BaseModel
 * Created by WZ.
 */
abstract class BaseListFragment : IBase, Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(layoutResId, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListViewFrame(view)
        initData()
        initErrorLayout()
    }

    var mRefreshLayout: SmartRefreshLayout? = null

    fun initListViewFrame(view: View?) {
        mRefreshLayout = view?.findViewById(R.id.refreshLayout)
                as SmartRefreshLayout
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
                refreshLayout.finishLoadMoreWithNoMoreData()//将不会再次触发加载更多事件
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
    fun initErrorLayout() {
        error_layout.onClick {
            onRefresh()
        }
    }

    open fun onEvent(event: Any) {

    }

    abstract override fun initView()
    abstract override fun initData()
    abstract fun onRefresh()
    abstract fun onLoadMore()

    abstract val layoutResId: Int


    override fun onStop() {
        super.onStop()
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


}