package com.model.basemodel.http.apiconfig

/**
 * BaseModel
 * Created by WZ.
 */
object HttpConfig {

    val IS_TESTING_SERVER = true
    val IS_UAT = false
    val TEST_SERVER = "http://jsonplaceholder.typicode.com"
    val ONLINE_SERVER = "http://baidu.com"
    val SERVER_URL = if (IS_TESTING_SERVER)
        TEST_SERVER
    else
        ONLINE_SERVER

    fun getServiceHost(): String {
        return SERVER_URL
    }

}
