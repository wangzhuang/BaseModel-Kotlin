package com.model.basemodel

import com.model.basemodel.ui.activity.base.BaseActivity
import org.jetbrains.anko.intentFor


/**
 * BaseModel
 * WZ
 */
class TestActivity : BaseActivity() {
    override fun getIntentMessageData() {

    }

    override val title: String = "测试activity"
    override val layoutResId: Int = R.layout.activity_main

    override fun initView() {



    }

    override fun initData() {
        startActivity(intentFor<MainListActivity>())
    }
}