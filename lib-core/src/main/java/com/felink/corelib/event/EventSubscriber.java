package com.felink.corelib.event;

import android.os.Bundle;

/**
 * description: 消息接收者<br/>
 * author: dingdj<br/>
 * date: 2015/3/3<br/>
 */
public interface EventSubscriber {

    //进行事件处理
    public void dealEvent(String eventKey, Bundle bundle);

}
