package jdk.mina.future.constant;



import jdk.mina.future.message.FutureMessage;
import jdk.mina.future.message.HeartBeatFutureMessage;
import jdk.mina.future.message.HqFutureContractMess;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 期货行情事件枚举
 * @Date 2017/08/16 15:47
 */
public enum EventEnum {
    HQ_FUTURE_CONTRACT(10004, HqFutureContractMess.class),
    HEART_BEAT(90001, HeartBeatFutureMessage.class);
    private int eventId;
    private Class<? extends FutureMessage> messageType;

    static Map<Integer, EventEnum> eventIdMap = new ConcurrentHashMap<Integer, EventEnum>();
    static {
        for(EventEnum eventEnum : values()) {
            eventIdMap.put(eventEnum.getEventId(), eventEnum);
        }
    }

    public static EventEnum getByEventId(int eventId) {
        return eventIdMap.get(eventId);
    }

    EventEnum(int eventId, Class<? extends FutureMessage> messageType) {
        this.eventId = eventId;
        this.messageType = messageType;
    }

    public int getEventId() {
        return eventId;
    }

    public Class<? extends FutureMessage> getMessageType() {
        return messageType;
    }
}
