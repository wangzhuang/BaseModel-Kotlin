package com.model.basemodel.http

import android.content.Context
import com.orhanobut.logger.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * BaseModel
 * Created by WZ.
 */
fun <T> EnqueueCallback(context: Context,isNormal:Boolean = true): Callback<T> = object : Callback<T> {
    override fun onResponse(p0: Call<T>?, p1: Response<T>?) {
        p1.let {
            HttpCommonUtil.putMessageToActivity(context,p1,isNormal)
        }
    }

    override fun onFailure(p0: Call<T>?, p1: Throwable?) {
        Logger.e(p0?.request()?.url?.toString() + "\n" + p1?.message + "\n" + p0?.request()?.body)
    }
}
