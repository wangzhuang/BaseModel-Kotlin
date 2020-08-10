package org.greenrobot.eventbus;

/**
 * @author
 * @date
 * @desc
 */

public class XEventObject {
    public Object subscriber;

    public XEventObject(Object subscriber) {
        this.subscriber = subscriber;
    }

    public XEventObject(IXEventSubsciber subscriber) {
        this.subscriber = subscriber;
    }

}
