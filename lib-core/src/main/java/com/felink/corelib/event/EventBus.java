package com.felink.corelib.event;

import android.os.Bundle;

import com.felink.corelib.config.Global;
import com.felink.corelib.kitset.StringUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * description: 消息总线 进行事件的分发和注册<br/>
 * author: dingdj<br/>
 * date: 2015/3/3<br/>
 */
public class EventBus {

    //更新地址信息
    public static final String UPDATE_ADDRESS_INFO = "UPDATE_ADDRESS_INFO";
    //数据变化
    public static final String DATA_SET_CHANGED = "DATA_SET_CHANGED";
    //DIY合并任务完成
    public static final String MERGING_TASK_FINISH = "MERGING_TASK_FINISH";
    //DIY 合成完成后通知主界面刷新
    public static final String NOTIFY_DIY_REFRESH = "NOTIFY_DIY_REFRESH";
    //事件和事件订阅者容器
    private HashMap<String, List<WeakReference<EventSubscriber>>> events = new HashMap<String, List<WeakReference<EventSubscriber>>>();

    private EventBus() {
    }

    public static final EventBus getInstance() {
        return EventBusHolder.instance;
    }

    //提供线程中发送一个事件通知的方法
    public static void publishEventFromWorkThread(final String eventKey, final Bundle bundle) {
        Global.runInMainThread(new Runnable() {
            @Override
            public void run() {
                EventBus.getInstance().publishEvent(eventKey, bundle);
            }
        });
    }

    //订阅事件
    public void subScribe(String eventKey, EventSubscriber subscriber) {
        if (StringUtil.isEmpty(eventKey) || subscriber == null) return;
        if (events.get(eventKey) == null) {
            List<WeakReference<EventSubscriber>> list = new ArrayList<WeakReference<EventSubscriber>>();
            list.add(new WeakReference<EventSubscriber>(subscriber));
            events.put(eventKey, list);
        } else {
            events.get(eventKey).add(new WeakReference<EventSubscriber>(subscriber));
        }

    }


    //取消订阅事件
    public void unSubScribe(String eventKey) {
        if (StringUtil.isEmpty(eventKey)) return;
        events.remove(eventKey);
    }

    //取消订阅事件
    public void unSubScribe(String eventKey, EventSubscriber subscriber) {
        if (StringUtil.isEmpty(eventKey)) return;
        if (events.get(eventKey) != null) {
            List<WeakReference<EventSubscriber>> list = events.get(eventKey);
            Iterator<WeakReference<EventSubscriber>> iterator = list.iterator();
            while (iterator.hasNext()) {
                WeakReference<EventSubscriber> reference = iterator.next();
                if (reference == null) {
                    continue;
                }
                EventSubscriber current = reference.get();
                if (current == null) {
                    continue;
                }
                if (subscriber == current) {
                    iterator.remove();
                    return;
                }
            }
        }
    }

    //发布事件
    public void publishEvent(String eventKey, Bundle bundle) {
        if (StringUtil.isEmpty(eventKey)) return;
        List<WeakReference<EventSubscriber>> references = events.get(eventKey);
        if (references != null) {
            Iterator<WeakReference<EventSubscriber>> iterator = references.iterator();
            while (iterator.hasNext()) {
                WeakReference<EventSubscriber> reference = iterator.next();
                if (reference == null) {
                    continue;
                }
                EventSubscriber subscriber = reference.get();
                if (subscriber != null) {
                    try {
                        subscriber.dealEvent(eventKey, bundle);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static class EventBusHolder {
        private static final EventBus instance = new EventBus();
    }

}
