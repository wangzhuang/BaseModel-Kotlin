package com.model.basemodel.beans.response

import java.io.Serializable

/**
 * author ：king
 * date : 2020/8/10 9:28
 * description :
 */
class TestBase : Serializable {
    /**
     * userId : 1
     * id : 1
     * title : sunt aut facere repellat provident occaecati excepturi optio reprehenderit
     * body : quia et suscipit
     * suscipit recusandae consequuntur expedita et cum
     * reprehenderit molestiae ut ut quas totam
     * nostrum rerum est autem sunt rem eveniet architecto
     */
    var userId = 0
    var id = 0
    var title: String? = null
    var body: String? = null

    companion object {
        private const val serialVersionUID = 3015778024035299305L
    }
}