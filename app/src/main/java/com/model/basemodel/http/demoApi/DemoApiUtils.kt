package com.model.basemodel.http.demoApi

import android.content.Context
import com.model.basemodel.beans.response.TestBase
import com.model.basemodel.http.ApiFactory
import com.model.basemodel.http.EnqueueCallback

/**
 * BaseModel
 * Created by WZ.
 */
fun userInfo(context: Context,isNomal:Boolean = true) {
    ApiFactory.getDemoAPI()
            ?.userInfo()
            ?.enqueue(EnqueueCallback<ArrayList<TestBase>>(context,isNomal))
}