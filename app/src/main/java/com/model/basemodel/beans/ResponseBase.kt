package com.model.basemodel.beans

import org.greenrobot.eventbus.XEventObject
import java.io.Serializable

/**
 * author ：king
 * date : 2020/8/10 8:57
 * description :
 */
class ResponseBase<T>(var data: T, subscriber: Any?) : XEventObject(subscriber),
    Serializable {

    companion object {
        private const val serialVersionUID = 7757077846026862522L
    }

}