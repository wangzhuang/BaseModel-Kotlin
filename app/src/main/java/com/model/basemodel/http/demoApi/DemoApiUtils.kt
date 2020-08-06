package com.model.basemodel.http.demoApi

import com.model.basemodel.http.ApiFactory
import com.model.basemodel.http.EnqueueCallback

/**
 * BaseModel
 * Created by WZ.
 */
fun userInfo() {
    ApiFactory.getDemoAPI()
            ?.userInfo()
            ?.enqueue(EnqueueCallback<String>())
}