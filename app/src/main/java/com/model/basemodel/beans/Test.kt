package com.model.basemodel.beans

import org.greenrobot.eventbus.XEventObject
import java.io.Serializable

/**
 * @author fujinhu
 * @date 2018/5/30
 * @desc
 */
class Test(subscriber: Any?) : XEventObject(subscriber), Serializable {
    companion object {
        private const val serialVersionUID = -1852662648332163938L
    }
}