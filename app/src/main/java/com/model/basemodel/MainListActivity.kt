package com.model.basemodel

import android.Manifest
import android.content.pm.PackageManager
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.alibaba.fastjson.JSON
import com.model.basemodel.beans.ResponseBase
import com.model.basemodel.beans.Test
import com.model.basemodel.beans.Test2
import com.model.basemodel.beans.response.TestBase
import com.model.basemodel.http.apiconfig.model
import com.model.basemodel.http.demoApi.userInfo
import com.model.basemodel.util.clickThrottleFirst
import com.orhanobut.logger.Logger
import com.yimai.app.ui.base.BaseListActivity
import kotlinx.android.synthetic.main.common_list.*
import net.idik.lib.slimadapter.SlimAdapter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.IXEventSubsciber
import org.greenrobot.eventbus.XEventBus
import org.jetbrains.anko.toast

/**
 * BaseModel
 * Created by WZ
 */
class MainListActivity : BaseListActivity() , IXEventSubsciber {
    override fun getIntentMessageData() {
    }
    override val layoutResId: Int = R.layout.common_list
    override val isStatusBarTransient: Boolean = true
    private val adapter by lazy {
        SlimAdapter.create().register<model>(R.layout.view_demo) {
            data, injector ->
            injector.text(R.id.title, data.title)
//            val image = injector.findViewById<ImageView>(R.id.image) as ImageView
//            Glide.with(this@MainListActivity).asGif()
//                    .load(data.logo).into(image)
        }.attachTo(mRecyclerView)
    }
    val list = mutableListOf<Any>()
    override val title: String by lazy {
        "列表"
    }

    override fun initView() {
        requestCallPhone()
        clickThrottleFirst(button){
            XEventBus.getDefault().post(Test(object : IXEventSubsciber {
                override fun getId(): MainListActivity {
                    return this@MainListActivity
                }
            }))
            EventBus.getDefault().post(Test2())
        }

    }

    override fun initData() {
        userInfo(this,false)
        addTestData()
    }



    private fun addTestData() {
        var Model: model? = null
        for (i in 0..10) {
            Model = model()
            Model.title = i.toString()
            Model.desc = "描述 $i"
            list.add(Model)
        }
        adapter.updateData(list).notifyDataSetChanged()
        refreshComplete()
    }

    override fun onRefresh() {
        initData()
    }

    override fun onLoadMore() {
    }
    private val CALL_PHONE_REQUEST_CODE = 0x0001

    private fun requestCallPhone() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //没有权限则动态申请
            requestPermission(Manifest.permission.CAMERA,getString(R.string.internal_storage_permissions),CALL_PHONE_REQUEST_CODE);
        } else {
            //已经有权限直接打电话
            toast("有权限")
        }
    }

    //获取用户的反馈 是否授予了权限
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        if (requestCode == CALL_PHONE_REQUEST_CODE) {
            if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授权了
                toast("用户授权了")
            } else {
                //没有授权提示用户
                toast("没有授权")
            }
        }
    }
    override fun onEvent(event: Any) {
        super.onEvent(event)
        when (event) {
            is model -> {
                Logger.json(JSON.toJSONString(event))

                for (i in 0..2) {
                    event.let { list.add(it) }
                }
                adapter.updateData(list).notifyDataSetChanged()
                refreshComplete()
            }
            is ResponseBase<*> ->{
                when(event.data){
                    is ArrayList<*> ->{
                        var array:ArrayList<TestBase> = event.data as ArrayList<TestBase>
                        Log.i("========Main=", array[0].title)
                    }
                }
            }
            is Test ->{
                Log.i("-----------========","MainListActivity recend Test")
            }
            is Test2 ->{
                Log.i("-----------========","MainListActivity recend Test2")
            }
            is ArrayList<*> ->{
                var array:ArrayList<TestBase> = event as ArrayList<TestBase>
                Log.i("========Main=", array[0].title)
            }
        }
    }
    //接收者需要实现同样的接口
    override fun getId(): Any? {
        return this
    }


}