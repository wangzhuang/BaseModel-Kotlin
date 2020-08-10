package com.model.basemodel.http



import android.content.Context
import com.model.basemodel.beans.ResponseBase
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.IXEventSubsciber
import org.greenrobot.eventbus.XEventBus
import retrofit2.Response

/**
 * hzx-andriod
 * Created by WZ.
 */
object HttpCommonUtil {
    fun <T> putMessageToActivity(context: Context,p1: Response<T>?,isNormal:Boolean) {
        if (p1?.code() == 200) {
            when (p1.headers()?.get("error-code").isNullOrEmpty()) {
                true -> {
                    if (isNormal) {
                        XEventBus.getDefault()
                            .post(ResponseBase(p1.body(), object : IXEventSubsciber {
                                override fun getId(): Any {
                                    return context
                                }

                            }))
                    } else {
                        EventBus.getDefault().post(p1.body()?: "")
                    }
                }
                false -> {
                    return
                }
            }
        } else {
            //错误处理
        }

    }

}