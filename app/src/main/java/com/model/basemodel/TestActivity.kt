package com.model.basemodel

import android.util.Log
import com.model.basemodel.beans.ResponseBase
import com.model.basemodel.beans.Test
import com.model.basemodel.beans.Test2
import com.model.basemodel.beans.response.TestBase
import com.model.basemodel.ui.activity.BrowserActivity
import com.model.basemodel.ui.activity.BrowserActivity.PARAM_MODE
import com.model.basemodel.ui.activity.BrowserActivity.PARAM_URL
import com.model.basemodel.ui.activity.base.BaseActivity
import org.greenrobot.eventbus.IXEventSubsciber
import org.jetbrains.anko.intentFor


/**
 * BaseModel
 * WZ
 */
class TestActivity : BaseActivity() , IXEventSubsciber {
    override fun getIntentMessageData() {

    }

    override val title: String = "测试activity"
    override val layoutResId: Int = R.layout.activity_main
    override val isStatusBarTransient: Boolean = true
    override fun initView() {
    }

    override fun initData() {
        startActivity(intentFor<BrowserActivity>(PARAM_MODE to 1,PARAM_URL to "https://github.com/Justson/AgentWeb"))
    }

    override fun onEvent(event: Any) {
        super.onEvent(event)
        when(event){
            is Test ->{
                Log.i("--------========","TestActivity recend Test")
            }
            is Test2 ->{
                Log.i("-----------========","MainListActivity recend Test2")
            }
            is ResponseBase<*> ->{
                when(event.data){
                    is ArrayList<*> ->{
                        var array:ArrayList<TestBase> = event.data as ArrayList<TestBase>
                            Log.i("=======TestActivity==", array[0].title)
                        }
                    }
                }
            is ArrayList<*> ->{
                var array:ArrayList<TestBase> = event as ArrayList<TestBase>
                Log.i("=======TestActivity==", array[0].title)
            }
            }
        }



    override fun getId(): Any {
        return this
    }

}