package com.model.basemodel

import com.model.basemodel.ui.activity.base.BaseActivity

/**
 * 主Activity
 */
class MainActivity : BaseActivity() {


    override val title: String by lazy {
        ""
    }
    override val layoutResId: Int = R.layout.activity_main
    override val isStatusBarTransient: Boolean = true

    override fun getIntentMessageData() {
    }

    override fun initView() {
    }

    override fun initData() {
    }


}