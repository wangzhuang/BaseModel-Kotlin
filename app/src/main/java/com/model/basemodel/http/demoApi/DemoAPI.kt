package com.model.basemodel.http.demoApi

import com.model.basemodel.http.apiconfig.HttpHeaderConfig.loginHeader
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap


/**
 * BaseModel
 * Created by WZ.
 */
interface DemoAPI {

    @GET("posts")
    fun userInfo(@HeaderMap map: Map<String, String> = loginHeader()): Call<String>

}