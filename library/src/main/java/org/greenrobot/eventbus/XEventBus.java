package org.greenrobot.eventbus;

/**
 * @author
 * @date
 * @desc
 */

public class XEventBus extends EventBus {
    public XEventBus() {
        super();
    }

    public static EventBus getDefault() {
        if (EventBus.defaultInstance instanceof XEventBus) {
            return EventBus.defaultInstance;
        }
        final XEventBus xEventBus = new XEventBus();
        EventBus.getDefault();
        return (EventBus.defaultInstance = xEventBus);
    }

    @Override
    void invokeSubscriber(Subscription subscription, Object event) {
        if (event instanceof XEventObject) {
            Object subscriber = ((XEventObject) event).subscriber;
            if (subscriber instanceof IXEventSubsciber) {
                IXEventSubsciber sender = (IXEventSubsciber) subscriber;
                //根据ID匹配
                if (sender.getId() != null && sender.getId().equals(subscription.subscriber)) {
                    super.invokeSubscriber(subscription, event);
                }
            } else {
                //直接根据对象匹配
                if (subscription.subscriber != null && subscription == subscriber) {
                    super.invokeSubscriber(subscription, event);
                }
            }
        } else {
            super.invokeSubscriber(subscription, event);
        }
    }
}
