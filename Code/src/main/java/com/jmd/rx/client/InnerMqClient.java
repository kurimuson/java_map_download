package com.jmd.rx.client;

import com.jmd.rx.Topic;
import com.jmd.rx.callback.OnMessageCallback;
import com.jmd.rx.callback.OnSubscribeCallback;
import io.reactivex.rxjava3.subjects.PublishSubject;

public interface InnerMqClient {

    void setOnSubscribeCallback(OnSubscribeCallback onSubscribeCallback);

    String getId();

    PublishSubject<Object> getSubject(Topic topic);

    boolean isDestroyed();

    <T> void sub(Topic topic, OnMessageCallback<T> callback);

    void destroy();

}
