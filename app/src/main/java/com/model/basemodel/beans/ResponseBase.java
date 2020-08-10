package com.model.basemodel.beans;

import org.greenrobot.eventbus.XEventObject;

import java.io.Serializable;

/**
 * author ：king
 * date : 2020/8/10 8:57
 * description :
 */
public class ResponseBase<T> extends XEventObject implements Serializable {
    public ResponseBase(T t,Object subscriber) {
        super(subscriber);
        data = t;
    }
    public T data;
}
