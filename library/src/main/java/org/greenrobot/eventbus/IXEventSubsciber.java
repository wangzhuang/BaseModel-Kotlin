package org.greenrobot.eventbus;

/**
 * @author
 * @date
 * @desc
 */

public interface IXEventSubsciber {
    /**
     * @return 区分发送者到底选择哪个订阅者
     */
    Object getId();
}
