package com.model.basemodel.ui.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.model.basemodel.ui.activity.base.IBase

/**
 * yimai_android
 * Created by WZ.
 */
abstract class BaseDialog : IBase, DialogFragment() {

    abstract val layoutResId: Int
    abstract override fun initView()
    abstract override fun initData()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(layoutResId, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }
}